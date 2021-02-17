package com.escodro.task.presentation.detail

import androidx.annotation.StringRes
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@Composable
internal fun LeadingIcon(
    imageVector: ImageVector,
    @StringRes contentDescription: Int,
    modifier: Modifier = Modifier,
) {
    Icon(
        imageVector = imageVector,
        contentDescription = stringResource(id = contentDescription),
        tint = MaterialTheme.colors.onSurface.copy(alpha = TrailingLeadingAlpha),
        modifier = modifier
    )
}

private const val TrailingLeadingAlpha = 0.54f
