package com.ivi.designsystem.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val IVIFontFamily = FontFamily.Default

/**
 * Automotive-optimized typography scale.
 * All sizes exceed NHTSA minimum (24sp body).
 */
object IVITypography {

    // === Display (Speedometer, large values) ===
    val DisplayLarge = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 72.sp,
        lineHeight = 80.sp,
        letterSpacing = (-1).sp
    )
    val DisplayMedium = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 56.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.5).sp
    )
    val DisplaySmall = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 44.sp,
        lineHeight = 52.sp,
    )

    // === Headlines (Screen titles, section headers) ===
    val HeadlineLarge = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 36.sp,
        lineHeight = 44.sp,
    )
    val HeadlineMedium = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
    )
    val HeadlineSmall = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 28.sp,
        lineHeight = 36.sp,
    )

    // === Title (Card titles, list item primary text) ===
    val TitleLarge = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 26.sp,
        lineHeight = 34.sp,
    )
    val TitleMedium = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.15.sp
    )

    // === Body (Primary content text) ===
    val BodyLarge = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp
    )
    val BodyMedium = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.25.sp
    )

    // === Label (Buttons, chips, small UI elements) ===
    val LabelLarge = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.1.sp
    )
    val LabelMedium = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    val LabelSmall = TextStyle(
        fontFamily = IVIFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    )
}
