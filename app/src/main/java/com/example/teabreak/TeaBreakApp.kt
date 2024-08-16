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
import android.os.Bundle
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons.Filled
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.teabreak.R.string
import com.example.teabreak.data.Utils
import com.example.teabreak.ui.home.HomeDestination
import com.example.teabreak.ui.navigation.TeaBreakNavHost
import com.example.teabreak.ui.tea.TeaEditDestination
import com.example.teabreak.ui.tea.TeaEntryDestination
import com.example.teabreak.ui.theme.TeaBreakTheme
import java.util.Calendar
import java.util.Date

/**
 * Top level composable that represents screens for the application.
 */
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TeaBreakApp(navController: NavHostController = rememberNavController()) {
    TeaBreakScaffold(navController = navController)
}

@Composable
fun TeaBreakScaffold(navController: NavHostController) {

    var titleRes: Int? by remember {
        mutableStateOf(null)
    }
    var canNavigateBack: Boolean by remember {
        mutableStateOf(false)
    }
    var showFab: Boolean by remember {
        mutableStateOf(true)
    }

    navController.addOnDestinationChangedListener { navController: NavController, navDestination: NavDestination, bundle: Bundle? ->
        if (navDestination.route == HomeDestination.route) {
            titleRes = null
            canNavigateBack = false
            showFab = true
        } else if (navDestination.route == TeaEntryDestination.route) {
            titleRes = TeaEntryDestination.titleRes
            canNavigateBack = true
            showFab = false
        } else if (navDestination.route?.contains(TeaEditDestination.route) == true) {
            titleRes = TeaEditDestination.titleRes
            canNavigateBack = true
            showFab = false
        } else {
            titleRes = null
            canNavigateBack = false
            showFab = true
        }
    }

    Scaffold(
        topBar = {
            TeaBreakTopAppBar(
                title = titleRes?.let { stringResource(id = it) },
                canNavigateBack = canNavigateBack,
                navigateUp = { navController.popBackStack() },
                navigateToEntry = { navController.navigate(TeaEntryDestination.route) }
            )
        }
    ) {
        TeaBreakNavHost(navController = navController, modifier = Modifier
            .padding(it)
            .fillMaxSize())
    }
}

/**
 * App bar to display title and conditionally display the back navigation.
 */
@Composable
fun TeaBreakTopAppBar(
    title: String?,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit = {},
    navigateToEntry: () -> Unit = {}
) {

    val timeOfDay = remember {
        Utils.getTimeOfDay()
    }

    TeaBreakTheme {
        Surface(
            color = MaterialTheme.colorScheme.primary,
            modifier = modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (canNavigateBack) {
                    // in tea add or edit
                    Box(
                        Modifier
                            .align(Alignment.CenterStart)
                            .padding(horizontal = 8.dp)) {
                        IconButton(onClick = navigateUp) {
                            Icon(
                                imageVector = Filled.ArrowBack,
                                contentDescription = stringResource(string.back_button),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        if (title != null) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.W600,
                                modifier = Modifier
                            )
                        } else {
                            Text(
                                text = "Good Evening, Matt!",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.W600,
                                modifier = Modifier
                            )
                        }
                    }
                } else {
                    // at home screen
                    // navController.navigate(TeaEntryDestination.route)
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (title != null) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.W600,
                                modifier = Modifier
                            )
                        } else {
                            Text(
                                text = "Good ${timeOfDay}!",
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.W600,
                                modifier = Modifier
                            )
                        }
                    }
                    Box(
                        Modifier
                            .align(Alignment.CenterEnd)
                            .padding(horizontal = 8.dp)) {
                        IconButton(
                            onClick = { navigateToEntry.invoke() }
                        ) {
                            Icon(
                                imageVector = Filled.Add,
                                contentDescription = stringResource(string.back_button),
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AppBarPreview() {
    TeaBreakTheme {
        Scaffold(
            topBar = {
                TeaBreakTopAppBar(title = null, canNavigateBack = false)
            }
        ) {
            Surface(
                Modifier
                    .fillMaxSize()
                    .padding(it)) {

            }
        }
    }
}
