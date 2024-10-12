package com.example.queue_calculator.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.queue_calculator.R

val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fontName = GoogleFont("MontSerrat")
val menuFontName = GoogleFont("Rubik Mono One")

// Declare the font families
object AppFont {
    val MontSerrat = FontFamily(
        Font(googleFont = fontName, fontProvider = provider),
    )
    val RubikMonoOne = FontFamily(
        Font(googleFont = menuFontName, fontProvider = provider),
    )
}

private val defaultTypography = Typography()
val CustomTypography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = AppFont.MontSerrat),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = AppFont.MontSerrat),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = AppFont.MontSerrat),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = AppFont.MontSerrat),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = AppFont.MontSerrat),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = AppFont.MontSerrat),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = AppFont.MontSerrat),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = AppFont.MontSerrat),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = AppFont.MontSerrat),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = AppFont.MontSerrat),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = AppFont.MontSerrat),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = AppFont.MontSerrat),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = AppFont.MontSerrat),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = AppFont.MontSerrat),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = AppFont.MontSerrat)
)