package com.escodro.splitinstall

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.escodro.core.extension.dialog
import com.escodro.core.extension.negativeButton
import com.escodro.core.extension.positiveButton
import com.escodro.core.extension.view
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import mu.KLogging

/**
 * Handles the Dynamic Features installation, abstracting the complex logic and providing visual
 * feedback.
 */
class SplitInstall(private val windowContext: Context) {

    private val manager: SplitInstallManager = SplitInstallManagerFactory.create(windowContext)

    private var featureName: String = ""

    private var onFeatureReady: () -> Unit = { /* Do nothing */ }

    private val confirmationDialog = ConfirmationDialog()

    private val loadingDialog = LoadingDialog()

    /**
     * Loads the requested feature, ensuring that it is ready to be used. If the feature is not
     * installed, it downloads from Google Play services providing visual feedback, otherwise it
     * loads the installed package.
     *
     * @param featureName the dynamic feature name
     */
    fun loadFeature(featureName: String, func: SplitInstall.() -> Unit): SplitInstall {
        this.featureName = featureName
        this.func()
        load()
        return this
    }

    /**
     * Defines the action to be taken when the feature is installed and ready to be accessed.
     */
    fun onFeatureReady(onFeatureReady: () -> Unit) {
        this.onFeatureReady = onFeatureReady
    }

    /**
     * Customizes the confirmation dialog shown to the user when prompting to download the dynamic
     * module.
     */
    fun confirmationDialog(block: ConfirmationDialog.() -> Unit) {
        this.confirmationDialog.apply(block)
    }

    /**
     * Customizes the loading dialog shown to the user when downloading the dynamic module.
     */
    fun loadingDialog(block: LoadingDialog.() -> Unit) {
        this.loadingDialog.apply(block)
    }

    private fun load() {
        if (featureName.isEmpty()) {
            throw IllegalArgumentException("Feature name not provided")
        }

        val isInstalled = isFeatureInstalled(featureName)
        logger.debug("load = [$featureName] - isInstalled = $isInstalled")

        if (isFeatureInstalled(featureName)) {
            onFeatureReady()
        } else {
            showConfirmationDialog()
        }
    }

    private fun downloadFeature() {
        logger.debug("downloadFeature = [$featureName]")

        val request = SplitInstallRequest.newBuilder()
            .addModule(featureName)
            .build()

        val dialog = getDownloadingDialog(windowContext)

        manager.registerListener {
            logger.debug("${it.status()}")

            when (it.status()) {
                SplitInstallSessionStatus.PENDING -> dialog.show()
                SplitInstallSessionStatus.INSTALLED -> onFeatureInstalled(dialog, onFeatureReady)
                else -> logger.debug("${it.status()}")
            }
        }

        manager.startInstall(request)
    }

    private fun showConfirmationDialog() = with(confirmationDialog) {
        windowContext.dialog(title, message) {
            positiveButton(R.string.split_confirmation_install_accept) { downloadFeature() }
            negativeButton(R.string.split_confirmation_install_deny) { /* Do nothing */ }
        }.show()
    }

    private fun getDownloadingDialog(windowContext: Context) = with(loadingDialog) {
        windowContext.dialog(title, message) {
            view(layout)
        }.setCancelable(false).create()
    }

    private fun onFeatureInstalled(dialog: AlertDialog, onFeatureReady: () -> Unit) {
        logger.debug("onFeatureInstalled")
        dialog.dismiss()
        onFeatureReady()
    }

    private fun isFeatureInstalled(featureName: String) =
        manager.installedModules.contains(featureName)

    companion object : KLogging()
}
