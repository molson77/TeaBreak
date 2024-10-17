/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.teabreak.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.teabreak.data.TeaType
import com.example.teabreak.data.Utils

private val LightColorScheme = lightColorScheme(
    primary = tb_theme_light_primary,
    onPrimary = tb_theme_light_headline_color,
    primaryContainer = tb_theme_light_primary_container,
    background = tb_theme_light_background,
    secondary = tb_theme_light_secondary,
    secondaryContainer = tb_theme_light_secondary_container,
    tertiaryContainer = tb_theme_light_tertiary_container
)

private val DarkColorScheme = darkColorScheme(
    primary = tb_theme_dark_primary,
    background = tb_theme_dark_background,
    secondary = tb_theme_dark_secondary,
    secondaryContainer = tb_theme_dark_secondary_container,
    tertiaryContainer = tb_theme_dark_tertiary_container
)

private val GreenTeaColorScheme = lightColorScheme(
    primary = green_light_primary,
    onPrimary = green_light_on_primary,
    secondary = green_light_secondary,
    onSecondary = green_light_on_secondary
)

private val BlackTeaColorScheme = lightColorScheme(
    primary = black_light_primary,
    onPrimary = black_light_on_primary,
    secondary = black_light_secondary,
    onSecondary = black_light_on_secondary
)

private val OolongTeaColorScheme = lightColorScheme(
    primary = oolong_light_primary,
    onPrimary = oolong_light_on_primary,
    secondary = oolong_light_secondary,
    onSecondary = oolong_light_on_secondary
)

private val WhiteTeaColorScheme = lightColorScheme(
    primary = white_light_primary,
    onPrimary = white_light_on_primary,
    secondary = white_light_secondary,
    onSecondary = white_light_on_secondary
)

private val PuerhTeaColorScheme = lightColorScheme(
    primary = puerh_light_primary,
    onPrimary = puerh_light_on_primary,
    secondary = puerh_light_secondary,
    onSecondary = puerh_light_on_secondary
)

private val PurpleTeaColorScheme = lightColorScheme(
    primary = purple_light_primary,
    onPrimary = purple_light_on_primary,
    secondary = purple_light_secondary,
    onSecondary = purple_light_on_secondary
)

private val RooibosTeaColorScheme = lightColorScheme(
    primary = rooibos_light_primary,
    onPrimary = rooibos_light_on_primary,
    secondary = rooibos_light_secondary,
    onSecondary = rooibos_light_on_secondary
)

private val MateTeaColorScheme = lightColorScheme(
    primary = mate_light_primary,
    onPrimary = mate_light_on_primary,
    secondary = mate_light_secondary,
    onSecondary = mate_light_on_secondary
)

private val HerbalTeaColorScheme = lightColorScheme(
    primary = herbal_light_primary,
    onPrimary = herbal_light_on_primary,
    secondary = herbal_light_secondary,
    onSecondary = herbal_light_on_secondary
)

@Composable
fun TeaBreakTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme
    /*val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }*/
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            window.navigationBarColor = Color.Black.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                true //if (darkTheme) false else true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        shapes = Shapes,
        typography = Typography,
        content = content
    )
}

@Composable
fun TeaTimerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    teaType: TeaType,
    content: @Composable () -> Unit
) {

    val teaColorScheme = when(teaType) {
        TeaType.GREEN -> GreenTeaColorScheme
        TeaType.BLACK -> BlackTeaColorScheme
        TeaType.OOLONG -> OolongTeaColorScheme
        TeaType.WHITE -> WhiteTeaColorScheme
        TeaType.PU_ERH -> PuerhTeaColorScheme
        TeaType.PURPLE -> PurpleTeaColorScheme
        TeaType.ROOIBOS -> RooibosTeaColorScheme
        TeaType.MATE -> MateTeaColorScheme
        TeaType.HERBAL -> HerbalTeaColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = teaColorScheme.primary.toArgb()
            window.navigationBarColor = teaColorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme (
        colorScheme = teaColorScheme,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun TeaTheme(
    teaType: TeaType,
    content: @Composable () -> Unit
) {
    val teaColorScheme = when(teaType) {
        TeaType.GREEN -> GreenTeaColorScheme
        TeaType.BLACK -> BlackTeaColorScheme
        TeaType.OOLONG -> OolongTeaColorScheme
        TeaType.WHITE -> WhiteTeaColorScheme
        TeaType.PU_ERH -> PuerhTeaColorScheme
        TeaType.PURPLE -> PurpleTeaColorScheme
        TeaType.ROOIBOS -> RooibosTeaColorScheme
        TeaType.MATE -> MateTeaColorScheme
        TeaType.HERBAL -> HerbalTeaColorScheme
    }

    MaterialTheme (
        colorScheme = teaColorScheme,
        shapes = Shapes,
        content = content
    )

}