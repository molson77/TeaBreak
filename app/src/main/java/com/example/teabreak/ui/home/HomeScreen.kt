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

package com.example.teabreak.ui.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teabreak.R
import com.example.teabreak.TeaBreakTopAppBar
import com.example.teabreak.data.Tea
import com.example.teabreak.data.TeaType
import com.example.teabreak.data.Utils
import com.example.teabreak.data.getName
import com.example.teabreak.ui.AppViewModelProvider
import com.example.teabreak.ui.navigation.NavigationDestination
import com.example.teabreak.ui.theme.TeaBreakTheme

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

/**
 * Entry route for Home screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navigateToTeaEntry: () -> Unit,
    navigateToTeaEdit: (Int) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val context = LocalContext.current
    val homeUiState by viewModel.homeUiState.collectAsState()

    HomeBody(
        teaList = homeUiState.teaList,
        onTeaClick = {
            // TODO: Launch Timer Activity
            Toast.makeText(context, "Navigate to Timer", Toast.LENGTH_SHORT).show()
        },
        onTeaLongClick = {
            navigateToTeaEdit(it)
        }
    )
}

@Composable
private fun HomeBody(
    teaList: List<Tea>, onTeaClick: (Int) -> Unit, onTeaLongClick: (Int) -> Unit, modifier: Modifier = Modifier
) {
    TeaBreakList(
        modifier = modifier,
        teaList = teaList,
        onTeaClick = { onTeaClick(it.id) },
        onTeaLongClick = { onTeaLongClick(it.id) }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TeaBreakList(
    teaList: List<Tea>, onTeaClick: (Tea) -> Unit, onTeaLongClick: (Tea) -> Unit, modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
    ) {
        items(items = teaList.sortedBy { it.type }) { tea ->
            TeaBreakTea(
                tea = tea,
                onTeaClick = onTeaClick,
                onTeaLongClick = onTeaLongClick,
                modifier = Modifier.animateItemPlacement(
                    // TODO animate list population
                    spring()
                )
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TeaBreakTea(
    tea: Tea, onTeaClick: (Tea) -> Unit, onTeaLongClick: (Tea) -> Unit, modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier.combinedClickable(
            onClick = { onTeaClick(tea) },
            onLongClick = { onTeaLongClick(tea) }
        ),
        colors = CardDefaults.cardColors(
            containerColor = Utils.getTeaBackgroundColor(tea.type)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = tea.name,
                        color = Color.White,
                        fontWeight = FontWeight(600),
                        fontSize = TextUnit(25F, TextUnitType.Sp)
                    )
                    Text(
                        text = tea.type.getName(),
                        color = Color.White,
                        fontWeight = FontWeight(300),
                        fontSize = TextUnit(14F, TextUnitType.Sp),
                        fontStyle = FontStyle.Italic
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TeaDetail(iconRes = R.drawable.tea_bag, text = "${tea.scoopAmount} ${tea.scoopUnit.getName()}")
                TeaDetail(iconRes = R.drawable.alarm_clock, text = Utils.formatTime(tea.steepSeconds))
                TeaDetail(iconRes = R.drawable.temperature, text = "${tea.temp}\u00B0")
            }
        }
    }
}

@Composable
fun TeaDetail(iconRes: Int, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(id = iconRes),
            contentDescription = "",
            tint = Color.White)
        Text(
            text = text,
            color = Color.White
        )
    }
}

@Preview(
        uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun TeaBreakTeaPreview() {
    TeaBreakTheme {
        Scaffold(
            topBar = {
                TeaBreakTopAppBar(
                    title = null,
                    canNavigateBack = false,
                    Modifier,
                    navigateUp = {}
                )
            }
        ) {
            HomeBody(
                modifier = Modifier.padding(it),
                teaList = listOf(
                    Utils.getDefaultTeaObject(id = 1, "English Breakfast", TeaType.BLACK),
                    Utils.getDefaultTeaObject(id = 0, "Jasmine Pearls", TeaType.GREEN),
                    Utils.getDefaultTeaObject(id = 3, "African Autumn", TeaType.ROOIBOS),
                    Utils.getDefaultTeaObject(id = 2, "High Mountain", TeaType.OOLONG),
                    Utils.getDefaultTeaObject(id = 7, "Royal Wedding", TeaType.WHITE),
                    Utils.getDefaultTeaObject(id = 5, "Old Tree", TeaType.PU_ERH),
                    Utils.getDefaultTeaObject(id = 4, "Kenyan Premium", TeaType.PURPLE),
                    Utils.getDefaultTeaObject(id = 6, "Yerba Mate", TeaType.MATE),
                ),
                onTeaClick = {},
                onTeaLongClick = {}
            )
        }
    }
}
