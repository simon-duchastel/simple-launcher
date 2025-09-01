package com.duchastel.simon.simplelauncher.libs.contacts.data

/**
 * Repository for managing contact selection and data retrieval from the Android contacts system.
 */
interface ContactsRepository {
    /**
     * Launches the system contact picker and returns the selected contact information.
     *
     * This function suspends until the user selects a contact or cancels the picker.
     * Requires READ_CONTACTS permission to be granted before calling.
     *
     * @return [Contact] if a contact was selected, null if the user cancelled the picker or
     * contact has no phone number
     */
    suspend fun pickContact(): Contact?

    /**
     * Initializes the contact picker activity result launcher.
     *
     * MUST be called during the activity's onCreate() method, before the activity
     * reaches the RESUMED state. This is required by the ActivityResultContract system.
     */
    fun activityOnCreate()
}