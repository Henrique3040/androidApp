package com.example.cafefinder.view.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration



/**
 * Represents the window size in terms of width and height, categorized into predefined types (Compact, Medium, Expanded).
 * Used to adjust UI layouts based on screen dimensions.
 */
data class WindowSize(
    val width: WindowType,
    val height: WindowType,

    )

/**
 * Enum class to define different window size types.
 * Helps in categorizing screen dimensions into Compact, Medium, or Expanded for responsive UI design.
 */
enum class WindowType {
    Compact,
    Medium,
    Expanded
}

/**
 * A Composable function that calculates and remembers the current window size based on the screen dimensions.
 *
 * @return [WindowSize] object with width and height categorized as [WindowType].
 */
@Composable
fun rememberWindowSize(): WindowSize {
    val configuration = LocalConfiguration.current

    return WindowSize(
        width = when{
            configuration.screenWidthDp < 600 -> WindowType.Compact
            configuration.screenWidthDp < 840 -> WindowType.Medium
            else -> WindowType.Expanded
        },

        height = when{
            configuration.screenHeightDp < 600 -> WindowType.Compact
            configuration.screenHeightDp < 840 -> WindowType.Medium
            else -> WindowType.Expanded
        }
    )
}