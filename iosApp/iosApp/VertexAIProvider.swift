//
//  VertexAIProvider.swift
//  iosApp
//
//  Created by Vlatko Galev on 6.10.26.
//

import Shared
import UIKit
import FirebaseAI
import Foundation

class VertexAIProvider: Shared.VertexAIProvider {

    static let shared = VertexAIProvider()

    let ai = FirebaseAI.firebaseAI(backend: .vertexAI())

    func identifyObject(prompt: String, obverseSideImageData: Data, reverseSideImageData: Data) async throws -> String? {
        guard let obverseSideImage = UIImage(data: obverseSideImageData) else {
            return "Obverse side Bitmap decoding failed."
        }
        guard let reverseSideImage = UIImage(data: reverseSideImageData) else {
            return "Reverse side Bitmap decoding failed."
        }

        let model = ai.generativeModel(modelName: "gemini-3.5-flash")
        let response = try await model.generateContent(obverseSideImage, reverseSideImage, prompt)


        guard let text = response.text else {
            return "Object identification failed."
        }

        return text
    }
}
