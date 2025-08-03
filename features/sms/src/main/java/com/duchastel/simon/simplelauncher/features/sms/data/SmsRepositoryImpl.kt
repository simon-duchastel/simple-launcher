package com.duchastel.simon.simplelauncher.features.sms.data

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SmsManager
import com.duchastel.simon.simplelauncher.features.permissions.data.Permission
import com.duchastel.simon.simplelauncher.features.permissions.data.PermissionsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

import javax.inject.Inject
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class SmsRepositoryImpl @Inject internal constructor(
    @ApplicationContext private val context: Context,
    private val permissionRepository: PermissionsRepository,
): SmsRepository {

    private val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)
    private val packageManager = context.packageManager

    @OptIn(ExperimentalTime::class)
    override suspend fun sendSms(phoneNumber: String, message: String): Boolean {
        val result: Boolean = permissionRepository.requestPermission(Permission.SEND_SMS)
        if (!result) {
            return false
        }

        if (!packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            return false
        }

        return suspendCancellableCoroutine { cont ->
            val sentAction =
                "SMS_SENT_ACTION_${Clock.System.now().epochSeconds}_${phoneNumber.hashCode()}"
            val requestCode = sentAction.hashCode()
            val messageId = requestCode.toString()
            val sentIntent = PendingIntent.getBroadcast(
                context,
                requestCode,
                Intent(sentAction).putExtra("messageId", messageId),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

            val receiver = object : BroadcastReceiver() {
                override fun onReceive(ctx: Context?, intent: Intent?) {
                    if (intent?.extras?.getString("messageId") != messageId) return

                    val successful = resultCode == Activity.RESULT_OK
                    context.unregisterReceiver(this)
                    cont.resume(successful)
                }
            }
            val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Context.RECEIVER_EXPORTED
            } else {
                0
            }
            context.registerReceiver(
                receiver,
                IntentFilter(sentAction),
                flags,
            )
            smsManager.sendTextMessage(
                phoneNumber, // destinationAddress
                null,        // scAddress
                message,     // text
                sentIntent,  // sentIntent
                null         // deliveryIntent
            )
            cont.invokeOnCancellation {
                try {
                    context.unregisterReceiver(receiver)
                } catch (_: Exception) {
                    // swallow the exception since it means the receiver wasn't registered
                }
            }
        }
    }
}

