package com.escodro.preference.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.AmbientContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.core.extension.getVersionName
import com.escodro.preference.R
import com.escodro.theme.AlkaaTheme

/**
 * Alkaa Preference Section.
 *
 * @param modifier the decorator
 * @param onAboutClicked function to be called when the about item is clicked
 */
@Composable
fun PreferenceSection(modifier: Modifier = Modifier, onAboutClicked: () -> Unit) {
    Column(modifier = modifier.fillMaxSize()) {
        AboutItem(onAboutClicked)
        VersionItem()
    }
}

@Composable
private fun VersionItem() {
    val title = stringResource(id = R.string.preference_title_version)
    val version = AmbientContext.current.getVersionName()
    PreferenceItem(title = title, version)
}

@Composable
private fun AboutItem(onAboutClicked: () -> Unit) {
    PreferenceItem(
        title = stringResource(id = R.string.preference_title_about),
        onItemClicked = onAboutClicked
    )
}

@Composable
private fun PreferenceItem(
    title: String,
    description: String? = null,
    onItemClicked: () -> Unit = { }
) {
    Column(
        modifier = Modifier
            .clickable { onItemClicked() }
            .padding(start = 32.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
            .fillMaxWidth()
            .preferredHeight(48.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body1
        )
        if (description != null) {
            Text(
                text = description,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun PreferencePreview() {
    AlkaaTheme {
        PreferenceSection(onAboutClicked = {})
    }
}
