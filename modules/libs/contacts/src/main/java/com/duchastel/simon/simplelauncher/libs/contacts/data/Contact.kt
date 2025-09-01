package com.duchastel.simon.simplelauncher.libs.contacts.data

/**
 * Represents contact information retrieved from the system contacts database.
 *
 * @property id Unique contact identifier
 * @property phoneNumber Primary phone number associated with the contact
 */
data class Contact(
    val id: String,
    val phoneNumber: String
)