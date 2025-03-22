package com.escodro.permission.api

/**
 * Permission controller interface.
 */
interface PermissionController {

    /**
     * Controller responsible for handling the permission request and check.
     */
    val controller: Any

    /**
     * Request the permission.
     *
     * @param permission permission to be requested
     */
    suspend fun requestPermission(permission: Permission)

    /**
     * Check if the permission is granted.
     *
     * @param permission permission to be checked
     *
     * @return true if the permission is granted, false otherwise
     */
    suspend fun isPermissionGranted(permission: Permission): Boolean
}
