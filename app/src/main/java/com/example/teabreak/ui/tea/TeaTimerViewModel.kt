package com.example.teabreak.ui.tea

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.teabreak.data.Tea
import com.example.teabreak.data.TeaType
import com.example.teabreak.data.TeasRepository
import com.example.teabreak.data.Utils
import kotlinx.coroutines.launch

class TeaTimerViewModel(
    private val teasRepository: TeasRepository
) : ViewModel() {

    private var tea: Tea? = null

    /**
     * Holds current tea ui state
     */
    var teaUiState by mutableStateOf(TeaTimerUiState(tea = Utils.getDefaultTeaObject(0,"", TeaType.GREEN)))
        private set

    /**
     * Sets initial UI state for editing a tea object
     */
    fun setInitialUIState(teaId: Int) {
        viewModelScope.launch {
            teasRepository.getTeaStream(teaId).collect {
                it?.let {
                    tea = it
                    teaUiState = TeaTimerUiState(it, it.steepSeconds, it.steepSeconds, TimerState.IDLE)
                }
            }
        }
    }

    /**
     * Updates the [teaUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(teaTimerUiState: TeaTimerUiState) {
        teaUiState =
            teaTimerUiState
    }

    fun updateUiState(timeRemaining: Int) {
        if (timeRemaining == 0) {
            teaUiState = teaUiState.copy(
                timeRemaining = 0,
                timerState = TimerState.FINISHED
            )
        } else {
            teaUiState = teaUiState.copy(
                timeRemaining = timeRemaining,
                timerState = TimerState.STEEPING
            )
        }
    }

    enum class TimerState {
        IDLE,
        STEEPING,
        FINISHED
    }

    data class TeaTimerUiState(
        val tea: Tea,
        val timeRemaining: Int = tea.steepSeconds,
        val steepTime: Int = tea.steepSeconds,
        val timerState: TimerState = TimerState.IDLE
    )
}