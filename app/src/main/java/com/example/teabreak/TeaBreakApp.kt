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

package com.example.teabreak

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.teabreak.R.string
import com.example.teabreak.data.TeaType
import com.example.teabreak.data.Utils
import com.example.teabreak.ui.navigation.TeaBreakNavHost
import com.example.teabreak.ui.tea.TeaEntryBody
import com.example.teabreak.ui.tea.TeaUiState
import com.example.teabreak.ui.theme.TeaBreakTheme

/**
 * Top level composable that represents screens for the application.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TeaBreakApp(navController: NavHostController = rememberNavController()) {
    TeaBreakNavHost(navController = navController)
}

/**
 * App bar to display title and conditionally display the back navigation.
 */
@Composable
fun TeaBreakTopAppBar(
    title: String?,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {}
) {
    TeaBreakTheme {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (canNavigateBack) {
                    Box(
                        Modifier
                            .align(Alignment.CenterStart)
                            .padding(horizontal = 8.dp)) {
                        IconButton(onClick = navigateUp) {
                            Icon(
                                imageVector = Filled.ArrowBack,
                                contentDescription = stringResource(string.back_button),
                                tint = Color.White
                            )
                        }
                    }
                }
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (title != null) {
                        Text(
                            text = title,
                            color = Color.White,
                            fontWeight = FontWeight.W600
                        )
                    } else {
                        Icon(
                            modifier = Modifier.size(40.dp),
                            painter = painterResource(id = R.drawable.teapotandcup),
                            contentDescription = "TeaBreak logo",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TeaBreakStructurePreview() {
    TeaBreakTheme {
        Scaffold(
            topBar = {
                TeaBreakTopAppBar(
                    title = null,
                    canNavigateBack = true,
                    Modifier,
                    navigateUp = {}
                )
            }
        ) {
            TeaEntryBody(
                modifier = Modifier.padding(it),
                teaUiState = TeaUiState(
                Utils.getDefaultBrewingPrefs(teaType = TeaType.GREEN, id = 0, name = "Jasmine Pearls")),
                onTeaValueChange = {},
                onSaveClick = {}
            )
        }
    }
}
