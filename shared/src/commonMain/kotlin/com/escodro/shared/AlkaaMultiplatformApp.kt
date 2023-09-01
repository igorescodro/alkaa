package com.escodro.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.escodro.designsystem.AlkaaTheme

@Composable
fun AlkaaMultiplatformApp() {
    AlkaaTheme {
        HelloWorld()
    }
}

@Composable
private fun HelloWorld() {
    Box(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Hello World!", color = MaterialTheme.colorScheme.onBackground)
    }
}
