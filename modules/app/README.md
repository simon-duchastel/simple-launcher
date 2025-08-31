# App Module

The main application module that serves as the entry point for the Simple Launcher Android application.

## Purpose

This module contains the application class, main activity, and dependency injection configuration that ties together all feature and library modules.

## Key Components

- **SimpleLauncherApplication** - Application class with Hilt dependency injection setup
- **MainActivity** - Primary activity that handles launcher functionality
- **Dependency Injection** - Hilt modules and component configuration

## Architecture

```
modules/app/
├── src/main/java/com/duchastel/simon/simplelauncher/
│   ├── SimpleLauncherApplication.kt    # Application entry point
│   ├── MainActivity.kt                 # Main launcher activity
│   └── di/
│       └── AppModule.kt               # Application-level DI configuration
└── src/main/
    ├── AndroidManifest.xml            # App permissions and launcher configuration
    └── res/                           # App resources and assets
```

## Launcher Configuration

This app acts as an Android home screen launcher with the following intent filters:

```xml
<intent-filter>
    <action android:name="android.intent.action.MAIN" />
    <category android:name="android.intent.category.HOME" />
    <category android:name="android.intent.category.DEFAULT" />
    <category android:name="android.intent.category.LAUNCHER" />
</intent-filter>
```

## Permissions

- `android.permission.SEND_SMS` - Required for SMS functionality
- Optional telephony hardware requirement for SMS features

## Dependencies

This module depends on:
- All feature modules (`features/*`)
- All library modules (`libs/*`)
- Hilt for dependency injection
- Android core libraries

## Getting Started

You can build the app from this module:

```bash
./gradlew :modules:app:assembleDebug
./gradlew installDebug
```
