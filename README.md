# Simple Launcher

![Cover image](images/cover-image.png)

Finally, a simple Android launcher.

**MANDATORY READING** - you MUST read [**How to Contribute**](docs/how-to-contribute.md) before continuing.

## Project Structure

```
simple-launcher/
├── modules/
│   ├── app/                        # Main application module
│   ├── features/                   # Feature-specific modules
│   │   ├── app-list/              # Application listing functionality
│   │   ├── homepage/              # Main launcher screen
│   │   ├── homepage-action/       # Homepage interaction handlers  
│   │   └── settings/              # Configuration and preferences
│   └── libs/                       # Shared library modules
│       ├── core-ext/              # Core Android extensions
│       ├── intents/               # Intent handling utilities
│       ├── permissions/           # Runtime permissions management
│       ├── sms/                   # SMS functionality integration
│       └── ui/                    # Shared UI components and theming
└── docs/                           # Comprehensive project documentation
```

## Features

- **Home Screen Replacement**: Acts as Android launcher with `android.intent.category.HOME`
- **SMS Integration**: Built-in SMS functionality with permissions management
- **Modular Architecture**: Clean separation of concerns across feature modules
- **Modern Android**: Kotlin-based with Jetpack Compose UI
- **Dependency Injection**: Hilt-powered dependency management

## Getting Started

### Prerequisites
- Android Studio Hedgehog | 2023.1.1 or later
- JDK 17+
- Android SDK with minimum API level 24+
- Kotlin 1.9+

### Quick Start
```bash
git clone <repository-url>
cd simple-launcher
./gradlew assembleDebug
```

Install on device:
```bash
./gradlew installDebug
```

### Build Variants
```bash
# Debug build
./gradlew assembleDebug

# Release build  
./gradlew assembleRelease

# Run tests
./gradlew test
```

## Technology Stack

- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern UI toolkit
- **Hilt** - Dependency injection
- **Gradle** with Kotlin DSL - Build system
- **Android Gradle Plugin** - Android build tooling

For detailed technology information, see [docs/technology-stack/](docs/technology-stack/).

## Architecture

This project follows a multi-module clean architecture pattern:

- **app/**: Application entry point and dependency injection setup
- **features/**: Self-contained feature modules with UI and business logic
- **libs/**: Reusable library modules for common functionality

Each module contains its own README.md with specific implementation details.

## Contributing

See [docs/how-to-contribute.md](docs/how-to-contribute.md) for detailed contribution guidelines.

## Testing

Run the test suite:
```bash
./gradlew test
./gradlew connectedAndroidTest
```

See [docs/testing.md](docs/testing.md) for testing guidelines.

## License

[View LICENSE file](LICENSE)
