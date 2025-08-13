package com.duchastel.simon.simplelauncher.libs.sms.data

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import android.app.PendingIntent

class SmsBroadcastReceiverFactoryImpl @Inject internal constructor(
    @ApplicationContext private val context: Context,
) : SmsBroadcastReceiverFactory {

    override fun createSentSmsBroadcastReceiver(
        messageId: String,
        onSentSmsReceived: (successfullySent: Boolean, broadcastReceiver: BroadcastReceiver) -> Unit,
    ): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                if (intent?.extras?.getString(EXTRA_MESSAGE_ID) != messageId) return

                val successful = resultCode == Activity.RESULT_OK
                onSentSmsReceived(successful, this)
            }
        }
    }

    override fun createDeliveredSmsBroadcastReceiver(
        messageId: String,
        onDeliveredSmsReceived: (successfullyDelivered: Boolean, broadcastReceiver: BroadcastReceiver) -> Unit,
    ): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                if (intent?.extras?.getString(EXTRA_MESSAGE_ID) != messageId) return

                val successful = resultCode == Activity.RESULT_OK
                onDeliveredSmsReceived(successful, this)
            }
        }
    }

    override fun createSentSmsPendingIntent(
        sentAction: String,
        messageId: String,
        requestCode: Int,
    ): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            requestCode,
            Intent(sentAction).putExtra(EXTRA_MESSAGE_ID, messageId),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        const val EXTRA_MESSAGE_ID = "messageId"
    }
}
