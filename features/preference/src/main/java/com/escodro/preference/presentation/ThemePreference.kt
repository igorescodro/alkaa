package com.escodro.preference.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.escodro.preference.R
import com.escodro.preference.model.AppThemeOptions

@Composable
internal fun AppThemeDialog(
    isDialogOpen: Boolean,
    onDismissRequest: () -> Unit,
    currentTheme: AppThemeOptions,
    onThemeUpdate: (AppThemeOptions) -> Unit
) {
    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = { /* Does not close */ },
            title = { Text(text = stringResource(id = R.string.preference_title_app_theme)) },
            text = {
                Column {
                    AppThemeOptions.values().forEach { item ->
                        val isSelected = currentTheme.id == item.id

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .selectable(
                                    selected = isSelected,
                                    onClick = {
                                        onThemeUpdate(item)
                                        onDismissRequest()
                                    }
                                )
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = {
                                    onThemeUpdate(item)
                                    onDismissRequest()
                                }
                            )
                            Text(
                                text = stringResource(id = item.titleRes),
                                style = MaterialTheme.typography.body2,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            },
            confirmButton = { /* Shows nothing */ },
            dismissButton = { /* Shows nothing */ }
        )
    }
}
