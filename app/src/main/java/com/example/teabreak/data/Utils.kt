package com.example.teabreak.data

import androidx.compose.ui.graphics.Color
import com.example.teabreak.ui.tea.TeaDetails
import com.example.teabreak.ui.theme.black_light_primary
import com.example.teabreak.ui.theme.green_light_primary
import com.example.teabreak.ui.theme.herbal_light_primary
import com.example.teabreak.ui.theme.mate_light_primary
import com.example.teabreak.ui.theme.oolong_light_primary
import com.example.teabreak.ui.theme.puerh_light_primary
import com.example.teabreak.ui.theme.purple_light_primary
import com.example.teabreak.ui.theme.rooibos_light_primary
import com.example.teabreak.ui.theme.white_light_primary
import java.text.SimpleDateFormat
import java.util.Date

object Utils {

    fun getTimeOfDay(): String {
        val hourOfDayFormat = SimpleDateFormat("hh")
        val currentHour = hourOfDayFormat.format(Date()).toInt()
        return if (currentHour <= 12) {
            "Morning"
        } else if (currentHour <= 17) {
            "Afternoon"
        } else {
            "Evening"
        }
    }

    fun secondsToMinutesAndSeconds(totalSeconds: Int): Pair<Int, Int> {
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return Pair(minutes, seconds)
    }

    fun getTotalSecondsFromMinutesAndSeconds(duration: Pair<Int, Int>): Int {
        return (duration.first*60) + duration.second
    }

    fun formatTime(totalSeconds: Int): String {
        val timeSeparated = secondsToMinutesAndSeconds(totalSeconds)
        val minutes = timeSeparated.first
        val seconds = timeSeparated.second
        return "$minutes:%02d".format(seconds)
    }

    /**
     * TeaDetails defaults by TeaType
     */
    fun getDefaultBrewingPrefs(
        teaType: TeaType,
        id: Int?,
        name: String?
    ): TeaDetails {
        return when(teaType) {
            TeaType.GREEN -> TeaDetails(
                id = id ?: 0,
                name = name ?: "",
                type = TeaType.GREEN,
                scoopAmount = 1,
                scoopUnit = ScoopUnit.TEASPOON,
                temp = 180,
                steepSeconds = 120
            )
            TeaType.BLACK -> TeaDetails(
                id = id ?: 0,
                name = name ?: "",
                type = TeaType.BLACK,
                scoopAmount = 1,
                scoopUnit = ScoopUnit.TEASPOON,
                temp = 212,
                steepSeconds = 240
            )
            TeaType.OOLONG -> TeaDetails(
                id = id ?: 0,
                name = name ?: "",
                type = TeaType.OOLONG,
                scoopAmount = 1,
                scoopUnit = ScoopUnit.TEASPOON,
                temp = 195,
                steepSeconds = 180
            )
            TeaType.WHITE -> TeaDetails(
                id = id ?: 0,
                name = name ?: "",
                type = TeaType.WHITE,
                scoopAmount = 2,
                scoopUnit = ScoopUnit.TEASPOON,
                temp = 175,
                steepSeconds = 150
            )
            TeaType.PU_ERH -> TeaDetails(
                id = id ?: 0,
                name = name ?: "",
                type = TeaType.PU_ERH,
                scoopAmount = 1,
                scoopUnit = ScoopUnit.TEASPOON_HEAPING,
                temp = 212,
                steepSeconds = 300
            )
            TeaType.PURPLE -> TeaDetails(
                id = id ?: 0,
                name = name ?: "",
                type = TeaType.PURPLE,
                scoopAmount = 1,
                scoopUnit = ScoopUnit.TEASPOON_HEAPING,
                temp = 180,
                steepSeconds = 180
            )
            TeaType.ROOIBOS -> TeaDetails(
                id = id ?: 0,
                name = name ?: "",
                type = TeaType.ROOIBOS,
                scoopAmount = 1,
                scoopUnit = ScoopUnit.TEASPOON,
                temp = 212,
                steepSeconds = 360
            )
            TeaType.MATE -> TeaDetails(
                id = id ?: 0,
                name = name ?: "",
                type = TeaType.MATE,
                scoopAmount = 1,
                scoopUnit = ScoopUnit.TEASPOON,
                temp = 155,
                steepSeconds = 240
            )
            TeaType.HERBAL -> TeaDetails(
                id = id ?: 0,
                name = name ?: "",
                type = TeaType.HERBAL,
                scoopAmount = 1,
                scoopUnit = ScoopUnit.TEASPOON_HEAPING,
                temp = 212,
                steepSeconds = 360
            )
        }
    }

    fun getDefaultTeaObject(
        id: Int,
        name: String,
        teaType: TeaType
    ): Tea {
        return when(teaType) {
            TeaType.GREEN -> Tea(
                id = id,
                name = name,
                type = TeaType.GREEN,
                scoopAmount = 1,
                scoopUnit = ScoopUnit.TEASPOON,
                temp = 180,
                steepSeconds = 120
            )
            TeaType.BLACK -> Tea(
                id = id,
                name = name,
                type = TeaType.BLACK,
                scoopAmount = 1,
                scoopUnit = ScoopUnit.TEASPOON,
                temp = 212,
                steepSeconds = 240
            )
            TeaType.OOLONG -> Tea(
                id = id,
                name = name,
                type = TeaType.OOLONG,
                scoopAmount = 1,
                scoopUnit = ScoopUnit.TEASPOON,
                temp = 195,
                steepSeconds = 180
            )
            TeaType.WHITE -> Tea(
                id = id,
                name = name,
                type = TeaType.WHITE,
                scoopAmount = 2,
                scoopUnit = ScoopUnit.TEASPOON,
                temp = 175,
                steepSeconds = 150
            )
            TeaType.PU_ERH -> Tea(
                id = id,
                name = name,
                type = TeaType.PU_ERH,
                scoopAmount = 1,
                scoopUnit = ScoopUnit.TEASPOON_HEAPING,
                temp = 212,
                steepSeconds = 300
            )
            TeaType.PURPLE -> Tea(
                id = id,
                name = name,
                type = TeaType.PURPLE,
                scoopAmount = 1,
                scoopUnit = ScoopUnit.TEASPOON_HEAPING,
                temp = 180,
                steepSeconds = 180
            )
            TeaType.ROOIBOS -> Tea(
                id = id,
                name = name,
                type = TeaType.ROOIBOS,
                scoopAmount = 1,
                scoopUnit = ScoopUnit.TEASPOON,
                temp = 212,
                steepSeconds = 360
            )
            TeaType.MATE -> Tea(
                id = id,
                name = name,
                type = TeaType.MATE,
                scoopAmount = 1,
                scoopUnit = ScoopUnit.TEASPOON,
                temp = 155,
                steepSeconds = 240
            )
            TeaType.HERBAL -> Tea(
                id = id,
                name = name,
                type = TeaType.HERBAL,
                scoopAmount = 1,
                scoopUnit = ScoopUnit.TEASPOON_HEAPING,
                temp = 212,
                steepSeconds = 360
            )
        }
    }

    fun getTeaBackgroundColor(teaType: TeaType): Color {
        return when(teaType) {
            TeaType.GREEN -> green_light_primary
            TeaType.BLACK -> black_light_primary
            TeaType.OOLONG -> oolong_light_primary
            TeaType.WHITE -> white_light_primary
            TeaType.PU_ERH -> puerh_light_primary
            TeaType.PURPLE -> purple_light_primary
            TeaType.ROOIBOS -> rooibos_light_primary
            TeaType.MATE -> mate_light_primary
            TeaType.HERBAL -> herbal_light_primary
        }
    }
}