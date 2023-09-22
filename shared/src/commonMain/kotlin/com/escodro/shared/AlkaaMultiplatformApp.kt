package com.escodro.shared

import androidx.compose.runtime.Composable
import com.escodro.designsystem.AlkaaTheme
import com.escodro.shared.navigation.AlkaaNavGraph

@Composable
fun AlkaaMultiplatformApp() {
    AlkaaTheme {
        AlkaaNavGraph()
    }
}
