# Testing

This document outlines testing practices and requirements for the Simple Launcher Android project.

## Testing Philosophy

- **Unit Tests**: Test business logic and utility functions in isolation
- **Integration Tests**: Test module interactions and data flow
- **UI Tests**: Test user interactions and screen behavior with Compose UI
- **Instrumented Tests**: Test Android-specific functionality requiring device context

## Test Structure

```
modules/
└── [module-name]/
    └── src/
        ├── test/                    # Unit tests (JVM)
        │   └── java/.../*Test.kt
        └── androidTest/             # Instrumented tests (Android device/emulator)
            └── java/.../*Test.kt
```

## Running Tests

### All Tests
```bash
./gradlew test                      # Run all unit tests
./gradlew connectedAndroidTest      # Run all instrumented tests on connected device
```

### Module-Specific Tests
```bash
./gradlew :modules:features:settings:test
./gradlew :modules:libs:sms:connectedAndroidTest
```

### Test Coverage
```bash
./gradlew testDebugUnitTestCoverage  # Generate coverage report
```

## Testing Guidelines

### Unit Tests
- Test business logic, data transformations, and utilities
- Mock Android dependencies using mockito
- Use JUnit 5 testing framework
- Name tests descriptively: `should_returnExpectedResult_when_givenValidInput()`

### Compose UI Tests  
- Use `@get:Rule val composeTestRule = createComposeRule()`
- Test user interactions, state changes, and UI behavior
- Use semantic testing over implementation details
- Example:
```kotlin
@Test
fun settings_screen_displays_correctly() {
    composeTestRule.setContent {
        SettingsScreen()
    }
    
    composeTestRule
        .onNodeWithText("Settings")
        .assertIsDisplayed()
}
```

### Instrumented Tests
- Test Android framework interactions (SMS, permissions, intents)
- Use real Android context and system services
- Test launcher functionality and home screen behavior

### Test Requirements

1. **All new features MUST have tests**
2. **All bug fixes MUST include regression tests**
3. **Minimum 80% code coverage** for business logic
4. **All tests MUST pass** before merging

## Test Dependencies

Testing libraries used in this project:
- **JUnit 5** - Testing framework
- **Mockito** - Mocking library for Kotlin
- **Compose Test** - UI testing for Jetpack Compose
- **Espresso** - Android UI testing
- **Robolectric** - Android unit testing framework
- **Hilt Test** - Testing with dependency injection

## Writing Good Tests

1. **Arrange-Act-Assert**: Structure tests clearly
2. **One assertion per test**: Keep tests focused
3. **Descriptive test names**: Make intent clear
4. **Mock external dependencies**: Keep tests isolated
5. **Test edge cases**: Include boundary conditions and error scenarios
6. **Fast execution**: Unit tests should run quickly
