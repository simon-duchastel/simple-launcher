package com.duchastel.simon.simplelauncher.libs.permissions.data

import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class PermissionsRepositoryImpl @Inject internal constructor(
    private val activity: Activity,
) : PermissionsRepository {

    private val permissionsFlow: MutableStateFlow<Map<Permission, Boolean>> = MutableStateFlow(mapOf())

    /**
     * [activity] is required to be a [ComponentActivity]. Throw if it isn't.
     */
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun activityOnCreate() {
        requestPermissionLauncher = (activity as ComponentActivity).registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { isGranted: Map<String, Boolean> ->
            permissionsFlow.update { currentPermissions ->
                currentPermissions.toMutableMap().apply {
                    isGranted.forEach { (permission, isGranted) ->
                        val typedPermission = permission.asPermission()
                        if (typedPermission != null) {
                            put(typedPermission, isGranted)
                        }
                    }
                }
            }
        }
    }

    override suspend fun requestPermission(permission: Permission): Boolean {
        val permissionStatus = ContextCompat.checkSelfPermission(
            activity, // context
            permission.asManifestPermission() // permission
        )
        if (permissionStatus == PackageManager.PERMISSION_GRANTED) {
            permissionsFlow.update { currentPermissions ->
                currentPermissions + (permission to true)
            }
            return true
        } else {
            requestPermissionLauncher.launch(arrayOf(permission.asManifestPermission()))
            return permissionsFlow.drop(1).first()[permission] == true
        }
    }

    private fun Permission.asManifestPermission(): String =
        when (this) {
            Permission.SEND_SMS -> android.Manifest.permission.SEND_SMS
            Permission.READ_CONTACTS -> android.Manifest.permission.READ_CONTACTS
        }

    private fun String.asPermission(): Permission? =
        when (this) {
            android.Manifest.permission.SEND_SMS -> Permission.SEND_SMS
            android.Manifest.permission.READ_CONTACTS -> Permission.READ_CONTACTS
            else -> null
        }
}
