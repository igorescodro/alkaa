package com.escodro.preference.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.escodro.core.extension.openUrl
import com.escodro.preference.R
import com.escodro.theme.AlkaaTheme
import com.escodro.theme.components.AlkaaToolbar
import java.util.Locale

/**
 * Alkaa about screen.
 */
@Composable
fun About(onUpPressed: () -> Unit) {
    Scaffold(
        topBar = { AlkaaToolbar(onUpPressed = onUpPressed) },
        content = { AboutContent() }
    )
}

@Composable
private fun AboutContent() {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        ContentHeader()
        Text(
            text = stringResource(id = R.string.about_description),
            style = MaterialTheme.typography.body1,
            lineHeight = 32.sp,
            modifier = Modifier.padding(16.dp)
        )
        ContentCallToAction()
    }
}

@Composable
private fun ContentHeader() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(color = MaterialTheme.colors.primary)
    ) {
        val appName = stringResource(id = R.string.app_name).toLowerCase(Locale.getDefault())
        Text(
            text = appName,
            style = MaterialTheme.typography.h1.copy(color = MaterialTheme.colors.surface)
        )
    }
}

@Composable
private fun ContentCallToAction() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        val context = LocalContext.current
        Button(onClick = { context.openUrl(PROJECT_URL) }) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = stringResource(id = R.string.about_cd_github)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = stringResource(id = R.string.about_button_project))
        }
    }
}

private const val PROJECT_URL = "https://github.com/igorescodro/alkaa"

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun AboutPreview() {
    AlkaaTheme {
        About(onUpPressed = { })
    }
}
