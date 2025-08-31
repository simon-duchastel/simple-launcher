# App List Feature

Module responsible for discovering, listing, and managing installed applications on the device.

## Purpose

This feature module handles the core launcher functionality of displaying available apps and managing user interactions with app icons and listings.

## Key Components

- **App Data Model** - Represents installed applications with metadata
- **App Discovery** - Queries system for installed applications  
- **App Launching** - Handles intent creation and app launching
- **Dependency Injection** - Module-specific Hilt configuration

## Architecture

```
modules/features/app-list/
├── src/main/java/.../features/applist/
│   ├── data/
│   │   └── App.kt                     # App data model
│   └── di/
│       └── AppListModule.kt           # Feature DI configuration
└── build.gradle.kts                   # Module dependencies
```

## Data Models

### App
Represents an installed application with:
- Package name and app name
- Icon and launch intent
- Installation metadata
- Permission information

## Integration

This module integrates with:
- **Homepage Feature** - Provides app data for home screen display
- **Intents Library** - Uses intent launching utilities
- **Permissions Library** - Handles app permission queries

## Dependencies

- Android PackageManager APIs
- Libs: `intents`, `permissions`
- Hilt dependency injection

## Usage Example

```kotlin
// Injected via Hilt in consuming features
@Inject
lateinit var appRepository: AppRepository

// Get list of installed apps
val apps = appRepository.getInstalledApps()
```