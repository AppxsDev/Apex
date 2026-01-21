package com.appxs.apex.presentation.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext

private val AlmostBlack = Color(0xFF1C1C1E)
private val AlmostWhite = Color(0xFFF5F5F5)

private val DarkColorScheme = darkColorScheme(
    primary = Black,
    onPrimary = White,
    primaryContainer = Black,
    onPrimaryContainer = White,
    secondary = Black,
    onSecondary = White,
    secondaryContainer = AlmostBlack,
    onSecondaryContainer = White,
    tertiary = Black,
    onTertiary = White,
    tertiaryContainer = Black,
    onTertiaryContainer = White,
    background = Black,
    onBackground = White,
    surface = Black,
    onSurface = White,
    surfaceVariant = AlmostBlack,
    onSurfaceVariant = White,
    error = Black,
    onError = White,
    errorContainer = Black,
    onErrorContainer = White,
    outline = White,
    inverseSurface = White,
    inverseOnSurface = Black
)

private val LightColorScheme = lightColorScheme(
    primary = White,
    onPrimary = Black,
    primaryContainer = White,
    onPrimaryContainer = Black,
    secondary = White,
    onSecondary = Black,
    secondaryContainer = AlmostWhite,
    onSecondaryContainer = Black,
    tertiary = White,
    onTertiary = Black,
    tertiaryContainer = White,
    onTertiaryContainer = Black,
    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    surfaceVariant = AlmostWhite,
    onSurfaceVariant = Black,
    error = White,
    onError = Black,
    errorContainer = White,
    onErrorContainer = Black,
    outline = Black,
    inverseSurface = Black,
    inverseOnSurface = White,
)

@Composable
fun ApexTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
