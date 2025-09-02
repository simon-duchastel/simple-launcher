# Simple Launcher

![Cover image](images/cover-image.png)

Finally, a simple Android launcher.

**MANDATORY READING** - you MUST read [**How to Contribute**](docs/how-to-contribute.md) before continuing.

## Project Structure

```
simple-launcher/
│── docs/                          # Comprehensive project documentation
└── modules/
    ├── app/                       # Main application module
    ├── features/                  # Feature-specific modules
    │   ├── app-list/              # Application listing functionality
    │   ├── homepage/              # Main launcher screen
    │   ├── homepage-action/       # Homepage interaction handlers  
    │   └── settings/              # Configuration and preferences
    └── libs/                      # Shared library modules
        ├── contacts/              # Contact selection and data retrieval
        ├── core-ext/              # Core Android extensions
        ├── emoji/                 # Emoji utilities
        ├── intents/               # Intent handling utilities
        ├── permissions/           # Runtime permissions management
        ├── phone-number/          # Phone number validation utilities
        ├── sms/                   # SMS functionality integration
        └── ui/                    # Shared UI components and theming
```

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

[This project is licensed under the MIT license](LICENSE)
