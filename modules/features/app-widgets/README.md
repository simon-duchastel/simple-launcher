# App Widgets Feature

Android App Widget hosting functionality.

## Purpose

This feature module provides the ability to host and display Android app widgets within the launcher,
enabling users to add interactive widgets from other applications to their home screen.

## Key Components

- **Widget Host Infrastructure** - AppWidgetHost and AppWidgetHostView management
- **Widget Repository** - Widget operations and state management  
- **Compose Integration** - Bridge between Android Views and Jetpack Compose
- **Widget Selection** - User interface for choosing widgets from settings

## Architecture

```
modules/features/app-widgets/
├── src/main/java/.../features/appwidgets/
│   ├── ui/
│   │   ├── widget/
│   │   │   ├── AppWidgetScreen.kt        # Circuit screen for widget display
│   │   │   ├── AppWidgetPresenter.kt     # Business logic & state management  
│   │   │   ├── AppWidgetState.kt         # UI state definition
│   │   │   └── AppWidgetUi.kt            # Composable widget UI
│   │   ├── selection/
│   │   │   ├── WidgetSelectionScreen.kt  # Circuit screen for widget selection
│   │   │   ├── WidgetSelectionPresenter.kt # Selection business logic
│   │   │   ├── WidgetSelectionState.kt   # Selection UI state
│   │   │   └── WidgetSelectionUi.kt      # Widget selection UI
│   │   └── compose/
│   │       └── AppWidgetHostCompose.kt   # Compose wrapper for AppWidgetHostView
│   ├── data/
│   │   ├── WidgetData.kt                 # Serializable widget data models
│   │   ├── AppWidgetRepository.kt        # Widget operations interface
│   │   └── AppWidgetRepositoryImpl.kt    # Widget management implementation
│   ├── host/
│   │   ├── LauncherAppWidgetHost.kt      # Custom AppWidgetHost implementation
│   │   └── LauncherAppWidgetHostView.kt  # Custom AppWidgetHostView
│   └── di/
│       └── AppWidgetsModule.kt           # Hilt dependency injection
├── src/test/                             # Unit tests
├── src/androidTest/                      # Instrumented tests
├── build.gradle.kts                      # Module configuration
└── README.md                             # This documentation
```

## Key Features

### Widget Hosting
- **AppWidgetHost Management** - Custom host with launcher-specific functionality
- **Widget Lifecycle** - Proper allocation, binding, and cleanup
- **Size Management** - Dynamic widget resizing and options
- **Error Handling** - Graceful fallbacks for widget failures

### Compose Integration
- **AndroidView Bridge** - Seamless integration with Compose UI
- **State Management** - Reactive widget state updates
- **Lifecycle Awareness** - Proper composition lifecycle handling

### Widget Operations
- **Widget Discovery** - List available widget providers
- **Widget Binding** - Bind widgets with proper permissions
- **Configuration** - Handle widget configuration activities
- **Removal** - Clean widget removal and resource cleanup

### Widget Selection
- **Available Widgets** - Browse all installable widgets on device
- **Selection Interface** - User-friendly widget picker UI
- **Settings Integration** - Seamless connection to launcher settings
- **Preview Support** - Display widget previews and information

## Data Models

### WidgetData
Core widget information for serialization:
```kotlin
data class WidgetData(
    val widgetId: Int,
    val providerComponentName: String,
    val width: Int,
    val height: Int,
    val label: String? = null
)
```

### WidgetProviderInfo
Widget provider metadata:
```kotlin
data class WidgetProviderInfo(
    val componentName: String,
    val label: String,
    val previewImage: String? = null,
    val minWidth: Int,
    val minHeight: Int,
    val maxWidth: Int? = null,
    val maxHeight: Int? = null,
    val resizeMode: Int = 0,
    val hasConfigurationActivity: Boolean = false
)
```

## Usage Examples

### Display Widget Screen
```kotlin
// Navigate to widget display
navigator.goTo(AppWidgetScreen(widgetData))
```

### Widget Selection Screen
```kotlin
// Navigate to widget selection from settings
navigator.goTo(WidgetSelectionScreen)
```

### Use Widget Repository
```kotlin
@Inject
lateinit var appWidgetRepository: AppWidgetRepository

// Get available widgets
val availableWidgets = appWidgetRepository.getAvailableWidgets()

// Bind a widget
val widgetId = appWidgetRepository.allocateWidgetId()
appWidgetRepository.bindWidget(widgetId, providerInfo)
```

### Compose Integration
```kotlin
AppWidgetHostCompose(
    widgetData = widgetData,
    appWidgetHost = appWidgetHost,
    onError = { error -> /* handle error */ }
)
```

## Integration Points

This module integrates with:
- **Homepage Feature** - Display widgets on launcher home screen
- **Settings Feature** - Widget configuration and management
- **Permissions Library** - Handle BIND_APPWIDGET permissions
- **UI Library** - Consistent theming and styling

## Permission Requirements

Adds the `BIND_APPWIDGET` permission.
```xml
<uses-permission android:name="android.permission.BIND_APPWIDGET" />
```