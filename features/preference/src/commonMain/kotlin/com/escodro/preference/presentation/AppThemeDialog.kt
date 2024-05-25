package com.escodro.preference.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.escodro.preference.model.AppThemeOptions
import com.escodro.resources.Res
import com.escodro.resources.preference_title_app_theme
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun AppThemeDialog(
    isDialogOpen: Boolean,
    onDismissRequest: () -> Unit,
    currentTheme: AppThemeOptions,
    onThemeUpdate: (AppThemeOptions) -> Unit,
) {
    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            title = { Text(text = stringResource(Res.string.preference_title_app_theme)) },
            text = {
                Column {
                    AppThemeOptions.entries.forEach { item ->
                        val isSelected = currentTheme.id == item.id

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .padding(8.dp)
                                .selectable(
                                    selected = isSelected,
                                    onClick = {
                                        onThemeUpdate(item)
                                        onDismissRequest()
                                    },
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    onThemeUpdate(item)
                                    onDismissRequest()
                                },
                            )
                            Text(
                                text = stringResource(item.titleRes),
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(start = 16.dp),
                            )
                        }
                    }
                }
            },
            confirmButton = { /* Shows nothing */ },
            dismissButton = { /* Shows nothing */ },
        )
    }
}
