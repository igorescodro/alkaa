package com.escodro.designsystem.components.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Task
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.theme.AlkaaThemePreview

/**
 * Default content containing a icon and a text showing some full screen information.
 * Component usually used for error, info or empty list screens.
 *
 * @param icon icon to be shown
 * @param iconContentDescription the icon content description
 * @param header the text header to be shown
 * @param modifier modifier to be set
 */
@Composable
fun DefaultIconTextContent(
    icon: ImageVector,
    iconContentDescription: String,
    header: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconContentDescription,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = header,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DefaultIconTextContentPreview(
    @PreviewParameter(DefaultIconTextContentPreviewParameterProvider::class) parameter: Pair<String, ImageVector>,
) {
    AlkaaThemePreview {
        DefaultIconTextContent(
            icon = parameter.second,
            iconContentDescription = "Icon representing ${parameter.first}",
            header = parameter.first,
            modifier = Modifier.size(400.dp),
        )
    }
}

private class DefaultIconTextContentPreviewParameterProvider :
    PreviewParameterProvider<Pair<String, ImageVector>> {
    override val values: Sequence<Pair<String, ImageVector>> = sequenceOf(
        Pair("No tasks yet", Icons.Outlined.Task),
        Pair("Nothing to see here", Icons.Outlined.Info),
        Pair("Nothing here, yet", Icons.Outlined.Error),
    )
}
