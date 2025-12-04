plugins {
    alias(libs.plugins.android.application)
    // You may need to add this if it's not in your libs.versions.toml
    id("org.jetbrains.kotlin.android") version "1.9.0"
}

android {
    namespace = "com.example.lab032"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.lab032"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11

        kotlinOptions {
            jvmTarget = "11"
        }
    }

    // THE "packaging" BLOCK HAS BEEN REMOVED.
    // THE STRAY "resources" BLOCK HAS BEEN REMOVED.
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // THE "appcrawler.platform" DEPENDENCY HAS BEEN REMOVED.

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
