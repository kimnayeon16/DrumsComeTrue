import org.jetbrains.kotlin.storage.CacheResetOnProcessCanceled.enabled

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("de.undercouch.download")
}

android {

    namespace = "com.ssafy.drumscometrue"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.ssafy.drumscometrue"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
}

// import DownloadMPTasks task
project.ext.set("ASSET_DIR", projectDir.toString() + "/src/main/assets")
apply(from = "download_tasks.gradle")

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")

    // MediaPipe Library
    implementation ("com.google.mediapipe:tasks-vision:latest.release")

    // Navigation library
    implementation ("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation ("androidx.navigation:navigation-ui-ktx:2.5.3")

    // CameraX core library
    implementation ("androidx.camera:camera-core:1.2.0-alpha02")

    // CameraX Camera2 extensions
    implementation ("androidx.camera:camera-camera2:1.2.0-alpha02")

    // CameraX Lifecycle library
    implementation ("androidx.camera:camera-lifecycle:1.2.0-alpha02")

    // CameraX View class -> 미리보기 화면구성
    implementation ("androidx.camera:camera-view:1.2.0-alpha02")

    // WindowManager
    implementation ("androidx.window:window:1.1.0-alpha03")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}