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

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.teabreak.data.ScoopUnit
import com.example.teabreak.data.TeaType
import com.example.teabreak.data.TeasRepository

/**
 * ViewModel to retrieve and update an tea from the [TeasRepository]'s data source.
 */
class TeaEditViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    /**
     * Holds current tea ui state
     */
    var teaUiState by mutableStateOf(TeaUiState())
        private set

    private val teaId: Int = checkNotNull(savedStateHandle[TeaEditDestination.teaIdArg])

    private fun validateInput(uiState: TeaDetails = teaUiState.teaDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
                    && TeaType.values().contains(type)
                    && scoopAmount >= 0
                    && ScoopUnit.values().contains(scoopUnit)
                    && temp != 0 && temp <= 212
                    && steepSeconds >= 0
        }
    }
}
