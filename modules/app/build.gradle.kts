plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

android {
    namespace = "com.duchastel.simon.simplelauncher"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.duchastel.simon.simplelauncher"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":modules:libs:ui"))
    implementation(project(":modules:features:homepage-action"))
    implementation(project(":modules:features:app-list"))
    implementation(project(":modules:libs:permissions"))
    implementation(project(":modules:libs:contacts"))
    implementation(project(":modules:features:homepage"))
    implementation(project(":modules:features:settings"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.circuit.foundation)
    implementation(libs.androidx.ui.tooling.preview.android)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
}