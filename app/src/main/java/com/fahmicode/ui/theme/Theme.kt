package com.fahmicode.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PureWhite,
    secondary = LightGray,
    background = PureBlack,
    surface = DarkGray,
    onPrimary = PureBlack,
    onSecondary = PureBlack,
    onBackground = PureWhite,
    onSurface = PureWhite,
    outline = DarkDivider,
    tertiary = PrimaryBlue,
    error = ExpenseColor
)

private val LightColorScheme = lightColorScheme(
    primary = PureBlack,
    secondary = DarkGray,
    background = LightGray,
    surface = PureWhite,
    onPrimary = PureWhite,
    onSecondary = PureWhite,
    onBackground = PureBlack,
    onSurface = PureBlack,
    outline = DividerGray,
    tertiary = PrimaryBlue,
    error = ExpenseColor
)

@Composable
fun BudgetPeckerTheme(
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
