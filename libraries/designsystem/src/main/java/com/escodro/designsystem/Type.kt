package com.escodro.designsystem

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Default application typography.
 */
val typography = Typography(
    h5 = TextStyle(
        fontWeight = FontWeight.Thin,
        fontSize = 24.sp
    ),
    h6 = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp
    )
)
