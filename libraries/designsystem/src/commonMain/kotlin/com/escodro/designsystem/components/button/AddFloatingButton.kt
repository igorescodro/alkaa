package com.escodro.designsystem.components.button

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.theme.AlkaaThemePreview

/**
 * Floating Action button do add new elements.
 *
 * @param contentDescription string resource to describe the add button
 * @param modifier Compose modifier
 * @param onClick function to be called on the click
 */
@Composable
fun AddFloatingButton(
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = contentDescription,
        )
    }
}

@Preview
@Composable
private fun AddFloatingButtonPreview() {
    AlkaaThemePreview {
        AddFloatingButton(
            contentDescription = "Add Item",
            onClick = {},
            modifier = Modifier.size(56.dp),
        )
    }
}
