package com.escodro.splitinstall

import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import timber.log.Timber

/**
 * Handles the Dynamic Features installation, abstracting the complex logic and providing visual
 * feedback.
 */
class SplitInstall(private val manager: SplitInstallManager) {

    /**
     * Loads the requested feature, ensuring that it is ready to be used. If the feature is not
     * installed, it downloads from Google Play services providing visual feedback, otherwise it
     * loads the installed package.
     *
     * @param featureName the dynamic feature name
     * @param onFeatureReady HFO to be called when the dynamic feature is ready to be used
     */
    fun loadFeature(featureName: String, onFeatureReady: () -> Unit) {
        val isInstalled = isFeatureInstalled(featureName)
        Timber.d("loadFeature = [$featureName] - isInstalled = $isInstalled")

        if (isFeatureInstalled(featureName)) {
            onFeatureReady()
        } else {
            downloadFeature(featureName, onFeatureReady)
        }
    }

    private fun downloadFeature(featureName: String, onFeatureReady: () -> Unit) {
        Timber.d("downloadFeature = [$featureName]")

        val request = SplitInstallRequest.newBuilder()
            .addModule(featureName)
            .build()

        manager.registerListener {
            Timber.d("${it.status()}")

            when (it.status()) {
                SplitInstallSessionStatus.INSTALLED -> onFeatureReady()
            }
        }

        manager.startInstall(request)
    }

    private fun isFeatureInstalled(featureName: String) =
        manager.installedModules.contains(featureName)
}
