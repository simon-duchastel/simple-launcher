package com.duchastel.simon.simplelauncher.libs.sms.data

import android.app.PendingIntent
import android.content.BroadcastReceiver

/**
 * Factory for creating SMS broadcast receivers and the corresponding pending intents.
 */
interface SmsBroadcastReceiverFactory {
    /**
     * Creates a [BroadcastReceiver] for handling SMS sent events.
     *
     * @param messageId The unique ID of the sent message.
     * @param onSentSmsReceived A callback function to be invoked when the SMS sent broadcast is
     * received. Provides a boolean indicating successful sending and the broadcast receiver
     * itself (useful for unregistering itself).
     * @return The [BroadcastReceiver] instance which received the notification.
     */
    fun createSentSmsBroadcastReceiver(
        messageId: String,
        onSentSmsReceived: (successfullySent: Boolean, broadcastReceiver: BroadcastReceiver) -> Unit,
    ): BroadcastReceiver

    /**
     * Creates a [BroadcastReceiver] for handling SMS sent events.
     *
     * @param messageId The unique ID of the sent message.
     * @param onDeliveredSmsReceived A callback function to be invoked when the SMS delivered
     * broadcast is received. Provides a boolean indicating successful delivery and the broadcast
     * receiver itself (useful for unregistering itself).
     * @return The [BroadcastReceiver] instance which received the notification.
     */
    fun createDeliveredSmsBroadcastReceiver(
        messageId: String,
        onDeliveredSmsReceived: (successfullyDelivered: Boolean, broadcastReceiver: BroadcastReceiver) -> Unit,
    ): BroadcastReceiver

    /**
     * Creates a [PendingIntent] for the SMS sent broadcast.
     *
     * @param sentAction The action string for the sent SMS broadcast, used as the
     * [android.content.Intent] action.
     * @param messageId The unique ID of the sent message.
     * @param requestCode A private request code for the sender. Must be globally unique.
     */
    fun createSentSmsPendingIntent(
        sentAction: String,
        messageId: String,
        requestCode: Int,
    ): PendingIntent
}
