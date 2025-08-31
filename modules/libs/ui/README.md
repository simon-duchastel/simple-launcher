# UI Library

Shared UI components, theming, and design system for the Simple Launcher application.

## Purpose

This library module provides a consistent design system, reusable UI components, and theming infrastructure used across all feature modules to ensure visual consistency and maintainability.

## Key Components

- **Design System** - Colors, typography, and spacing definitions
- **Theme Configuration** - Light/dark theme support
- **Shared Components** - Reusable Jetpack Compose components
- **UI Utilities** - Common UI helper functions

## Architecture

```
modules/libs/ui/
├── src/main/java/.../libs/ui/
│   ├── theme/
│   │   ├── Color.kt                   # Color palette definitions
│   │   ├── Theme.kt                   # App theme configuration
│   │   └── Type.kt                    # Typography system
│   └── components/
│       └── SettingsButton.kt          # Shared UI components
└── build.gradle.kts                   # UI dependencies
```

## Design System

### Color Palette
- **Primary Colors** - Brand identity colors
- **Secondary Colors** - Accent and supporting colors
- **Surface Colors** - Backgrounds and containers
- **Content Colors** - Text and icon colors
- **System Colors** - Error, warning, success states

### Typography
- **Text Styles** - Heading, body, caption text definitions
- **Font Weights** - Regular, medium, bold variations
- **Font Sizes** - Consistent sizing scale
- **Line Heights** - Optimal readability spacing

### Spacing & Layout
- **Spacing Scale** - Consistent margin and padding values
- **Grid System** - Layout structure and alignment
- **Component Sizing** - Standard component dimensions

## Theme Support

### Light Theme
- High contrast colors for daylight usage
- Material Design 3 light color scheme
- Optimized for battery efficiency

### Dark Theme
- OLED-friendly dark colors
- Reduced eye strain in low light
- System dark mode integration

### Dynamic Theming
- **System Theme Following** - Automatically follows system preference
- **User Override** - Manual theme selection in settings
- **Adaptive Colors** - Material You color extraction (Android 12+)

## Shared Components

### SettingsButton
Standardized button component used in settings screens:
- Consistent styling and behavior
- Built-in accessibility features
- Theme-aware color adaptation

### Future Components
Planned shared components:
- App icons and badges
- Search bars and input fields
- Navigation components
- Loading and progress indicators

## Usage Examples

### Theme Application
```kotlin
@Composable
fun MyScreen() {
    SimpleLauncherTheme {
        // Your UI content here
        Surface(color = MaterialTheme.colorScheme.background) {
            // Components automatically use theme colors
        }
    }
}
```

### Shared Components
```kotlin
@Composable
fun SettingsItem() {
    SettingsButton(
        text = "Theme Settings",
        onClick = { navigateToThemeSettings() }
    )
}
```

### Color Usage
```kotlin
// Access theme colors
Text(
    text = "Welcome",
    color = MaterialTheme.colorScheme.onPrimary,
    style = MaterialTheme.typography.headlineLarge
)
```

## Integration

This library is used by:
- **All Feature Modules** - Consistent UI theming and components
- **Settings Feature** - Theme configuration and preview
- **Homepage Feature** - App launcher UI components

## Dependencies

- **Jetpack Compose** - Modern UI toolkit
- **Material Design 3** - Component library and theming
- **Compose Foundation** - Basic UI building blocks
- **Compose UI** - Core Compose functionality

## Accessibility

- **Content Descriptions** - Screen reader support
- **Touch Target Sizes** - Minimum 48dp touch targets  
- **Color Contrast** - WCAG AA compliance
- **Dynamic Type** - Respect system font size preferences
- **High Contrast** - Support for accessibility settings

## Testing

UI testing includes:
- **Component Tests** - Individual component behavior
- **Theme Tests** - Color and typography validation
- **Accessibility Tests** - Screen reader and navigation testing
- **Visual Regression Tests** - Prevent unintended visual changes

## Design Principles

1. **Consistency** - Unified visual language across the app
2. **Accessibility** - Inclusive design for all users
3. **Performance** - Efficient rendering and minimal recomposition
4. **Modularity** - Reusable and composable components
5. **Material Design** - Following Google's design guidelines