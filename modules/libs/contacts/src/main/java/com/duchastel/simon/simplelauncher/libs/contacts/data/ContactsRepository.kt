package com.duchastel.simon.simplelauncher.libs.contacts.data

import android.app.Activity
import android.net.Uri

/**
 * TODO - add comments
 */
interface ContactsRepository {
    /**
     * TODO - add comments
     */
    suspend fun pickContact(): Contact?

    /**
     * TODO - add comments
     */
    fun activityOnCreate()
}