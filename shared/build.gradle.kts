import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidMultiplatformLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    android {
        namespace = "com.vlatkogalev.mintmind.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
        withHostTest {
            isIncludeAndroidResources = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.exifinterface)
            implementation(project.dependencies.platform(libs.firebase.bom))
            implementation(libs.bundles.firebase.android)

            implementation(libs.ktor.client.okhttp)
            implementation(libs.androidx.exifinterface)

            api(libs.androidx.startup)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        commonMain.dependencies {
            implementation(libs.bundles.kotlinx)
            implementation(libs.bundles.androidx)
            implementation(libs.bundles.compose)
            implementation(libs.bundles.navigation)
            implementation(libs.bundles.koin)
            implementation(libs.bundles.room)
            implementation(libs.bundles.coil)
            implementation(libs.bundles.connectivity)
            implementation(libs.bundles.ktor)
            implementation(libs.bundles.filekit)
            implementation(libs.material.icons.extended)
            implementation(libs.zoomable)
            implementation(libs.napier)
            implementation(libs.camerak)
            api(libs.kmpnotifier)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.ui.tooling)
    ksp(libs.room.compiler)
}

room {
    schemaDirectory("$projectDir/schemas")
}
