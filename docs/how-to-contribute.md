# How to Contribute

This document outlines how to contribute to this Android codebase. You must strictly follow the rules here before opening a pull request proposing a new change.

## Steps to propose a change

1. Create a new branch in the project (use a descriptive name on what you want to achieve)
2. Make your changes.
3. Smoke test your change. This may mean running the launcher app on a device or emulator, testing specific feature modules, or manually verifying functionality works as expected.
4. Ensure all tests pass. Add any new tests. Read and follow the [testing guidelines](testing.md).
5. Run lint checks and fix any code style issues using `./gradlew lint`.
6. Make sure all documentation is up to date. Add or update any new documentation as-needed. Read and follow [the documentation guidelines](docs.md).

## Contribution Rules

1. ALWAYS ensure your change is properly documented, if necessary. Documentation is not required for small changes, only medium-large or conceptually interesting changes. Keep documentation high-level yet specific, and do not document every variable and function change you make - if it is a small change, it doesn't always necessitate documentation. Read and follow [the documentation guidelines](docs.md).
2. ALWAYS ensure your change is properly tested. All tests must pass before submitting, and any new changes must be properly tested. Read and follow the [testing guidelines](testing.md).
3. ALWAYS work from within the confines of the technology stack. DO NOT add new technologies or use a different approach than what is described in the [Android technology stack document](technology-stack/android-technology-stack.md). If you absolutely feel that a new technology or approach would be beneficial, open a Github issue with your reasoning.

## Project Philosophy

1. The repository is organized by modules and sub-modules. Each module should contain a logical grouping of functionality. Ex. the [modules/features/settings/](../modules/features/settings/) module contains settings-related functionality. Each module should have a single high-level purpose. Create a new module in the relevant category whenever you find the current one is growing too big and/or the change you're making fits a sufficiently new purpose.
2. Keep files small and granular. Each file should have a single purpose. Create a new file in the relevant module whenever you find the current one is growing too big and/or the change you're making fits a sufficiently new purpose.
3. Keep functions/classes small and granular. Each function/class should have a single purpose. Create a new function or class in the relevant file whenever you find the current one is growing too big and/or the change you're making fits a sufficiently new purpose.
4. Code is self-documenting. Use KDoc comments only when absolutely necessary, ex. when there's a hidden gotcha in the code or complex business logic that needs explanation.

## Android-Specific Guidelines

1. Follow Android coding conventions and best practices.
2. Use dependency injection with Hilt for all modules.
3. Follow the established module structure: `features/` for UI-related modules, `libs/` for reusable utilities.
4. Use Jetpack Compose for all UI development.
5. Write unit tests for business logic and UI tests for user interactions.
6. Ensure proper permission handling for SMS and other sensitive features.

## Security Guidelines

- Never commit secrets, keys, or sensitive data
- Follow security best practices in all code
- Validate and sanitize user inputs
- Handle runtime permissions properly
- Use secure communication for any network operations