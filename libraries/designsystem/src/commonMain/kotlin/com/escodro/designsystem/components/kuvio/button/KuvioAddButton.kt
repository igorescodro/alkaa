package com.escodro.designsystem.components.kuvio.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.theme.AlkaaThemePreview
import com.escodro.resources.Res
import com.escodro.resources.kuvio_add_button_cd
import org.jetbrains.compose.resources.stringResource

/**
 * A circular "add" button.
 *
 * @param onClick callback invoked when the button is tapped.
 * @param modifier modifier to be applied to the button container.
 */
@Composable
fun KuvioAddButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = stringResource(Res.string.kuvio_add_button_cd),
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.padding(all = 4.dp).fillMaxSize(),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KuvioAddButtonLightPreview() {
    AlkaaThemePreview {
        KuvioAddButton(
            modifier = Modifier.size(32.dp),
            onClick = {},
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioAddButtonDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioAddButton(
            onClick = {},
            modifier = Modifier.size(32.dp),
        )
    }
}
