package com.duchastel.simon.simplelauncher.libs.permissions.data

/**
 * Manages requesting permissions from the user.
 */
interface PermissionsRepository {
    /**
     * Requests the given [permission] from the user. Suspends until the permission is either
     * granted or denied. Note that this might not result in a prompt to the user, such as if the
     * permission is already granted.
     *
     * @param permission the permission to be requested
     * @return true if the permission was granted, false otherwise
     */
    suspend fun requestPermission(permission: Permission): Boolean
}
