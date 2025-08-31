# Homepage Feature

The main launcher screen that users interact with as their home screen replacement.

## Purpose

This feature module provides the primary user interface for the launcher, displaying apps, handling user interactions, and managing the home screen experience.

## Key Components

- **Homepage UI** - Jetpack Compose screens and components
- **State Management** - ViewModel and state handling
- **Navigation** - Screen navigation and routing
- **Dependency Injection** - Feature-specific Hilt configuration

## Architecture

```
modules/features/homepage/
├── src/main/java/.../features/homepage/
│   ├── ui/
│   │   ├── HomepageScreen.kt          # Main homepage Compose screen
│   │   ├── HomepageViewModel.kt       # State and business logic
│   │   └── components/                # Reusable UI components
│   └── di/
│       └── HomepageModule.kt          # Feature DI configuration
└── build.gradle.kts                   # Module dependencies
```

## User Experience

The homepage provides:
- **App Grid/List** - Display of available applications
- **Search Functionality** - Quick app discovery
- **Recent Apps** - Quick access to frequently used apps
- **Customization** - Layout and theme options
- **Gesture Support** - Touch interactions and navigation

## State Management

Uses modern Android architecture:
- **ViewModel** - Manages UI state and business logic
- **StateFlow/LiveData** - Reactive state updates
- **Compose State** - UI state management

## Integration

This module integrates with:
- **App List Feature** - Consumes app data for display
- **Settings Feature** - Applies user preferences
- **Homepage Action Feature** - Handles interaction events
- **UI Library** - Uses shared components and theming

## Dependencies

- Jetpack Compose for UI
- Navigation Component
- Features: `app-list`, `settings`, `homepage-action`
- Libs: `ui`, `core-ext`

## Testing

Includes:
- **Unit Tests** - ViewModel and state logic
- **UI Tests** - Compose screen interactions
- **Integration Tests** - Feature module interactions