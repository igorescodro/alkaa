package com.escodro.task.presentation.detail

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

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

@Composable
internal fun TaskDetailSectionContent(
    imageVector: ImageVector,
    @StringRes contentDescription: Int,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LeadingIcon(
            imageVector = imageVector,
            contentDescription = contentDescription
        )
        Box(modifier = Modifier.padding(start = 16.dp)) {
            content()
        }
    }
}

private const val TrailingLeadingAlpha = 0.54f
