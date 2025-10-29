package com.escodro.task.presentation.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.icon.LeadingIcon

@Composable
internal fun TaskDetailSectionContent(
    imageVector: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LeadingIcon(
            imageVector = imageVector,
            contentDescription = contentDescription,
        )
        Box(modifier = Modifier.padding(start = 16.dp)) {
            content()
        }
    }
}
