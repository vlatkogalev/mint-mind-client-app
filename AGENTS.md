# AGENTS.md - MintMind

Kotlin Multiplatform project (Compose Multiplatform) targeting Android + iOS.

## Build & Run

| Target            | Command                                                                 |
|-------------------|-------------------------------------------------------------------------|
| Android debug APK | `./gradlew :androidApp:assembleDebug`                                   |
| Android release   | `./gradlew :androidApp:assembleRelease` (debug-signed, no minification) |
| Shared tests      | `./gradlew :shared:check` (test infra exists, no tests written yet)     |
| iOS               | Open `iosApp/` in Xcode, run the `iosApp` scheme                        |

There is no `:shared:build` shortcut for iOS framework — the Xcode build triggers it via Gradle
integration.

## Architecture

- **Monorepo**: `:androidApp` (thin shell) depends on `:shared` (all logic + UI).
- **`:shared`** builds a **static** framework (`Shared.framework`) for `iosArm64` +
  `iosSimulatorArm64`.
- **Source sets**: `commonMain/` contains all shared code (UI, domain, data, DI). `androidMain/` and
  `iosMain/` provide `expect`/`actual` implementations and platform-specific deps (Ktor engine,
  Firebase, AI providers).
- **Feature-based clean architecture**: each feature (`auth`, `feed`, `identify`, `user`, `app`,
  `notification`) is self-contained with `data/`, `domain/`, `presentation/`, `di/` subpackages.

## Key Conventions

- **DI**: Koin 4.x. Each feature has its own DI module. Platform modules wire platform-specific
  `actual` impls.
- **Navigation**: Type-safe via sealed `Screen` class annotated with `@Serializable` + Jetpack
  Navigation Compose. Nested `NavHost` for bottom nav.
- **Networking**: Ktor 3.x with three qualified `HttpClient` beans (`AuthClient`, default with token
  refresh, `SSEClient`, `S3Client`). Uses `expect`/`actual` for engine (OkHttp on Android, Darwin on
  iOS).
- **Database**: Room 2.8.x with KSP. `expect`/`actual` `AppDatabase` constructor using
  `BundledSQLiteDriver`. Schema export is disabled.
- **AI/Computer Vision**: Abstract `AIProvider` interface — Firebase AI on Android, VertexAI on iOS.
- **Gradle conventions**: Configuration cache enabled (`org.gradle.configuration-cache=true`),
  type-safe project accessors enabled. All dependency versions in `gradle/libs.versions.toml`; use
  `libs.` accessors (version catalog + bundles).

## Gotchas

- **No CI/CD, no lint config** — no detekt, ktlint, or GitHub Actions exist.
- **Release build is debug-signed** — ProGuard is commented out; release type is a placeholder.
- **Firebase config files** (`google-services.json`, `GoogleService-Info.plist`) are gitignored —
  new clones need these provided separately.
- **Room KSP**: After adding/editing DAOs or entities, run a Gradle sync or
  `./gradlew :shared:kspCommonMainKotlinMetadata` to regenerate.
- **iOS deep links**: Handled via `MainViewController.kt` bridge layer between Swift and Compose.
- **JVM target is 11** everywhere (not 17).
