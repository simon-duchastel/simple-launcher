plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "com.duchastel.simon.simplelauncher.features.homepageaction"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
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
    implementation(project(":ui"))

    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation("com.google.accompanist:accompanist-systemuicontroller:0.34.0")
    implementation("com.slack.circuit:circuit-foundation:0.20.0")
    implementation("com.slack.circuit:circuit-overlay:0.20.0")
    implementation("com.slack.circuit:circuit-retained:0.20.0")
    implementation("com.slack.circuit:circuit-codegen-annotations:0.20.0")
    kapt("com.slack.circuit:circuit-codegen:0.20.0")
    implementation("io.coil-kt:coil-compose:2.6.0")
}