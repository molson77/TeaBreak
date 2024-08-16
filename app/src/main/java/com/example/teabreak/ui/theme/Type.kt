package com.example.teabreak.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.teabreak.R

val teaBreakFontFamily = FontFamily(
    Font(R.font.raleway_light, FontWeight.Light),
    Font(R.font.raleway, FontWeight.Normal),
    Font(R.font.raleway_medium, FontWeight.Medium),
    Font(R.font.raleway_bold, FontWeight.Bold)
)

// Set of Material typography styles to start with
val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = teaBreakFontFamily,
        fontWeight = FontWeight(600),
        fontSize = 24.sp
    ),
    titleLarge = TextStyle(
        fontFamily = teaBreakFontFamily,
        fontWeight = FontWeight(400),
        fontSize = 22.sp
    ),
    labelSmall = TextStyle(
        fontFamily = teaBreakFontFamily,
        fontWeight = FontWeight(400),
        fontSize = 12.sp
    ),
    labelMedium = TextStyle(
        fontFamily = teaBreakFontFamily,
        fontWeight = FontWeight(600),
        fontSize = 16.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = teaBreakFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = teaBreakFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
    )
)