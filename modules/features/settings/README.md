# Settings Feature

Configuration and preferences management for the Simple Launcher application.

## Purpose

This feature module provides user-facing settings screens and manages application configuration, preferences, and customization options.

## Key Components

- **Settings UI** - Jetpack Compose settings screens and components
- **Preferences Management** - Data storage and retrieval
- **Settings Repository** - Data layer for preferences
- **State Management** - Settings state and validation

## Architecture

```
modules/features/settings/
├── src/main/java/.../features/settings/
│   ├── ui/
│   │   ├── settings/
│   │   │   ├── SettingsScreen.kt      # Main settings screen
│   │   │   ├── SettingsPresenter.kt   # Business logic
│   │   │   └── SettingsState.kt       # UI state management
│   │   └── modifysetting/
│   │       └── ModifySettingsScreen.kt # Individual setting modification
│   ├── data/
│   │   ├── Setting.kt                 # Setting data model
│   │   ├── SettingData.kt            # Setting value container
│   │   ├── SettingsRepository.kt      # Settings data interface
│   │   └── SettingsRepositoryImpl.kt  # Settings implementation
│   └── SettingsActivity.kt            # Standalone settings activity
└── src/test/                          # Unit and integration tests
```

## Settings Categories

### Appearance
- **Theme Selection** - Light/dark/auto theme options
- **App Grid Layout** - Grid size and arrangement
- **Icon Customization** - Size and style options

### Behavior
- **App Launch Settings** - Launch behavior and animations
- **Gesture Configuration** - Custom gesture actions
- **Search Settings** - Search behavior and indexing

### Privacy & Permissions
- **SMS Permissions** - SMS functionality controls
- **App Visibility** - Show/hide specific applications
- **Data Usage** - Analytics and telemetry preferences

## Data Models

### Setting
Core setting configuration with:
- Setting key and display name
- Value type and validation rules
- Default values and constraints

### SettingData
Container for setting values with:
- Type-safe value storage
- Validation and conversion
- Change tracking

## Integration

This module integrates with:
- **Homepage Feature** - Applies visual and behavioral settings
- **App List Feature** - Filters and sorting preferences  
- **SMS Library** - SMS-related configuration
- **UI Library** - Theme and styling settings

## Dependencies

- **DataStore** - Modern preferences storage
- **Jetpack Compose** - Settings UI
- Libs: `ui`, `core-ext`
- Hilt dependency injection

## Testing

Comprehensive test coverage:
- **SettingsRepositoryImplTest** - Data layer testing
- **SettingsPresenterTest** - Business logic validation
- **UI Tests** - Settings screen interactions

## Usage Example

```kotlin
// Access settings in other modules
@Inject
lateinit var settingsRepository: SettingsRepository

// Get theme setting
val theme = settingsRepository.getTheme()
```