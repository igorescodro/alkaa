package com.escodro.preference.presentation

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.escodro.designsystem.components.AlkaaToolbar
import com.escodro.preference.provider.BrowserProvider
import com.escodro.resources.MR
import dev.icerock.moko.resources.compose.stringResource
import org.koin.compose.koinInject

/**
 * Alkaa about screen.
 */
@Composable
fun About(onUpPress: () -> Unit, modifier: Modifier = Modifier) {
    Scaffold(
        topBar = { AlkaaToolbar(onUpPress = onUpPress) },
        content = { paddingValues -> AboutContent(modifier = Modifier.padding(paddingValues)) },
        modifier = modifier,
    )
}

@Composable
private fun AboutContent(modifier: Modifier = Modifier) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        ContentHeader()
        Text(
            text = stringResource(MR.strings.about_description),
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 32.sp,
            modifier = Modifier.padding(16.dp),
        )
        ContentCallToAction()
    }
}

@Composable
private fun ContentHeader() {
    val infiniteTransition = rememberInfiniteTransition()
    val color by infiniteTransition.animateColor(
        initialValue = MaterialTheme.colorScheme.primary,
        targetValue = MaterialTheme.colorScheme.tertiary,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10_000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse,
        ),
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(color = color),
    ) {
        val appName = stringResource(MR.strings.about_title).lowercase()
        Text(
            text = appName,
            style = MaterialTheme.typography.displayLarge.copy(
                color = MaterialTheme.colorScheme.surface,
            ),
        )
    }
}

@Composable
private fun ContentCallToAction(
    browserProvider: BrowserProvider = koinInject(),
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
    ) {
        Button(onClick = {
            browserProvider.openUrl(ProjectUrl)
        }) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = stringResource(MR.strings.about_cd_github),
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = stringResource(MR.strings.about_button_project))
        }
    }
}

private const val ProjectUrl = "https://github.com/igorescodro/alkaa"
