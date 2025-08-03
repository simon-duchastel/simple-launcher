# App-List Feature Module

This feature module is responsible for displaying a list of installed applications on the device.

## Architecture

This module follows the **Circuit** architecture pattern for the presentation layer.

-   **`AppListScreen`**: The main Composable for this feature.
-   **`AppListPresenter`**: The presenter responsible for fetching the list of apps and handling UI events.
-   **`AppListUiState`**: Represents the state of the UI.
-   **`AppRepository`**: The repository responsible for fetching the list of installed applications from the `PackageManager`.

## AI Instructions

This file provides context for AI assistants. `GEMINI.md` and `CLAUDE.md` are symlinked to this file.
