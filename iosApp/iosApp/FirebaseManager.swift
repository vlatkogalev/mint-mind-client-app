//
//  FirebaseManager.swift
//  iosApp
//
//  Created by Vlatko Galev on 6.10.2026.
//

import Foundation
import FirebaseCore
import FirebaseFirestore
import FirebaseAuth
import FirebaseCrashlytics
import FirebaseAnalytics
import Shared

class FirebaseManager: NSObject, Shared.FirebaseManager {
    static let shared = FirebaseManager()

    private let db: Firestore
    private let auth: Auth
    private let crashlytics: Crashlytics

    private override init() {
        guard FirebaseApp.app() != nil else {
            fatalError("FirebaseApp.configure() must be called before FirebaseControllerIOS is initialized.")
        }
        self.db = Firestore.firestore(database: "mura-reports-db")

        self.auth = Auth.auth()
        self.crashlytics = Crashlytics.crashlytics()
        super.init()
    }

    func reportImage(
        prompt: String, generatedPrompt: String, category: String,
        callback: @escaping (String?, String?) -> Void
    ) {
        let reportData: [String: Any] = [
            "timestamp": FieldValue.serverTimestamp(),
            "original_prompt": prompt,
            "generated_prompt": generatedPrompt,
            "reportCategory": category,
        ]

        let newDocumentRef = db.collection("image_reports").document()

        newDocumentRef.setData(reportData) { error in
            if let error = error {
                _ = callback(nil, error.localizedDescription)
            } else {
                _ = callback(newDocumentRef.documentID, nil)
            }
        }
    }

    func signInAnonymously() async throws -> String? {
        do {
            let authResult = try await auth.signInAnonymously()
            return authResult.user.uid
        } catch {
            print("Firebase Anonymous Auth failed (Swift): \(error.localizedDescription)")
            throw error
        }
    }

    func getCurrentUserId() -> String? {
        return auth.currentUser?.uid
    }

    func setUserId(userId: String) {
        crashlytics.setUserID(userId)
    }

    func logMessage(message: String) {
        crashlytics.log(message)
    }

    func recordHandledException(throwable: KotlinThrowable) {
        let userInfo: [String: Any] = [
            NSLocalizedDescriptionKey: throwable.message ?? "Kotlin Throwable",
            "KotlinExceptionType": String(describing: type(of: throwable)),
        ]
        let error = NSError(domain: "KotlinExceptionDomain", code: 0, userInfo: userInfo)
        crashlytics.record(error: error)
    }

    func setCustomKey(key: String, value: Any) {
        crashlytics.setCustomValue(value, forKey: key)
    }

    @objc func logEvent(name: String, params: [String: Any]?) {
        print("Attempting to log Firebase Analytics event (Swift): \(name) with params: \(params ?? [:])")

        Analytics.logEvent(name, parameters: params)
        print("Called Analytics.logEvent(\(name), parameters: \(params?.keys.joined(separator: ", ") ?? ""))")
    }
}
