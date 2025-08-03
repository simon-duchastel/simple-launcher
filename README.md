# simple-launcher
Finally, a simple Android launcher

# How to Add Composable Previews

This document provides instructions on how to add Jetpack Compose previews to your composables.

## General Rules

- **One Preview Per File**: Each composable file should have only one preview function.
- **Use `PreviewParameterProvider` for Multiple States**: If your composable has multiple states you want to preview, create a `PreviewParameterProvider` to supply the different states to a single preview function.
- **Internal Previews**: All preview functions should be marked as `internal`.
- **Cover Reasonable States**: Previews should cover the most common and important states of a composable. For example, a list should have a preview for both a populated state and an empty state.

## Steps to Add a Preview

1.  **Create a Preview Composable**:
    -   Create a new `internal` composable function.
    -   Annotate it with `@Composable` and `@Preview`.
    -   The function should call your actual composable, providing it with the necessary state.

2.  **Add Dependencies**:
    -   Ensure that your module's `build.gradle.kts` file has the following dependencies:
        ```kotlin
        implementation(libs.androidx.compose.ui.tooling.preview)
        debugImplementation(libs.androidx.compose.ui.tooling)
        ```

3.  **Provide State for the Preview**:
    -   For simple composables, you can create the state directly in the preview function.
    -   For composables with complex state, or if you want to preview multiple states, create a `PreviewParameterProvider`.

## Example: Simple Preview

```kotlin
@Preview(showBackground = true)
@Composable
internal fun MySimpleComponentPreview() {
    MySimpleComponent(text = "Hello Preview")
}
```

## Example: Preview with `PreviewParameterProvider`

```kotlin
// Your composable state
data class MyState(val items: List<String>)

// Provider for the state
internal class MyStateProvider : PreviewParameterProvider<MyState> {
    override val values: Sequence<MyState>
        get() = sequenceOf(
            MyState(items = listOf("Item 1", "Item 2")),
            MyState(items = emptyList())
        )
}

// The preview composable
@Preview(showBackground = true)
@Composable
internal fun MyComponentPreview(
    @PreviewParameter(MyStateProvider::class) state: MyState
) {
    MyComponent(state = state)
}
```
