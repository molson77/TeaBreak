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
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teabreak.TeaBreakTopAppBar
import com.example.teabreak.R
import com.example.teabreak.data.ScoopUnit
import com.example.teabreak.data.TeaType
import com.example.teabreak.data.Utils
import com.example.teabreak.data.getName
import com.example.teabreak.ui.AppViewModelProvider
import com.example.teabreak.ui.navigation.NavigationDestination
import com.example.teabreak.ui.theme.TeaBreakTheme
import kotlinx.coroutines.launch

object TeaEntryDestination : NavigationDestination {
    override val route = "tea_entry"
    override val titleRes = R.string.tea_entry_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeaEntryScreen(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: TeaEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TeaBreakTopAppBar(
                title = stringResource(TeaEntryDestination.titleRes),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        TeaEntryBody(
            teaUiState = viewModel.teaUiState,
            onTeaValueChange = viewModel::updateUiState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveTea()
                    navigateBack()
                }
            },
            onDeleteClick = {

            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun TeaEntryBody(
    modifier: Modifier = Modifier,
    teaUiState: TeaUiState,
    onTeaValueChange: (TeaDetails) -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit = {},
    editMode: Boolean = false
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_large)),
        modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium))
    ) {
        TeaInputForm(
            teaDetails = teaUiState.teaDetails,
            onValueChange = onTeaValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            enabled = teaUiState.isEntryValid,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.save_action))
        }
    }
}

@Composable
fun TeaInputForm(
    teaDetails: TeaDetails,
    modifier: Modifier = Modifier,
    onValueChange: (TeaDetails) -> Unit = {},
    enabled: Boolean = true
) {

    val context = LocalContext.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
    ) {

        OutlinedTextField(
            value = teaDetails.name,
            onValueChange = { onValueChange(teaDetails.copy(name = it)) },
            label = { Text(stringResource(R.string.tea_name_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )

        TeaTypeSelector(
            teaType = teaDetails.type,
            onTeaTypeChange = {
                val defaultTeaPreferences = Utils.getDefaultBrewingPrefs(
                    teaType = it,
                    id = teaDetails.id,
                    name = teaDetails.name
                )
                onValueChange(defaultTeaPreferences)
                Toast.makeText(context, "${it.getName()} default preferences set", Toast.LENGTH_SHORT).show()
            }
        )

        TeaScoopSelector(
            scoopAmount = teaDetails.scoopAmount,
            scoopUnit = teaDetails.scoopUnit,
            onScoopAmountChange = { onValueChange(teaDetails.copy(scoopAmount = it)) },
            onScoopUnitChange = { onValueChange(teaDetails.copy(scoopUnit = it)) }
        )

        TeaSteepTimeSelector(
            steepSeconds = teaDetails.steepSeconds,
            onSteepTimeChange = { onValueChange(teaDetails.copy(steepSeconds = it)) }
        )

        TeaSteepTempSelector(
            steepTemp = teaDetails.temp,
            onSteepTempChange = { onValueChange(teaDetails.copy(temp = it)) }
        )

        if (enabled) {
            Text(
                text = stringResource(R.string.required_fields),
                modifier = Modifier.padding(start = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeaTypeSelector(
    teaType: TeaType?,
    onTeaTypeChange: (TeaType) -> Unit,
) {

    val context = LocalContext.current
    val teaTypes = TeaType.values()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = teaType?.getName() ?: "Select a type of tea",
            onValueChange = {},
            label = { Text(stringResource(R.string.tea_type_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            teaTypes.forEach { type ->
                DropdownMenuItem(
                    text = { Text(text = type.getName()) },
                    onClick = {
                        expanded = false
                        onTeaTypeChange.invoke(type)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeaScoopSelector(
    scoopAmount: Int,
    scoopUnit: ScoopUnit,
    onScoopAmountChange: (Int) -> Unit,
    onScoopUnitChange: (ScoopUnit) -> Unit
) {

    val context = LocalContext.current
    val scoopUnits = ScoopUnit.values()
    var expanded by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        // Scoop Amount
        OutlinedTextField(
            value = scoopAmount.toString(),
            onValueChange = { onScoopAmountChange.invoke(it.toInt()) },
            label = { Text(stringResource(R.string.scoop_amount_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(0.5F)
        )

        // Scoop Unit
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = scoopUnit.getName(),
                onValueChange = {},
                label = { Text(stringResource(R.string.scoop_unit_req)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                scoopUnits.forEach { unit ->
                    DropdownMenuItem(
                        text = { Text(text = unit.getName()) },
                        onClick = {
                            expanded = false
                            onScoopUnitChange.invoke(unit)
                            Toast.makeText(context, unit.getName(), Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeaSteepTimeSelector(
    steepSeconds: Int,
    onSteepTimeChange: (Int) -> Unit
) {

    val context = LocalContext.current

    val duration: Pair<Int, Int> = Utils.secondsToMinutesAndSeconds(steepSeconds)
    val minutes = duration.first
    val seconds = duration.second
    var minutesExpanded by remember { mutableStateOf(false) }
    var secondsExpanded by remember { mutableStateOf(false) }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {

        // Minutes
        ExposedDropdownMenuBox(
            expanded = minutesExpanded,
            onExpandedChange = {
                minutesExpanded = !minutesExpanded
            }
        ) {
            OutlinedTextField(
                value = minutes.toString(),
                onValueChange = {},
                label = { Text(stringResource(R.string.minutes_req)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = minutesExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .width(100.dp)
            )

            ExposedDropdownMenu(
                expanded = minutesExpanded,
                onDismissRequest = { minutesExpanded = false }
            ) {
                (0..10).forEach { num ->
                    DropdownMenuItem(
                        text = { Text(text = num.toString()) },
                        onClick = {
                            minutesExpanded = false
                            onSteepTimeChange.invoke(Utils.getTotalSecondsFromMinutesAndSeconds(Pair(num, seconds)))
                        }
                    )
                }
            }
        }

        Text(
            text = ":",
            fontSize = 30.sp
        )

        // Seconds
        ExposedDropdownMenuBox(
            expanded = secondsExpanded,
            onExpandedChange = {
                secondsExpanded = !secondsExpanded
            }
        ) {
            OutlinedTextField(
                value = seconds.toString(),
                onValueChange = {},
                label = { Text(stringResource(R.string.seconds_req)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = secondsExpanded) },
                modifier = Modifier
                    .menuAnchor()
                    .width(100.dp)
            )

            ExposedDropdownMenu(
                expanded = secondsExpanded,
                onDismissRequest = { secondsExpanded = false }
            ) {
                (0..59).forEach { num ->
                    DropdownMenuItem(
                        text = { Text(text = num.toString()) },
                        onClick = {
                            secondsExpanded = false
                            onSteepTimeChange.invoke(Utils.getTotalSecondsFromMinutesAndSeconds(Pair(minutes, num)))
                        }
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeaSteepTempSelector(
    steepTemp: Int,
    onSteepTempChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        // Steep Temp
        OutlinedTextField(
            value = "$steepTemp",
            onValueChange = { onSteepTempChange.invoke(it.toInt()) },
            label = { Text(stringResource(R.string.steep_temp_req)) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TeaEntryScreenPreview() {
    TeaBreakTheme {
        Scaffold(
            topBar = {
                TeaBreakTopAppBar(
                    title = stringResource(TeaEntryDestination.titleRes),
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
