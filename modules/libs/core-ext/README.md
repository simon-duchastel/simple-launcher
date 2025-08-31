# Core Extensions Library

Core Android framework extensions and utilities used across all modules in the Simple Launcher application.

## Purpose

This library module provides common Android extensions, utilities, and abstractions that are shared across feature modules to reduce code duplication and improve consistency.

## Key Components

- **Android Extensions** - Extension functions for Android framework classes
- **Utility Classes** - Common helper classes and functions
- **Type Aliases** - Convenient type definitions
- **Constants** - Shared application constants

## Architecture

```
modules/libs/core-ext/
├── src/main/java/.../libs/core/
│   ├── extensions/
│   │   ├── ContextExt.kt              # Context extension functions
│   │   ├── ViewExt.kt                 # View-related extensions
│   │   └── CollectionExt.kt           # Collection utilities
│   ├── utils/
│   │   ├── Logger.kt                  # Logging utilities
│   │   └── DateUtils.kt               # Date/time helpers
│   └── Constants.kt                   # Application constants
└── build.gradle.kts                   # Minimal dependencies
```

## Extension Categories

### Context Extensions
- **Resource Access** - Simplified resource retrieval
- **System Services** - Easy access to system services
- **Intent Creation** - Intent building helpers

### Collection Extensions
- **Safe Operations** - Null-safe collection operations
- **Filtering** - Common filtering operations
- **Transformations** - Data transformation utilities

### UI Extensions
- **View Helpers** - View visibility and state management
- **Dimension Utilities** - DP/PX conversions and calculations
- **Color Operations** - Color manipulation and theming

## Usage Examples

### Context Extensions
```kotlin
// Get system service with type safety
val packageManager = context.packageManager<PackageManager>()

// Resource access
val appName = context.stringRes(R.string.app_name)
```

### Collection Extensions
```kotlin
// Safe operations
val firstApp = apps.firstOrNull { it.isLauncher }

// Filtering helpers
val userApps = apps.filterUserApps()
```

## Integration

This library is used by:
- All feature modules for common utilities
- Other library modules for shared functionality
- App module for application-wide constants

## Dependencies

Minimal dependencies for maximum reusability:
- Android Core KTX
- Kotlin standard library
- No external dependencies to avoid conflicts

## Design Principles

1. **No Business Logic** - Pure utility functions only
2. **Minimal Dependencies** - Avoid heavy external libraries
3. **Extension Functions** - Prefer extensions over utility classes
4. **Type Safety** - Leverage Kotlin's type system
5. **Performance** - Optimized for frequent usage