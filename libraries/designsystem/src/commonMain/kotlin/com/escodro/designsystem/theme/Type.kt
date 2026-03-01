package com.escodro.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.escodro.designsystem.config.DesignSystemConfig
import com.escodro.resources.Lato_Bold
import com.escodro.resources.Lato_Light
import com.escodro.resources.Lato_Regular
import com.escodro.resources.Nunito_Bold
import com.escodro.resources.Nunito_Medium
import com.escodro.resources.OpenSans_Bold
import com.escodro.resources.OpenSans_Light
import com.escodro.resources.OpenSans_Medium
import com.escodro.resources.OpenSans_Regular
import com.escodro.resources.Res
import org.jetbrains.compose.resources.Font

@Composable
fun openSansFamily(): FontFamily = FontFamily(
    Font(resource = Res.font.OpenSans_Regular, weight = FontWeight.Normal),
    Font(resource = Res.font.OpenSans_Bold, weight = FontWeight.Bold),
    Font(resource = Res.font.OpenSans_Medium, weight = FontWeight.Medium),
    Font(resource = Res.font.OpenSans_Light, weight = FontWeight.Light),
)

@Composable
fun latoFamily(): FontFamily = FontFamily(
    Font(resource = Res.font.Lato_Regular, weight = FontWeight.Normal),
    Font(resource = Res.font.Lato_Bold, weight = FontWeight.Bold),
    Font(resource = Res.font.Lato_Light, weight = FontWeight.Light),
)

@Composable
fun nunitoFamily(): FontFamily = FontFamily(
    Font(resource = Res.font.Nunito_Bold, weight = FontWeight.Bold),
    Font(resource = Res.font.Nunito_Medium, weight = FontWeight.Medium),
)

@Suppress("LongMethod")
@Composable
fun alkaaTypography(): Typography {
    val openSansFamily: FontFamily = openSansFamily()
    val nunitoFamily: FontFamily = nunitoFamily()
    val latoFamily: FontFamily = latoFamily()

    val titleFontFamily = if (DesignSystemConfig.IsNewDesignEnabled) {
        nunitoFamily
    } else {
        openSansFamily
    }

    val defaultFontFamily = if (DesignSystemConfig.IsNewDesignEnabled) {
        nunitoFamily
    } else {
        latoFamily
    }

    return remember(key1 = titleFontFamily, key2 = defaultFontFamily) {
        Typography(
            displayLarge = TextStyle(
                fontFamily = titleFontFamily,
                fontWeight = FontWeight.W800,
                fontSize = 48.sp,
                lineHeight = 56.sp,
                letterSpacing = (-0.5).sp,
            ),
            displayMedium = TextStyle(
                fontFamily = titleFontFamily,
                fontWeight = FontWeight.W800,
                fontSize = 38.sp,
                lineHeight = 46.sp,
                letterSpacing = (-0.25).sp,
            ),
            displaySmall = TextStyle(
                fontFamily = titleFontFamily,
                fontWeight = FontWeight.W800,
                fontSize = 32.sp,
                lineHeight = 40.sp,
                letterSpacing = (-0.25).sp,
            ),
            headlineLarge = TextStyle(
                fontFamily = titleFontFamily,
                fontWeight = FontWeight.W800,
                fontSize = 26.sp,
                lineHeight = 32.sp,
                letterSpacing = 0.sp,
            ),
            headlineMedium = TextStyle(
                fontFamily = titleFontFamily,
                fontWeight = FontWeight.W800,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                letterSpacing = 0.sp,
            ),
            headlineSmall = TextStyle(
                fontFamily = titleFontFamily,
                fontWeight = FontWeight.W700,
                fontSize = 20.sp,
                lineHeight = 26.sp,
                letterSpacing = 0.sp,
            ),
            titleLarge = TextStyle(
                fontFamily = titleFontFamily,
                fontWeight = FontWeight.W700,
                fontSize = 17.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.sp,
            ),
            titleMedium = TextStyle(
                fontFamily = titleFontFamily,
                fontWeight = FontWeight.W700,
                fontSize = 15.sp,
                lineHeight = 22.sp,
                letterSpacing = 0.sp,
            ),
            titleSmall = TextStyle(
                fontFamily = titleFontFamily,
                fontWeight = FontWeight.W700,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.sp,
            ),
            bodyLarge = TextStyle(
                fontFamily = defaultFontFamily,
                fontWeight = FontWeight.W400,
                fontSize = 15.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.15.sp,
            ),
            bodyMedium = TextStyle(
                fontFamily = defaultFontFamily,
                fontWeight = FontWeight.W400,
                fontSize = 14.sp,
                lineHeight = 22.sp,
                letterSpacing = 0.15.sp,
            ),
            bodySmall = TextStyle(
                fontFamily = defaultFontFamily,
                fontWeight = FontWeight.W400,
                fontSize = 12.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.2.sp,
            ),
            labelLarge = TextStyle(
                fontFamily = defaultFontFamily,
                fontWeight = FontWeight.W700,
                fontSize = 13.sp,
                lineHeight = 18.sp,
                letterSpacing = 0.1.sp,
            ),
            labelMedium = TextStyle(
                fontFamily = defaultFontFamily,
                fontWeight = FontWeight.W700,
                fontSize = 11.sp,
                lineHeight = 16.sp,
                letterSpacing = 0.5.sp,
            ),
            labelSmall = TextStyle(
                fontFamily = defaultFontFamily,
                fontWeight = FontWeight.W600,
                fontSize = 10.sp,
                lineHeight = 14.sp,
                letterSpacing = 1.5.sp,
            ),
        )
    }
}
