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

package com.example.teabreak.ui.tea

import android.content.res.Configuration
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teabreak.TeaBreakTopAppBar
import com.example.teabreak.R
import com.example.teabreak.data.TeaType
import com.example.teabreak.data.Utils
import com.example.teabreak.ui.AppViewModelProvider
import com.example.teabreak.ui.navigation.NavigationDestination
import com.example.teabreak.ui.theme.TeaBreakTheme
import kotlinx.coroutines.launch

object TeaEditDestination : NavigationDestination {
    override val route = "tea_edit"
    override val titleRes = R.string.edit_tea_title
    const val teaIdArg = "teaId"
    val routeWithArgs = "$route/{$teaIdArg}"
}

@Composable
fun TeaEditScreen(
    onNavigateUp: () -> Unit,
    viewModel: TeaEditViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = null) {
        viewModel.setInitialUIState()
    }

    TeaEntryBody(
        teaUiState = viewModel.teaUiState,
        onTeaValueChange = viewModel::updateUiState,
        editMode = true,
        onSaveClick = {
            coroutineScope.launch {
                viewModel.saveTea()
            }.invokeOnCompletion {
                onNavigateUp.invoke()
            }
        },
        onDeleteClick = {
            coroutineScope.launch {
                viewModel.deleteTea()
            }.invokeOnCompletion {
                onNavigateUp.invoke()
            }
        }
    )
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun TeaEditScreenPreview() {
    TeaBreakTheme {
        Scaffold(
            topBar = {
                TeaBreakTopAppBar(
                    title = stringResource(TeaEditDestination.titleRes),
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
