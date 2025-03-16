package com.escodro.designsystem.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * TopAppBar for screens that need a back button.
 *
 * @param isCompact flag to indicate if the screen is in compact mode - if true it shows the "back"
 * button, otherwise it shows the "close" button
 * @param onUpPress function to be called when the back/up button is clicked
 * @param modifier Compose modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlkaaToolbar(
    isCompact: Boolean,
    onUpPress: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val iconType = if (isCompact) IconType.Back else IconType.Close

    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onUpPress) {
                Icon(
                    imageVector = iconType.imageVector,
                    contentDescription = iconType.contentDescription,
                )
            }
        },
        modifier = modifier,
    )
}

enum class IconType(
    val imageVector: ImageVector,
    val contentDescription: String?,
) {
    Back(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back"),
    Close(imageVector = Icons.Rounded.Close, contentDescription = "Close"),
}
