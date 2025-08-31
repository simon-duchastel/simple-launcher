# Intents Library

Intent creation, launching, and management utilities for the Simple Launcher application.

## Purpose

This library module provides a centralized and type-safe way to handle Android intents, including app launching, system intents, and custom actions.

## Key Components

- **Intent Launcher** - Primary interface for launching intents
- **Intent Builder** - Type-safe intent creation utilities
- **System Intents** - Pre-configured system action intents
- **Dependency Injection** - Hilt module configuration

## Architecture

```
modules/libs/intents/
├── src/main/java/.../intents/
│   ├── IntentLauncher.kt              # Main intent launching interface
│   ├── IntentLauncherImpl.kt          # Intent launching implementation
│   └── IntentsModule.kt               # Hilt DI configuration
└── build.gradle.kts                   # Module dependencies
```

## Key Features

### App Launching
- **Safe App Launch** - Handles app not found exceptions
- **Launch Modes** - Single task, clear top, and custom launch modes
- **Package Validation** - Ensures target apps are installed

### System Intents
- **Settings Intents** - System settings, app info, permissions
- **Communication** - Phone, SMS, email intents
- **Browser** - Web page and URL handling

### Error Handling
- **Exception Handling** - Graceful handling of intent failures
- **Fallback Actions** - Alternative actions when primary intent fails
- **User Feedback** - Toast messages and error reporting

## Usage Examples

### App Launching
```kotlin
@Inject
lateinit var intentLauncher: IntentLauncher

// Launch an app
intentLauncher.launchApp(context, packageName)

// Launch app with custom action
intentLauncher.launchAppWithAction(context, packageName, action)
```

### System Intents
```kotlin
// Open app settings
intentLauncher.openAppSettings(context, packageName)

// Send SMS
intentLauncher.sendSms(context, phoneNumber, message)

// Open URL
intentLauncher.openUrl(context, url)
```

## Error Handling

```kotlin
// Intent launching with error handling
val result = intentLauncher.launchApp(context, packageName)
when (result) {
    is IntentResult.Success -> { /* Handle success */ }
    is IntentResult.AppNotFound -> { /* Show error message */ }
    is IntentResult.SecurityException -> { /* Handle permission issues */ }
}
```

## Integration

This library is used by:
- **Homepage Action Feature** - App launching and system actions
- **App List Feature** - Application launching logic
- **Settings Feature** - System settings access

## Dependencies

- Android Intent and PackageManager APIs
- Core Extensions library for utilities
- Hilt for dependency injection

## Testing

Includes comprehensive tests for:
- Intent creation and validation
- Error handling scenarios
- Integration with Android framework

## Security Considerations

- **Package Validation** - Prevents launching malicious or non-existent apps
- **Permission Checks** - Validates required permissions before launching
- **Intent Sanitization** - Cleans and validates intent data