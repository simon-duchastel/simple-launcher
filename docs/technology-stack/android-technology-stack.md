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

### Architecture Components
- **ViewModel** - UI-related data holder with lifecycle awareness
- **LiveData/StateFlow** - Observable data holders
- **Navigation Component** - In-app navigation framework

### Module Structure
- **Multi-module architecture** - Feature and library modules for scalability
- **Clean Architecture** - Separation of concerns across layers

## Module Categories

### Features (`modules/features/`)
- **UI-focused modules** with Compose screens and business logic
- Each feature is self-contained with its own navigation and state management

### Libraries (`modules/libs/`)
- **Reusable utilities** shared across feature modules
- Android framework abstractions and extensions

### App (`modules/app/`)
- **Application entry point** with dependency injection configuration
- Main activity and application class

## Key Libraries

### UI & User Experience
- **Jetpack Compose** - Declarative UI framework
- **Material 3** - Material Design components
- **Compose Navigation** - Screen navigation management

### Data & State Management
- **Kotlin Coroutines** - Asynchronous programming
- **Flow** - Reactive data streams
- **DataStore** - Modern data storage (replacing SharedPreferences)

### Testing
- **JUnit 5** - Unit testing framework
- **MockK** - Mocking library for Kotlin
- **Compose Test** - UI testing for Compose
- **Espresso** - Android UI testing
- **Robolectric** - Android unit testing

### Android Specific
- **SMS Manager** - SMS functionality integration
- **Permission handling** - Runtime permission management
- **Intent handling** - App launching and system integration

## Code Style & Quality

### Conventions
- **Kotlin Coding Conventions** - Official Kotlin style guide
- **Android Kotlin Style Guide** - Google's Android-specific guidelines

### Static Analysis
- **Android Lint** - Built-in code analysis and optimization suggestions
- **Detekt** - Kotlin static code analysis (if configured)

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
- **Retrofit without proper integration** - Coordinate with existing network layer
- **Multiple DI frameworks** - Stick with Hilt
- **Legacy AndroidX libraries** - Prefer modern Jetpack alternatives