package com.escodro.designsystem.components.kuvio.chip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.kuvio.text.KuvioLabelMediumText
import com.escodro.designsystem.theme.AlkaaThemePreview

private val ChipColorOrange = Color(0xFFE07030)

/**
 * Sealed class representing the visual variant of a [KuvioTaskChip].
 */
sealed class KuvioTaskChipType {
    /** Common label property for all chip types. */
    abstract val label: String

    /** Due today — rendered with the error colour scheme. */
    data class DateToday(override val label: String) : KuvioTaskChipType()

    /** Due soon — rendered with an orange accent. */
    data class DateSoon(override val label: String) : KuvioTaskChipType()

    /** Due later — rendered with a neutral surface colour. */
    data class DateLater(override val label: String) : KuvioTaskChipType()

    /** Repeating task indicator. */
    data class Repeat(override val label: String) : KuvioTaskChipType()

    /** List membership label. */
    data class List(override val label: String) : KuvioTaskChipType()

    /** Tag label. */
    data class Tag(override val label: String) : KuvioTaskChipType()
}

private data class ChipVisuals(
    val backgroundColor: Color,
    val contentColor: Color,
    val icon: ImageVector?,
)

/**
 * Compact pill-shaped chip used inside task rows to surface key metadata.
 *
 * @param type the visual variant and label of the chip
 * @param modifier the modifier to be applied to the chip
 */
@Composable
fun KuvioTaskChip(type: KuvioTaskChipType, modifier: Modifier = Modifier) {
    val visuals = resolveVisuals(type)

    Box(
        modifier = modifier
            .height(22.dp)
            .clip(MaterialTheme.shapes.extraLarge)
            .background(visuals.backgroundColor)
            .padding(horizontal = 10.dp, vertical = 3.dp),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp),
        ) {
            if (visuals.icon != null) {
                Icon(
                    imageVector = visuals.icon,
                    contentDescription = null,
                    tint = visuals.contentColor,
                    modifier = Modifier.size(11.dp),
                )
            }
            KuvioLabelMediumText(text = type.label, color = visuals.contentColor)
        }
    }
}

@Composable
private fun resolveVisuals(type: KuvioTaskChipType): ChipVisuals = when (type) {
    is KuvioTaskChipType.DateToday -> ChipVisuals(
        backgroundColor = MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
        contentColor = MaterialTheme.colorScheme.error,
        icon = Icons.Rounded.CalendarToday,
    )

    is KuvioTaskChipType.DateSoon -> ChipVisuals(
        backgroundColor = ChipColorOrange.copy(alpha = 0.12f),
        contentColor = ChipColorOrange,
        icon = Icons.Rounded.CalendarToday,
    )

    is KuvioTaskChipType.DateLater -> ChipVisuals(
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        icon = Icons.Rounded.CalendarToday,
    )

    is KuvioTaskChipType.Repeat -> ChipVisuals(
        backgroundColor = MaterialTheme.colorScheme.surfaceVariant,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        icon = Icons.Rounded.Repeat,
    )

    is KuvioTaskChipType.List -> ChipVisuals(
        backgroundColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f),
        contentColor = MaterialTheme.colorScheme.primary,
        icon = null,
    )

    is KuvioTaskChipType.Tag -> ChipVisuals(
        backgroundColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.10f),
        contentColor = MaterialTheme.colorScheme.tertiary,
        icon = null,
    )
}

@Preview(showBackground = true)
@Composable
private fun KuvioTaskChipAllVariantsLightPreview() {
    AlkaaThemePreview {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(12.dp),
        ) {
            KuvioTaskChip(type = KuvioTaskChipType.DateToday(label = "Today"))
            KuvioTaskChip(type = KuvioTaskChipType.DateSoon(label = "Tomorrow"))
            KuvioTaskChip(type = KuvioTaskChipType.DateLater(label = "Mar 10"))
            KuvioTaskChip(type = KuvioTaskChipType.Repeat(label = "Daily"))
            KuvioTaskChip(type = KuvioTaskChipType.List(label = "Work"))
            KuvioTaskChip(type = KuvioTaskChipType.Tag(label = "urgent"))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioTaskChipAllVariantsDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(12.dp),
        ) {
            KuvioTaskChip(type = KuvioTaskChipType.DateToday(label = "Today"))
            KuvioTaskChip(type = KuvioTaskChipType.DateSoon(label = "Tomorrow"))
            KuvioTaskChip(type = KuvioTaskChipType.DateLater(label = "Mar 10"))
            KuvioTaskChip(type = KuvioTaskChipType.Repeat(label = "Daily"))
            KuvioTaskChip(type = KuvioTaskChipType.List(label = "Work"))
            KuvioTaskChip(type = KuvioTaskChipType.Tag(label = "urgent"))
        }
    }
}
