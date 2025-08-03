plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

android {
    namespace = "com.duchastel.simon.simplelauncher.features.applist"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":ui"))

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.material3)
//    implementation(libs.androidx.compose.runtime)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation(libs.circuit.foundation)
//    implementation(libs.circuit.runtime.presenter)
//    implementation(libs.circuit.runtime.ui)
    implementation(libs.accompanist.drawablepainter)

    testImplementation(libs.junit)
//    testImplementation(libs.circuit.test)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.test.junit4)
}

kapt {
    correctErrorTypes = true
}