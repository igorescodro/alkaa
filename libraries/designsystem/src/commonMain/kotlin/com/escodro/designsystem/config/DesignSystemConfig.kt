package com.escodro.designsystem.config

/**
 * Temporary local feature config to be used while the migration to the new design system is being
 * implemented.
 *
 * The value is always `false` for production builds and will be manually set to `true` during
 * development.
 */
object DesignSystemConfig {

    /**
     * Flag to enable the new design system.
     */
    const val IsNewDesignEnabled = false
}
