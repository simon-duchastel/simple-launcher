package com.duchastel.simon.simplelauncher.libs.permissions.data

import android.app.Activity
import androidx.activity.result.contract.ActivityResultContract

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

    /**
     * Callback triggered by an [Activity] in its onCreate method. Intended to be called ONLY by an
     * [Activity] and this MUST be called in its onCreate method since the repository must register
     * its [ActivityResultContract] in onCreate (ie. before RESUME).
     */
    fun activityOnCreate()
}
