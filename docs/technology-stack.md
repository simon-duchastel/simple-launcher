# Android Technology Stack

This document outlines the technologies, frameworks, and libraries used in the Simple Launcher Android application.

## Core Technologies

### Language & Build System
- **Kotlin** - Primary programming language with full interop
- **Gradle** with Kotlin DSL - Build automation and dependency management
- **Android Gradle Plugin** - Android-specific build configuration

### Android Framework
- **Android SDK** - Target API level 34+, minimum API level 24+
- **Jetpack Compose** - Modern declarative UI toolkit
- **Material Design 3** - Design system and components

## Architecture & Dependencies

### Dependency Injection
- **Hilt** - Compile-time dependency injection built on Dagger

### UI
- **Compose** - Jetpack Compose for UI

### Architecture Components
- **Circuit** - Compose-first architecutre and navigation, [an open-source framework from Slack[(https://slackhq.github.io/circuit)

## Key Libraries

### UI & User Experience
- **Jetpack Compose** - Declarative UI framework
- **Material 3** - Material Design components

### Data & State Management
- **Kotlin Coroutines** - Asynchronous programming
- **Flow** - Reactive data streams
- **DataStore** - Modern data storage

### Testing
- **JUnit 5** - Unit testing framework
- **MockK** - Mocking library for Kotlin
- **Espresso** - Android UI testing
- **Robolectric** - Android unit testing

## Build Configuration

### Gradle Configuration
```kotlin
// Module-level build.gradle.kts structure
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
}

android {
    compileSdk = 34
    defaultConfig {
        minSdk = 24
        targetSdk = 34
    }
    
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    
    kotlinOptions {
        jvmTarget = "17"
    }
}
```

## Adding New Dependencies

1. **Check existing usage** - Ensure the dependency aligns with current stack
2. **Version catalogs** - Use `libs.versions.toml` for version management
3. **Module compatibility** - Verify the dependency works across all modules
4. **Security review** - Ensure dependencies are from trusted sources
5. **Documentation update** - Update this document when adding major dependencies

## Prohibited Technologies

To maintain consistency and avoid conflicts:

- **View System (XML layouts)** - Use Compose instead
- **RxJava** - Use Kotlin Coroutines and Flow
- **Multiple DI frameworks** - Stick with Hilt
- **Legacy AndroidX libraries** - Prefer modern Jetpack alternatives
