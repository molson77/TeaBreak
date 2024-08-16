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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teabreak.R
import com.example.teabreak.TeaBreakTopAppBar
import com.example.teabreak.data.ScoopUnit
import com.example.teabreak.data.TeaType
import com.example.teabreak.data.Utils
import com.example.teabreak.data.getName
import com.example.teabreak.ui.AppViewModelProvider
import com.example.teabreak.ui.elements.TeaTypeLogo
import com.example.teabreak.ui.navigation.NavigationDestination
import com.example.teabreak.ui.theme.TeaBreakTheme
import kotlinx.coroutines.launch

object TeaEntryDestination : NavigationDestination {
    override val route = "tea_entry"
    override val titleRes = R.string.tea_entry_title
}

@Composable
fun TeaEntryScreen(
    modifier: Modifier = Modifier,
    onNavigateUp: () -> Unit,
    viewModel: TeaEntryViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    NewTeaEntryBody(
        teaUiState = viewModel.teaUiState,
        onTeaValueChange = viewModel::updateUiState,
        onSaveClick = {
            coroutineScope.launch {
                viewModel.saveTea()
            }.invokeOnCompletion {
                onNavigateUp.invoke()
            }
        },
        onDeleteClick = {

        },
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
            .fillMaxHeight()
    )
}

@Composable
fun NewTeaEntryBody(
    modifier: Modifier = Modifier,
    teaUiState: TeaUiState,
    onTeaValueChange: (TeaDetails) -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit = {},
    editMode: Boolean = false
) {
    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxHeight()
    ) {
        NewTeaInputForm(
            teaDetails = teaUiState.teaDetails,
            onValueChange = onTeaValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .padding(bottom = 48.dp)
                .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
        ) {
            if (editMode) {
                Button(
                    onClick = onDeleteClick,
                    enabled = true,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth(0.5F).height(56.dp)
                ) {
                    Text(
                        stringResource(R.string.delete)
                    )
                }
            }
            Button(
                onClick = onSaveClick,
                enabled = teaUiState.isEntryValid,
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    stringResource(R.string.save_action)
                )
            }
        }
    }
}

@Composable
fun NewTeaInputForm(
    teaDetails: TeaDetails,
    modifier: Modifier = Modifier,
    onValueChange: (TeaDetails) -> Unit = {},
    enabled: Boolean = true
) {

    val context = LocalContext.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_extra_large))
    ) {

        TeaNameSelector(
            modifier = Modifier
                .padding(top = 24.dp)
                .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
            teaName = teaDetails.name,
            onNameChange = {
                onValueChange(teaDetails.copy(name = it))
            }
        )

        NewTeaTypeSelector(
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

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TeaScoopAmountSelector(
                modifier = Modifier
                    .padding(start = dimensionResource(id = R.dimen.padding_medium))
                    .fillMaxWidth(0.6F),
                scoopAmount = teaDetails.scoopAmount,
                onScoopAmountChange = {
                    onValueChange(teaDetails.copy(scoopAmount = it))
                }
            )
            TeaScoopUnitSelector(
                modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_medium)),
                scoopUnit = teaDetails.scoopUnit,
                onScoopUnitChange = {
                    onValueChange(teaDetails.copy(scoopUnit = it))
                }
            )
        }

        Row(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            NewTeaSteepTimeSelector(
                modifier = Modifier.fillMaxWidth(0.5F),
                steepSeconds = teaDetails.steepSeconds,
                onSteepTimeChange = { onValueChange(teaDetails.copy(steepSeconds = it)) }
            )
            TeaSteepTempSelector(
                steepTemp = teaDetails.temp,
                onSteepTempChange = { onValueChange(teaDetails.copy(temp = it)) }
            )
        }
    }
}

@Composable
fun TeaNameSelector(
    modifier: Modifier = Modifier,
    teaName: String,
    onNameChange: (String) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            stringResource(R.string.tea_name_req).uppercase(),
            style = MaterialTheme.typography.labelSmall
        )
        OutlinedTextField(
            value = teaName,
            onValueChange = { onNameChange.invoke(it) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                disabledBorderColor = MaterialTheme.colorScheme.secondary,
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            enabled = true,
            singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTeaTypeSelector(
    modifier: Modifier = Modifier,
    teaType: TeaType?,
    onTeaTypeChange: (TeaType) -> Unit,
) {

    val context = LocalContext.current
    val teaTypes = TeaType.values().toList()
    val listState = rememberLazyListState()

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
            text = stringResource(R.string.tea_type_req).uppercase(),
            style = MaterialTheme.typography.labelSmall
        )
        LazyRow(
            state = listState,
        ) {
            items(teaTypes) {
                TeaTypeSelectionIcon(
                    teaType = it,
                    selected = it == teaType,
                    onTeaTypeChange = {
                        onTeaTypeChange.invoke(it)
                    }
                )
            }
        }
    }
}

@Composable
fun TeaTypeSelectionIcon(
    modifier: Modifier = Modifier,
    teaType: TeaType,
    selected: Boolean,
    onTeaTypeChange: (TeaType) -> Unit,
) {
    Box(
        modifier = if (selected) modifier else modifier.alpha(0.4F)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = dimensionResource(id = R.dimen.padding_medium),
                    end = dimensionResource(id = R.dimen.padding_small)
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    if(!selected) onTeaTypeChange.invoke(teaType)
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            TeaTypeLogo(teaType = teaType)
            Text(
                text = teaType.getName(),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeaScoopUnitSelector(
    modifier: Modifier = Modifier,
    scoopUnit: ScoopUnit,
    onScoopUnitChange: (ScoopUnit) -> Unit
) {
    val context = LocalContext.current
    val scoopUnits = ScoopUnit.values()
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            stringResource(R.string.scoop_unit_req).uppercase(),
            style = MaterialTheme.typography.labelSmall
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = scoopUnit.getName(),
                onValueChange = {},
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.secondary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                    disabledBorderColor = MaterialTheme.colorScheme.secondary,
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                ),
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                shape = RoundedCornerShape(12.dp),
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

@Composable
fun TeaScoopAmountSelector(
    modifier: Modifier = Modifier,
    scoopAmount: Int,
    onScoopAmountChange: (Int) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            stringResource(R.string.scoop_amount_req).uppercase(),
            style = MaterialTheme.typography.labelSmall
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.padding(bottom = 5.dp)
                ) {
                    Text(
                        text = scoopAmount.toString(),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
            Slider(
                value = scoopAmount.toFloat(),
                valueRange = 1.0F..5.0F,
                steps = 3,
                onValueChange = {
                    onScoopAmountChange.invoke(it.toInt())
                },
                colors = SliderDefaults.colors(
                    thumbColor = MaterialTheme.colorScheme.primary,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewTeaSteepTimeSelector(
    modifier: Modifier = Modifier,
    steepSeconds: Int,
    onSteepTimeChange: (Int) -> Unit
) {

    val duration: Pair<Int, Int> = Utils.secondsToMinutesAndSeconds(steepSeconds)
    val minutes = duration.first
    val seconds = duration.second
    var minutesExpanded by remember { mutableStateOf(false) }
    var secondsExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            stringResource(R.string.time_req).uppercase(),
            style = MaterialTheme.typography.labelSmall
        )
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
                    supportingText = { Text(stringResource(R.string.minutes_req)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        disabledBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(0.5F)
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
                    supportingText = { Text(stringResource(R.string.seconds_req)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                        disabledBorderColor = MaterialTheme.colorScheme.secondary,
                        focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    ),
                    readOnly = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .menuAnchor()
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeaSteepTempSelector(
    steepTemp: Int,
    onSteepTempChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            stringResource(R.string.steep_temp_req).uppercase(),
            style = MaterialTheme.typography.labelSmall
        )
        OutlinedTextField(
            value = "$steepTemp",
            onValueChange = { onSteepTempChange.invoke(it.toInt()) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                disabledBorderColor = MaterialTheme.colorScheme.secondary,
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
private fun NewTeaEntryScreenPreview() {
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
            NewTeaEntryBody(
                modifier = Modifier.padding(it),
                teaUiState = TeaUiState(
                    Utils.getDefaultBrewingPrefs(teaType = TeaType.GREEN, id = 0, name = "Jasmine Pearls").copy(scoopAmount = 1)),
                onTeaValueChange = {},
                onSaveClick = {},
                editMode = false
            )
        }
    }
}
