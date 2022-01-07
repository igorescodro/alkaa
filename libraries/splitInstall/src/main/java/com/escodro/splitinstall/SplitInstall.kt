package com.escodro.splitinstall

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.escodro.designsystem.components.AlkaaDialog
import com.escodro.designsystem.components.DialogArguments
import com.escodro.splitinstall.SplitInstallState.Downloading
import com.escodro.splitinstall.SplitInstallState.FeatureReady
import com.escodro.splitinstall.SplitInstallState.RequestDownload
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import logcat.logcat

/**
 * Composable to handle the all the operations to open a Dynamic Feature Module. This Composable
 * handles the user request, download and notifies the caller when the feature is ready to be
 * opened. If the feature is already installed, it will open the feature right away.
 *
 * There is a workaround in this code because the Jetpack Navigation Compose does not support
 * Dynamic Feature properly. Once Alkaa needs to open a new Activity, the Composable/Dialog needed
 * to do so needs to be manually removed from the stack. Hopefully this hack will not be needed
 * when the proper support is launched.
 *
 * @param context the application context
 * @param featureName the dynamic feature name
 * @param onDismiss the action to dismiss the current composable
 * @param onFeatureReady function called when the feature is ready to be opened
 */
@Composable
fun LoadFeature(
    context: Context,
    featureName: String,
    onDismiss: () -> Unit,
    onFeatureReady: () -> Unit
) {
    if (featureName.isEmpty()) {
        throw IllegalArgumentException("Feature name not provided")
    }

    val manager = remember(featureName) { SplitInstallManagerFactory.create(context) }
    val isFeatureReady =
        remember(featureName) { isFeatureInstalled(manager = manager, featureName = featureName) }
    val initialState =
        remember(featureName) { if (isFeatureReady) FeatureReady else RequestDownload }
    var state by rememberSaveable(featureName) { mutableStateOf(initialState) }

    when (state) {
        RequestDownload -> RequestDownload(onDismiss = onDismiss, setState = { state = it })
        Downloading -> DownloadFeature(
            featureName = featureName,
            manager = manager,
            onDismiss = onDismiss,
            setState = { state = it }
        )
        FeatureReady -> {
            onDismiss()
            onFeatureReady()
        }
    }
}

private fun isFeatureInstalled(
    manager: SplitInstallManager,
    featureName: String,
): Boolean {
    val isFeatureInstalled = manager.installedModules.contains(featureName)
    logcat(TAG) { "load = [$featureName] - isFeatureInstalled = $isFeatureInstalled" }

    return isFeatureInstalled
}

@Composable
private fun RequestDownload(setState: (SplitInstallState) -> Unit, onDismiss: () -> Unit) {
    var isDialogOpen by rememberSaveable { mutableStateOf(true) }

    val arguments = DialogArguments(
        title = stringResource(id = R.string.split_confirmation_install_title),
        text = stringResource(id = R.string.split_confirmation_install_description),
        confirmText = stringResource(id = R.string.split_confirmation_install_accept),
        dismissText = stringResource(id = R.string.split_confirmation_install_deny),
        onConfirmAction = { setState(Downloading) }
    )
    AlkaaDialog(
        arguments = arguments,
        isDialogOpen = isDialogOpen,
        onDismissRequest = {
            isDialogOpen = false
            onDismiss()
        }
    )
}

@Composable
private fun DownloadFeature(
    featureName: String,
    manager: SplitInstallManager,
    onDismiss: () -> Unit,
    setState: (SplitInstallState) -> Unit
) {
    var isDialogOpen by rememberSaveable { mutableStateOf(true) }
    DisposableEffect(featureName) {
        val request = SplitInstallRequest.newBuilder()
            .addModule(featureName)
            .build()

        val listener = SplitInstallStateUpdatedListener {
            logcat { "${it.status()}" }

            when (it.status()) {
                SplitInstallSessionStatus.PENDING -> isDialogOpen = true
                SplitInstallSessionStatus.INSTALLED -> {
                    isDialogOpen = false
                    setState(FeatureReady)
                    onDismiss()
                }
                else -> logcat { "${it.status()}" }
            }
        }

        manager.registerListener(listener)
        manager.startInstall(request)

        onDispose { manager.unregisterListener(listener) }
    }

    if (isDialogOpen) {
        DownloadDialog()
    }
}

@Composable
private fun DownloadDialog() {
    AlertDialog(
        onDismissRequest = { /* Does not close */ },
        title = { Text(text = stringResource(id = R.string.split_downloading_title)) },
        text = {
            Column {
                Text(text = stringResource(id = R.string.split_downloading_description))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(64.dp))
                }
            }
        },
        confirmButton = { /* Shows nothing */ },
        dismissButton = { /* Shows nothing */ }
    )
}

private const val TAG = "SplitInstall"
