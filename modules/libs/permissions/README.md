# Permissions Library

Runtime permission management and utilities for the Simple Launcher application.

## Purpose

This library module provides a centralized system for handling Android runtime permissions, including permission requests, status checking, and user education.

## Key Components

- **Permissions Repository** - Core permission management interface
- **Permission States** - Type-safe permission status representation
- **Request Handlers** - Permission request flow management
- **Dependency Injection** - Hilt module configuration

## Architecture

```
modules/libs/permissions/
├── src/main/java/.../libs/permissions/
│   ├── data/
│   │   └── PermissionsRepository.kt   # Permission management interface
│   └── di/
│       └── PermissionsModule.kt       # Hilt DI configuration
└── build.gradle.kts                   # Module dependencies
```

## Permission Types

### SMS Permissions
- **SEND_SMS** - Required for SMS functionality
- **READ_SMS** - Optional for SMS history access
- **RECEIVE_SMS** - Optional for incoming SMS processing

### System Permissions
- **QUERY_ALL_PACKAGES** - App discovery and listing
- **REQUEST_DELETE_PACKAGES** - App uninstallation
- **SYSTEM_ALERT_WINDOW** - Overlay permissions

## Permission States

```kotlin
sealed class PermissionState {
    object Granted -> PermissionState()
    object Denied -> PermissionState()
    object PermanentlyDenied -> PermissionState()
    object NotRequested -> PermissionState()
}
```

## Key Features

### Permission Checking
- **Real-time Status** - Current permission status checking
- **Batch Checking** - Multiple permission status validation
- **Permission Groups** - Related permissions grouped together

### Permission Requests
- **Guided Requests** - Step-by-step permission request flow
- **User Education** - Explain why permissions are needed
- **Fallback Handling** - Graceful degradation when permissions denied

### Settings Integration
- **Settings Deep Links** - Direct navigation to permission settings
- **Permission History** - Track permission request attempts
- **User Preferences** - Remember user permission choices

## Usage Examples

### Check Permission Status
```kotlin
@Inject
lateinit var permissionsRepository: PermissionsRepository

// Check SMS permission
val smsPermission = permissionsRepository.checkPermission(
    context, 
    Manifest.permission.SEND_SMS
)

when (smsPermission) {
    is PermissionState.Granted -> enableSmsFeatures()
    is PermissionState.Denied -> showPermissionRationale()
    is PermissionState.PermanentlyDenied -> openPermissionSettings()
}
```

### Request Permissions
```kotlin
// Request SMS permissions
permissionsRepository.requestPermissions(
    activity,
    listOf(Manifest.permission.SEND_SMS),
    onGranted = { enableSmsFeatures() },
    onDenied = { showPermissionError() }
)
```

## Integration

This library is used by:
- **SMS Library** - SMS permission management
- **App List Feature** - Package query permissions
- **Settings Feature** - Permission status display and management

## Dependencies

- Android runtime permission APIs
- AndroidX Activity Result API
- Core Extensions library
- Hilt for dependency injection

## Best Practices

1. **Minimal Permissions** - Request only necessary permissions
2. **User Education** - Explain permission needs clearly
3. **Graceful Degradation** - App remains functional without optional permissions
4. **Respect User Choice** - Don't repeatedly ask for denied permissions
5. **Settings Navigation** - Provide easy access to permission settings

## Testing

Includes tests for:
- Permission status checking logic
- Permission request flow handling
- Edge cases and error scenarios