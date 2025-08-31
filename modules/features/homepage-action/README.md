# Homepage Action Feature

Handles user interactions and actions performed on the homepage launcher screen.

## Purpose

This feature module processes user interactions like app launches, gestures, long-presses, and other actions that occur on the main homepage interface.

## Key Components

- **Action Handlers** - Processes different types of user interactions
- **Gesture Recognition** - Touch and gesture processing
- **Intent Management** - App launching and system intents
- **Dependency Injection** - Action-specific Hilt configuration

## Architecture

```
modules/features/homepage-action/
├── src/main/java/.../features/homepageaction/
│   ├── handlers/
│   │   ├── AppLaunchHandler.kt        # App launching logic
│   │   ├── GestureHandler.kt          # Gesture processing
│   │   └── LongPressHandler.kt        # Long-press interactions
│   ├── models/
│   │   └── UserAction.kt              # Action data models
│   └── di/
│       └── HomepageActionModule.kt    # Feature DI configuration
└── build.gradle.kts                   # Module dependencies
```

## Action Types

### App Launch Actions
- **Tap to Launch** - Standard app launching
- **App Info** - System app information screen
- **Uninstall** - App removal actions

### Gesture Actions
- **Swipe** - Navigation and scrolling
- **Long Press** - Context menus and actions
- **Pinch/Zoom** - UI scaling interactions

### System Actions
- **Settings Access** - Launcher settings screen
- **System UI** - Status bar and navigation interactions

## Integration

This module integrates with:
- **Homepage Feature** - Receives user interaction events
- **App List Feature** - Processes app-related actions
- **Settings Feature** - Handles settings actions
- **Intents Library** - Uses intent launching utilities

## Dependencies

- Android gesture detection APIs
- Features: `app-list`, `settings`
- Libs: `intents`, `permissions`
- Hilt dependency injection

## Usage Example

```kotlin
// Handle user action in homepage
@Inject
lateinit var actionProcessor: ActionProcessor

// Process app launch
actionProcessor.handleAppLaunch(app, context)

// Process gesture
actionProcessor.handleGesture(gestureType, coordinates)
```