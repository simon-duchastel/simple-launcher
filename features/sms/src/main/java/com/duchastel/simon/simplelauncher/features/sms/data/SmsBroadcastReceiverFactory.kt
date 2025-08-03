package com.duchastel.simon.simplelauncher.features.sms.data

import android.app.PendingIntent
import android.content.BroadcastReceiver

/**
 *
 */

/**
 *
 */
interface SmsBroadcastReceiverFactory {
    /**
     *
     */
    fun createSentSmsBroadcastReceiver(
        messageId: String,
        onSentSmsReceived: (successfullySent: Boolean, broadcastReceiver: BroadcastReceiver) -> Unit,
    ): BroadcastReceiver

    /**
     *
     */
    fun createSentSmsPendingIntent(
        sentAction: String,
        messageId: String,
        requestCode: Int,
    ): PendingIntent
}
