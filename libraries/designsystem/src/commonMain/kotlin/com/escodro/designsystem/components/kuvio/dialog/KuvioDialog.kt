package com.escodro.designsystem.components.kuvio.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.kuvio.text.KuvioBodyMediumText
import com.escodro.designsystem.components.kuvio.text.KuvioHeadlineSmallText
import com.escodro.designsystem.theme.AlkaaThemePreview

/**
 * Standard and image-header dialog component following the Alkaa Design System
 *
 * The component adapts its layout based on the provided optional slots:
 * - **Image-header variant** — when [imageHeader] is supplied, a 180dp illustration area is
 *   rendered at the top of the dialog and title/body text is centred.
 * - **Icon variant** — when only [icon] is supplied (no [imageHeader]), the icon is rendered
 *   above centred title/body text. Use [KuvioDialogIconContainer] to apply the correct circular
 *   colored background defined by the design system.
 * - **Informational variant** — when neither [icon] nor [imageHeader] is provided, title and
 *   body text are left-aligned and a divider separates the body from the action row.
 *
 * @param title the dialog headline text
 * @param body the supporting description shown below the title
 * @param onDismiss called when the dialog is dismissed (e.g. back-press or scrim tap)
 * @param confirmButton primary action button composable (e.g. [Button] or [FilledTonalButton])
 * @param modifier optional modifier for the dialog container
 * @param icon optional icon composable rendered above the title; use [KuvioDialogIconContainer] to
 *   apply the design-system circular tinted background; ignored when [imageHeader] is set
 * @param imageHeader optional composable rendered inside the 180dp header area; receives
 *   [BoxScope] so callers can freely position illustration elements; takes precedence over [icon]
 * @param dismissButton optional secondary action button composable (e.g. [TextButton])
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KuvioDialog(
    title: String,
    body: String,
    onDismiss: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    imageHeader: (@Composable BoxScope.() -> Unit)? = null,
    dismissButton: (@Composable () -> Unit)? = null,
) {
    val isCentered = icon != null || imageHeader != null
    val hasIcon = icon != null && imageHeader == null

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            shadowElevation = 6.dp,
            modifier = Modifier.widthIn(min = 280.dp, max = 360.dp),
        ) {
            Column {
                if (imageHeader != null) {
                    Box(
                        modifier = Modifier.fillMaxWidth().height(180.dp),
                        contentAlignment = Alignment.Center,
                        content = imageHeader,
                    )
                }
                KuvioDialogTextContent(
                    title = title,
                    body = body,
                    isCentered = isCentered,
                    hasIcon = hasIcon,
                    icon = icon,
                )
                if (!isCentered) {
                    Spacer(modifier = Modifier.height(20.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                }
                KuvioDialogActions(
                    confirmButton = confirmButton,
                    dismissButton = dismissButton,
                )
            }
        }
    }
}

/**
 * Circular icon container for use as the [KuvioDialog.icon] slot.
 *
 * Renders a 40dp circle filled with [color] at 12 % opacity, matching the icon-container
 * specification from the Alkaa Design System.
 *
 * @param color the accent colour applied to the background tint
 * @param modifier optional modifier applied to the container
 * @param content the icon composable rendered at the centre of the circle
 */
@Composable
fun KuvioDialogIconContainer(
    color: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.12f)),
        contentAlignment = Alignment.Center,
        content = { content() },
    )
}

@Composable
private fun KuvioDialogTextContent(
    title: String,
    body: String,
    isCentered: Boolean,
    hasIcon: Boolean,
    icon: (@Composable () -> Unit)?,
) {
    Column(
        modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp),
        horizontalAlignment = if (isCentered) Alignment.CenterHorizontally else Alignment.Start,
    ) {
        if (hasIcon) {
            icon?.invoke()
            Spacer(modifier = Modifier.height(16.dp))
        }
        KuvioHeadlineSmallText(text = title, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(12.dp))
        KuvioBodyMediumText(
            text = body,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun KuvioDialogActions(
    confirmButton: @Composable () -> Unit,
    dismissButton: (@Composable () -> Unit)?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        dismissButton?.invoke()
        confirmButton()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4FA)
@Composable
private fun KuvioDialogConfirmationLightPreview() {
    AlkaaThemePreview {
        KuvioDialog(
            title = PreviewTitleConfirmation,
            body = PreviewBodyConfirmation,
            onDismiss = {},
            icon = {
                KuvioDialogIconContainer(color = MaterialTheme.colorScheme.primary) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp),
                    )
                }
            },
            confirmButton = {
                Button(onClick = {}) {
                    KuvioBodyMediumText(text = PreviewActionMarkComplete, color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = {}) { KuvioBodyMediumText(text = PreviewActionCancel) }
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioDialogConfirmationDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioDialog(
            title = PreviewTitleConfirmation,
            body = PreviewBodyConfirmation,
            onDismiss = {},
            icon = {
                KuvioDialogIconContainer(color = MaterialTheme.colorScheme.primary) {
                    Icon(
                        imageVector = Icons.Rounded.Check,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp),
                    )
                }
            },
            confirmButton = {
                Button(onClick = {}) {
                    KuvioBodyMediumText(text = PreviewActionMarkComplete, color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = {}) { KuvioBodyMediumText(text = PreviewActionCancel) }
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4FA)
@Composable
private fun KuvioDialogDestructiveLightPreview() {
    AlkaaThemePreview {
        KuvioDialog(
            title = PreviewTitleDestructive,
            body = PreviewBodyDestructive,
            onDismiss = {},
            icon = {
                KuvioDialogIconContainer(color = MaterialTheme.colorScheme.error) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(22.dp),
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    ),
                ) {
                    KuvioBodyMediumText(text = PreviewActionDelete, color = MaterialTheme.colorScheme.onError)
                }
            },
            dismissButton = {
                TextButton(onClick = {}) { KuvioBodyMediumText(text = PreviewActionCancel) }
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioDialogDestructiveDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioDialog(
            title = PreviewTitleDestructive,
            body = PreviewBodyDestructive,
            onDismiss = {},
            icon = {
                KuvioDialogIconContainer(color = MaterialTheme.colorScheme.error) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(22.dp),
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    ),
                ) {
                    KuvioBodyMediumText(text = PreviewActionDelete, color = MaterialTheme.colorScheme.onError)
                }
            },
            dismissButton = {
                TextButton(onClick = {}) { KuvioBodyMediumText(text = PreviewActionCancel) }
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4FA)
@Composable
private fun KuvioDialogInformationalLightPreview() {
    AlkaaThemePreview {
        KuvioDialog(
            title = PreviewTitleInformational,
            body = PreviewBodyInformational,
            onDismiss = {},
            confirmButton = {
                FilledTonalButton(onClick = {}) { KuvioBodyMediumText(text = PreviewActionGotIt) }
            },
            dismissButton = {
                TextButton(onClick = {}) { KuvioBodyMediumText(text = PreviewActionLearnMore) }
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioDialogInformationalDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioDialog(
            title = PreviewTitleInformational,
            body = PreviewBodyInformational,
            onDismiss = {},
            confirmButton = {
                FilledTonalButton(onClick = {}) { KuvioBodyMediumText(text = PreviewActionGotIt) }
            },
            dismissButton = {
                TextButton(onClick = {}) { KuvioBodyMediumText(text = PreviewActionLearnMore) }
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4FA)
@Composable
private fun KuvioDialogFeatureIntroLightPreview() {
    AlkaaThemePreview {
        KuvioDialog(
            title = PreviewTitleFeatureIntro,
            body = PreviewBodyFeatureIntro,
            onDismiss = {},
            imageHeader = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f),
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                            ),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(64.dp),
                    )
                }
            },
            confirmButton = {
                Button(onClick = {}) {
                    KuvioBodyMediumText(text = PreviewActionEnable, color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = {}) { KuvioBodyMediumText(text = PreviewActionNotNow) }
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioDialogFeatureIntroDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioDialog(
            title = PreviewTitleFeatureIntro,
            body = PreviewBodyFeatureIntro,
            onDismiss = {},
            imageHeader = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primaryContainer,
                                    MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.6f),
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                            ),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.AutoAwesome,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(64.dp),
                    )
                }
            },
            confirmButton = {
                Button(onClick = {}) {
                    KuvioBodyMediumText(text = PreviewActionEnable, color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = {}) { KuvioBodyMediumText(text = PreviewActionNotNow) }
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4FA)
@Composable
private fun KuvioDialogEmptyStateLightPreview() {
    AlkaaThemePreview {
        KuvioDialog(
            title = PreviewTitleEmptyState,
            body = PreviewBodyEmptyState,
            onDismiss = {},
            imageHeader = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF34C98C).copy(alpha = 0.20f),
                                    Color(0xFF1099B0).copy(alpha = 0.16f),
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                            ),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.TaskAlt,
                        contentDescription = null,
                        tint = Color(0xFF34C98C),
                        modifier = Modifier.size(64.dp),
                    )
                }
            },
            confirmButton = {
                FilledTonalButton(onClick = {}) { KuvioBodyMediumText(text = PreviewActionAddTask) }
            },
            dismissButton = {
                TextButton(onClick = {}) { KuvioBodyMediumText(text = PreviewActionViewScheduled) }
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioDialogEmptyStateDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioDialog(
            title = PreviewTitleEmptyState,
            body = PreviewBodyEmptyState,
            onDismiss = {},
            imageHeader = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF34C98C).copy(alpha = 0.20f),
                                    Color(0xFF1099B0).copy(alpha = 0.16f),
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                            ),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.TaskAlt,
                        contentDescription = null,
                        tint = Color(0xFF34C98C),
                        modifier = Modifier.size(64.dp),
                    )
                }
            },
            confirmButton = {
                FilledTonalButton(onClick = {}) { KuvioBodyMediumText(text = PreviewActionAddTask) }
            },
            dismissButton = {
                TextButton(onClick = {}) { KuvioBodyMediumText(text = PreviewActionViewScheduled) }
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF0F4FA)
@Composable
private fun KuvioDialogPermissionLightPreview() {
    AlkaaThemePreview {
        KuvioDialog(
            title = PreviewTitlePermission,
            body = PreviewBodyPermission,
            onDismiss = {},
            imageHeader = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFE07030).copy(alpha = 0.18f),
                                    Color(0xFFFFE0B2).copy(alpha = 0.16f),
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                            ),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.NotificationsActive,
                        contentDescription = null,
                        tint = Color(0xFFE07030),
                        modifier = Modifier.size(64.dp),
                    )
                }
            },
            confirmButton = {
                Button(onClick = {}) {
                    KuvioBodyMediumText(text = PreviewActionAllow, color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = {}) { KuvioBodyMediumText(text = PreviewActionSkip) }
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF0F1B2D)
@Composable
private fun KuvioDialogPermissionDarkPreview() {
    AlkaaThemePreview(isDarkTheme = true) {
        KuvioDialog(
            title = PreviewTitlePermission,
            body = PreviewBodyPermission,
            onDismiss = {},
            imageHeader = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFE07030).copy(alpha = 0.18f),
                                    Color(0xFFFFE0B2).copy(alpha = 0.16f),
                                ),
                                start = Offset(0f, 0f),
                                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                            ),
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = Icons.Rounded.NotificationsActive,
                        contentDescription = null,
                        tint = Color(0xFFE07030),
                        modifier = Modifier.size(64.dp),
                    )
                }
            },
            confirmButton = {
                Button(onClick = {}) {
                    KuvioHeadlineSmallText(text = PreviewActionAllow, color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = {}) { KuvioBodyMediumText(text = PreviewActionSkip) }
            },
        )
    }
}

private const val PreviewTitleConfirmation = "Mark as complete?"
private const val PreviewBodyConfirmation =
    "This will move \"Prepare Q3 slides\" to your completed tasks. " +
        "You can undo this from the completed list."
private const val PreviewActionMarkComplete = "Mark complete"
private const val PreviewActionCancel = "Cancel"

private const val PreviewTitleDestructive = "Delete list?"
private const val PreviewBodyDestructive =
    "Deleting \"Work\" will permanently remove all 14 tasks inside it. " +
        "This action cannot be undone."
private const val PreviewActionDelete = "Delete"

private const val PreviewTitleInformational = "What are Smart Lists?"
private const val PreviewBodyInformational =
    "Smart Lists like Today and Scheduled are automatically populated based " +
        "on due dates and priority. They update in real time as you add or complete tasks."
private const val PreviewActionGotIt = "Got it"
private const val PreviewActionLearnMore = "Learn more"

private const val PreviewTitleFeatureIntro = "Meet Smart Assist"
private const val PreviewBodyFeatureIntro =
    "Alkaa can now suggest due dates, priorities and list placements based " +
        "on your habits. Enable it once and it quietly keeps your tasks organised."
private const val PreviewActionEnable = "Enable"
private const val PreviewActionNotNow = "Not now"

private const val PreviewTitleEmptyState = "All caught up!"
private const val PreviewBodyEmptyState =
    "You have no tasks due today. Add something new, or check your upcoming " +
        "scheduled tasks to stay ahead."
private const val PreviewActionAddTask = "Add task"
private const val PreviewActionViewScheduled = "View scheduled"

private const val PreviewTitlePermission = "Enable reminders?"
private const val PreviewBodyPermission =
    "Allow Alkaa to send you notifications so you never miss a due date. " +
        "You can customise reminder timing in Settings at any time."
private const val PreviewActionAllow = "Allow"
private const val PreviewActionSkip = "Skip"
