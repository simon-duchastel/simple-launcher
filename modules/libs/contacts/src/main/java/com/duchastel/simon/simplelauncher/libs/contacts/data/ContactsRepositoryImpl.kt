package com.duchastel.simon.simplelauncher.libs.contacts.data

import android.app.Activity
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.VisibleForTesting
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@ActivityScoped
class ContactsRepositoryImpl @Inject internal constructor(
    private val activity: Activity,
) : ContactsRepository {

    private lateinit var pickContactLauncher: ActivityResultLauncher<Void?>
    private val contactResultFlow = MutableStateFlow<Contact?>(null)

    override fun activityOnCreate() {
        pickContactLauncher = (activity as ComponentActivity).registerForActivityResult(
            ActivityResultContracts.PickContact()
        ) { uri ->
            val contact = if (uri != null) {
                resolveContact(uri)
            } else {
                null
            }
            contactResultFlow.update { contact }
        }
    }

    override suspend fun pickContact(): Contact? {
        pickContactLauncher.launch(null)
        return contactResultFlow.drop(1).first()
    }

    @VisibleForTesting
    fun resolveContact(uri: Uri): Contact? {
        val contactCursor = activity.contentResolver.query(
            uri,
            arrayOf(ContactsContract.Contacts._ID),
            null,
            null,
            null
        )

        if (contactCursor?.moveToFirst() == true) {
            val contactId = contactCursor.getString(
                contactCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID)
            )
            contactCursor.close()

            val phoneCursor = activity.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                "${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?",
                arrayOf(contactId),
                null
            )

            if (phoneCursor?.moveToFirst() == true) {
                val numberIndex = phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)
                val number = phoneCursor.getString(numberIndex)
                phoneCursor.close()
                return Contact(contactId, number)
            }
            phoneCursor?.close()
        } else {
            contactCursor?.close()
        }
        
        return null
    }
}