package com.example.teabreak

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.teabreak.data.Tea
import com.example.teabreak.data.TeaType
import com.example.teabreak.data.Utils
import com.example.teabreak.data.getName
import com.example.teabreak.ui.AppViewModelProvider
import com.example.teabreak.ui.home.TeaDetail
import com.example.teabreak.ui.tea.TeaTimerViewModel
import com.example.teabreak.ui.theme.TeaTimerTheme
import android.Manifest
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextAlign

class TeaTimerActivity : ComponentActivity() {

    private var teaId: Int = 0
    private lateinit var teaType: TeaType

    private val viewModel: TeaTimerViewModel by viewModels { AppViewModelProvider.Factory }

    private val br: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            updateViewModel(intent)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {

        } else {

        }
    }

    companion object {
        const val TEA_ID = "tea_id"
        const val TEA_TYPE = "tea_type"
        fun createTimerActivityIntent(
            context: Context,
            tea: Tea
        ): Intent {
            return Intent(context, TeaTimerActivity::class.java).apply {
                putExtra(TEA_ID, tea.id)
                putExtra(TEA_TYPE, tea.type.name)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        teaId = intent.extras?.getInt(TEA_ID) ?: 0
        teaType = intent.extras?.getString(TEA_TYPE)?.let { TeaType.valueOf(it) } ?: TeaType.GREEN

        if (!TeaTimerService.isTimerServiceActive) {
            viewModel.setInitialUIState(teaId)
        }

        val backgroundColor = Utils.getTeaBackgroundColor(teaType)

        setContent {
            TeaTimerTheme(teaType = teaType) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = backgroundColor
                ) {
                    TeaTimer(
                        viewModel,
                        onStart = { startTeaTimer() },
                        onClose = { cancelTeaTimer() }
                    )
                }
                BackHandler {
                    cancelTeaTimer()
                }
            }
        }

        askNotificationPermissionIfNecessary()
    }

    private fun updateViewModel(intent: Intent) {
        val secondsRemaining = intent.extras?.getLong(TeaTimerService.COUNTDOWN_MILLIS_REMAINING)?.div(1000)?.toInt()
        secondsRemaining?.let {
            viewModel.updateUiState(it)
        }
    }

    override fun onResume() {
        super.onResume()
        moveToBackground()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(br, IntentFilter(TeaTimerService.COUNTDOWN_BR), RECEIVER_NOT_EXPORTED)
        } else {
            registerReceiver(br, IntentFilter(TeaTimerService.COUNTDOWN_BR))
        }
    }

    override fun onPause() {
        super.onPause()
        moveToForeground()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(br)
        stopService(Intent(this, TeaTimerService::class.java))
    }

    private fun startTeaTimer() {
        if (viewModel.teaUiState.tea.name != "") {
            val teaTimerService = Intent(this, TeaTimerService::class.java)
            teaTimerService.putExtra(TeaTimerService.TIMER_ACTION, TeaTimerService.START)
            teaTimerService.putExtra(TeaTimerService.TEA_ID, viewModel.teaUiState.tea.id)
            teaTimerService.putExtra(TeaTimerService.TEA_NAME, viewModel.teaUiState.tea.name)
            teaTimerService.putExtra(TeaTimerService.TEA_TYPE, viewModel.teaUiState.tea.type)
            teaTimerService.putExtra(TeaTimerService.STEEP_TIME_SECONDS, viewModel.teaUiState.tea.steepSeconds)
            startService(teaTimerService)
        }
    }

    private fun cancelTeaTimer() {
        val teaTimerService = Intent(this, TeaTimerService::class.java)
        teaTimerService.putExtra(TeaTimerService.TIMER_ACTION, TeaTimerService.CANCEL)
        startService(teaTimerService)
        stopService(Intent(this, TeaTimerService::class.java))
        finish()
    }

    private fun moveToBackground() {
        val teaTimerService = Intent(this, TeaTimerService::class.java)
        teaTimerService.putExtra(TeaTimerService.TIMER_ACTION, TeaTimerService.MOVE_TO_BACKGROUND)
        startService(teaTimerService)
    }

    private fun moveToForeground() {
        val teaTimerService = Intent(this, TeaTimerService::class.java)
        teaTimerService.putExtra(TeaTimerService.TIMER_ACTION, TeaTimerService.MOVE_TO_FOREGROUND)
        teaTimerService.putExtra(TeaTimerService.TEA_ID, viewModel.teaUiState.tea.id)
        teaTimerService.putExtra(TeaTimerService.TEA_NAME, viewModel.teaUiState.tea.name)
        teaTimerService.putExtra(TeaTimerService.TEA_TYPE, viewModel.teaUiState.tea.type)
        teaTimerService.putExtra(TeaTimerService.STEEP_TIME_SECONDS, viewModel.teaUiState.tea.steepSeconds)
        startService(teaTimerService)
    }

    private fun askNotificationPermissionIfNecessary() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            // Granted
        } else {
            // Directly ask for the permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}

@Composable
fun TeaTimer(
    viewModel: TeaTimerViewModel,
    onStart: () -> Unit,
    onClose: () -> Unit
) {

    var uiState = viewModel.teaUiState

    Box(
        Modifier.fillMaxSize()
    ) {
        TeaTimerTitle(
            uiState,
            modifier = Modifier
                .padding(top = 45.dp)
                .fillMaxWidth(.80F)
                .align(Alignment.TopCenter)
        )

        TeaTimerClock(
            uiState,
            modifier = Modifier
                .fillMaxWidth(.70F)
                .aspectRatio(1F)
                .align(Alignment.Center)
        )

        TeaTimerButton(
            teaTimerUiState = uiState,
            onStart = { onStart.invoke() },
            onClose = { onClose.invoke() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 120.dp)
        )
    }
}

@Composable
fun TeaTimerTitle(teaTimerUiState: TeaTimerViewModel.TeaTimerUiState, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.width(IntrinsicSize.Max)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(IntrinsicSize.Max)
        ) {
            Text(
                text = if (teaTimerUiState.timerState == TeaTimerViewModel.TimerState.STEEPING) "Now Brewing:" else teaTimerUiState.tea.type.getName(),
                style = TextStyle(
                    color = Color.White,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight(300),
                    fontSize = TextUnit(14F, TextUnitType.Sp),
                    textAlign = TextAlign.Center,
                )
            )
            Text(
                text = teaTimerUiState.tea.name,
                style = TextStyle(
                    color = Color.White,
                    fontWeight = FontWeight(700),
                    fontSize = TextUnit(36F, TextUnitType.Sp),
                    textAlign = TextAlign.Center,
                )
            )
        }
        TeaTimerDetails(
            tea = teaTimerUiState.tea,
            modifier = Modifier.padding(top = 20.dp)
        )
    }
}

@Composable
fun TeaTimerDetails(tea: Tea, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(50.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TeaDetail(iconRes = R.drawable.tea_bag, text = "${tea.scoopAmount} ${tea.scoopUnit.getName()}")
            TeaDetail(iconRes = R.drawable.temperature, text = "${tea.temp}\u00B0")
        }
    }
}

@Composable
fun TeaTimerClock(teaTimerUiState: TeaTimerViewModel.TeaTimerUiState, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {

        CircularProgressIndicator(
            progress = (teaTimerUiState.timeRemaining.toFloat()/teaTimerUiState.steepTime.toFloat()),
            color = Color.White,
            strokeWidth = 7.dp,
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = Utils.formatTime(teaTimerUiState.timeRemaining),
            color = Color.White,
            fontWeight = FontWeight(900),
            fontSize = TextUnit(76F, TextUnitType.Sp)
        )
    }
}

@Composable
fun TeaTimerButton(
    teaTimerUiState: TeaTimerViewModel.TeaTimerUiState,
    onStart: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when (teaTimerUiState.timerState) {
            TeaTimerViewModel.TimerState.IDLE -> {
                IconButton(
                    onClick = {
                        onStart.invoke()
                    }
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Start Tea Timer",
                        tint = Color.White,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
            else -> {
                IconButton(
                    onClick = {
                        onClose.invoke()
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close Tea Timer",
                        tint = if (teaTimerUiState.timerState == TeaTimerViewModel.TimerState.STEEPING)
                            Color.White.copy(alpha = 0.5F) else Color.White,
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TimerPreview() {
    TeaTimerTheme(teaType = TeaType.OOLONG) {

        val tea = Utils.getDefaultTeaObject(id = 1, "French Blue Lavender", TeaType.HERBAL)

        var uiState = TeaTimerViewModel.TeaTimerUiState(tea, 234, tea.steepSeconds, TeaTimerViewModel.TimerState.STEEPING)

        Box(
            Modifier
                .fillMaxSize()
                .background(Utils.getTeaBackgroundColor(tea.type))
        ) {
            TeaTimerTitle(
                uiState,
                modifier = Modifier
                    .padding(top = 45.dp)
                    .fillMaxWidth(.80F)
                    .align(Alignment.TopCenter)
            )

            TeaTimerClock(
                uiState,
                modifier = Modifier
                    .fillMaxWidth(.70F)
                    .aspectRatio(1F)
                    .align(Alignment.Center)
            )

            TeaTimerButton(
                teaTimerUiState = uiState,
                onStart = {  },
                onClose = {  },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 120.dp)
            )
        }
    }
}