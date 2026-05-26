package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = PrimarySleek,
    onPrimary = OnPrimarySleek,
    primaryContainer = PrimaryContainerSleek,
    onPrimaryContainer = OnPrimaryContainerSleek,
    secondary = SecondarySleek,
    onSecondary = OnSecondarySleek,
    secondaryContainer = SecondaryContainerSleek,
    onSecondaryContainer = OnSecondaryContainerSleek,
    background = BackgroundSleek,
    onBackground = Slate900,
    surface = SurfaceSleek,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate700,
    error = ErrorSleek,
    onError = OnErrorSleek
  )

private val LightColorScheme =
  lightColorScheme(
    primary = PrimarySleek,
    onPrimary = OnPrimarySleek,
    primaryContainer = PrimaryContainerSleek,
    onPrimaryContainer = OnPrimaryContainerSleek,
    secondary = SecondarySleek,
    onSecondary = OnSecondarySleek,
    secondaryContainer = SecondaryContainerSleek,
    onSecondaryContainer = OnSecondaryContainerSleek,
    background = BackgroundSleek,
    onBackground = Slate900,
    surface = SurfaceSleek,
    onSurface = Slate900,
    surfaceVariant = Slate100,
    onSurfaceVariant = Slate700,
    error = ErrorSleek,
    onError = OnErrorSleek
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme =
    when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
        val context = LocalContext.current
        if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> DarkColorScheme
      else -> LightColorScheme
    }

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
