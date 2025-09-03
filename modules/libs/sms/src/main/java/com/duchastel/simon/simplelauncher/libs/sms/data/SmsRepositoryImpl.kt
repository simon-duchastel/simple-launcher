package com.duchastel.simon.simplelauncher.libs.sms.data

import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SmsManager
import com.duchastel.simon.simplelauncher.libs.permissions.data.Permission
import com.duchastel.simon.simplelauncher.libs.permissions.data.PermissionsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class SmsRepositoryImpl @Inject internal constructor(
    @param:ApplicationContext private val context: Context,
    private val permissionRepository: PermissionsRepository,
    private val smsBroadcastReceiverFactory: SmsBroadcastReceiverFactory,
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
            val sentIntent = smsBroadcastReceiverFactory.createSentSmsPendingIntent(
                sentAction,
                messageId,
                requestCode,
            )

            val receiver = smsBroadcastReceiverFactory.createSentSmsBroadcastReceiver(
                messageId = messageId,
                onSentSmsReceived = { successfullySent, broadcastReceiver ->
                    context.unregisterReceiver(broadcastReceiver)
                    cont.resume(successfullySent)
                }
            )
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
                runCatching {
                    // swallow the exception since it means the receiver wasn't registered anyways
                    context.unregisterReceiver(receiver)
                }
            }
        }
    }
}

