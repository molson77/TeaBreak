package com.example.teabreak.data

import androidx.compose.ui.graphics.Color
import com.example.teabreak.ui.tea.TeaDetails
import com.example.teabreak.ui.theme.tb_tea_black_bg
import com.example.teabreak.ui.theme.tb_tea_green_bg
import com.example.teabreak.ui.theme.tb_tea_herbal_bg
import com.example.teabreak.ui.theme.tb_tea_mate_bg
import com.example.teabreak.ui.theme.tb_tea_oolong_bg
import com.example.teabreak.ui.theme.tb_tea_puerh_bg
import com.example.teabreak.ui.theme.tb_tea_purple_bg
import com.example.teabreak.ui.theme.tb_tea_rooibos_bg
import com.example.teabreak.ui.theme.tb_tea_white_bg

object Utils {

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
            TeaType.GREEN -> tb_tea_green_bg
            TeaType.BLACK -> tb_tea_black_bg
            TeaType.OOLONG -> tb_tea_oolong_bg
            TeaType.WHITE -> tb_tea_white_bg
            TeaType.PU_ERH -> tb_tea_puerh_bg
            TeaType.PURPLE -> tb_tea_purple_bg
            TeaType.ROOIBOS -> tb_tea_rooibos_bg
            TeaType.MATE -> tb_tea_mate_bg
            TeaType.HERBAL -> tb_tea_herbal_bg
        }
    }
}