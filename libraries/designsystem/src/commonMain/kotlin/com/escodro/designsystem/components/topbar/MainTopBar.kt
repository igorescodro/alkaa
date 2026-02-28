package com.escodro.designsystem.components.topbar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.escodro.designsystem.theme.AlkaaThemePreview

/**
 * Main Top Bar component used across the app.
 *
 * @param title the title to be shown in the Top Bar
 * @param modifier modifier to be applied to the Top Bar
 * @param windowInsets the window insets to be applied to the Top Bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(
    title: String,
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
) {
    CenterAlignedTopAppBar(
        modifier = modifier,
        windowInsets = windowInsets,
        title = {
            Text(
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Light),
                text = title,
                color = MaterialTheme.colorScheme.tertiary,
            )
        },
    )
}

@Preview
@Composable
private fun AlkaaTopBarPreview() {
    AlkaaThemePreview {
        MainTopBar(title = "Home Sweet Home")
    }
}
