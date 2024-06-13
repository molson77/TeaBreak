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
import androidx.lifecycle.ViewModel
import com.example.teabreak.data.ScoopUnit
import com.example.teabreak.data.Tea
import com.example.teabreak.data.TeaType
import com.example.teabreak.data.TeasRepository
import java.text.NumberFormat

/**
 * ViewModel to validate and insert teas in the Room database.
 */
class TeaEntryViewModel(private val teasRepository: TeasRepository) : ViewModel() {

    /**
     * Holds current tea ui state
     */
    var teaUiState by mutableStateOf(TeaUiState())
        private set

    /**
     * Updates the [teaUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(teaDetails: TeaDetails) {
        teaUiState =
            TeaUiState(teaDetails = teaDetails, isEntryValid = validateInput(teaDetails))
    }

    suspend fun saveTea() {
        if (validateInput()) {
            teasRepository.insertTea(teaUiState.teaDetails.toTea())
        }
    }

    private fun validateInput(uiState: TeaDetails = teaUiState.teaDetails): Boolean {
        return with(uiState) {
            name.isNotBlank()
                    && TeaType.values().contains(uiState.type)
                    && scoopAmount >= 0
                    && ScoopUnit.values().contains(scoopUnit)
                    && temp != 0 && temp <= 212
                    && steepSeconds >= 0
        }
    }
}

/**
 * Represents Ui State for a Tea.
 */
data class TeaUiState(
    val teaDetails: TeaDetails = TeaDetails(),
    val isEntryValid: Boolean = false
)

data class TeaDetails(
    val id: Int = 0,
    val name: String = "",
    val type: TeaType = TeaType.GREEN,
    val scoopAmount: Int = 1,
    val scoopUnit: ScoopUnit = ScoopUnit.TEASPOON,
    val temp: Int = 180,
    val steepSeconds: Int = 120
)

/**
 * Extension function to convert [TeaDetails] to [Tea]
 */
fun TeaDetails.toTea(): Tea = Tea(
    id = id,
    name = name,
    type = type,
    scoopAmount = scoopAmount,
    scoopUnit = scoopUnit,
    temp = temp,
    steepSeconds = steepSeconds
)

/**
 * Extension function to convert [Tea] to [TeaUiState]
 */
fun Tea.toTeaUiState(isEntryValid: Boolean = false): TeaUiState = TeaUiState(
    teaDetails = this.toTeaDetails(),
    isEntryValid = isEntryValid
)

/**
 * Extension function to convert [Tea] to [TeaDetails]
 */
fun Tea.toTeaDetails(): TeaDetails = TeaDetails(
    id = id,
    name = name,
    type = type,
    scoopAmount = scoopAmount,
    scoopUnit = scoopUnit,
    temp = temp,
    steepSeconds = steepSeconds
)
