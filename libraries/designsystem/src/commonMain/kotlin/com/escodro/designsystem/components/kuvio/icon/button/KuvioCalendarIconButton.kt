package com.escodro.designsystem.components.kuvio.icon.button

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.escodro.designsystem.theme.AlkaaThemePreview
import com.escodro.resources.Res
import com.escodro.resources.kuvio_calendar_icon_cd
import org.jetbrains.compose.resources.stringResource

/**
 * Action icon for picking a due date inside a focused
 * [com.escodro.designsystem.components.kuvio.bar.KuvioAddTaskBar].
 *
 * Renders a 32 dp circular touch target with a calendar icon tinted in
 * [MaterialTheme.colorScheme.onSurfaceVariant].
 *
 * @param onClick callback invoked when the icon is tapped.
 * @param modifier modifier to be applied to the icon button.
 */
@Composable
fun KuvioCalendarIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.clip(CircleShape),
    ) {
        Icon(
            imageVector = Icons.Outlined.DateRange,
            contentDescription = stringResource(Res.string.kuvio_calendar_icon_cd),
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun KuvioCalendarIconLightPreview() {
    AlkaaThemePreview {
        KuvioCalendarIcon(onClick = {})
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioCalendarIconDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioCalendarIcon(onClick = {})
    }
}
