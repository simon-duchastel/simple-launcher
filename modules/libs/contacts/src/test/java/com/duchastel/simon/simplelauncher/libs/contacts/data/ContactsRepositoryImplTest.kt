package com.duchastel.simon.simplelauncher.libs.contacts.data

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ContactsRepositoryImplTest {

    private lateinit var activity: ComponentActivity
    private lateinit var contentResolver: ContentResolver
    private lateinit var contactsRepository: ContactsRepositoryImpl
    
    @Suppress("UNCHECKED_CAST")
    private val mockLauncher: ActivityResultLauncher<Void?> = mock()

    @Before
    fun setUp() {
        contentResolver = mock()
        activity = mock {
            on { contentResolver } doReturn contentResolver
            on { registerForActivityResult(any<ActivityResultContracts.PickContact>(), any()) } doReturn mockLauncher
        }
        
        contactsRepository = ContactsRepositoryImpl(activity)
    }

    @Test
    fun `resolveContact returns contact when valid contact data is provided`() {
        val contactUri = mock<Uri>()
        val contactId = "123"
        val phoneNumber = "555-1234"
        
        // Mock contact cursor
        val contactCursor: Cursor = mock {
            on { moveToFirst() } doReturn true
            on { getColumnIndexOrThrow(ContactsContract.Contacts._ID) } doReturn 0
            on { getString(0) } doReturn contactId
        }
        
        // Mock phone cursor
        val phoneCursor: Cursor = mock {
            on { moveToFirst() } doReturn true
            on { getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER) } doReturn 0
            on { getString(0) } doReturn phoneNumber
        }
        
        whenever(
            contentResolver.query(
                eq(contactUri),
                eq(arrayOf(ContactsContract.Contacts._ID)),
                eq(null),
                eq(null),
                eq(null)
            )
        ).thenReturn(contactCursor)
        
        whenever(
            contentResolver.query(
                eq(ContactsContract.CommonDataKinds.Phone.CONTENT_URI),
                eq(arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)),
                eq("${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"),
                eq(arrayOf(contactId)),
                eq(null)
            )
        ).thenReturn(phoneCursor)

        val result = contactsRepository.resolveContact(contactUri)
        
        assertEquals(Contact(contactId, phoneNumber), result)
        verify(contactCursor).close()
        verify(phoneCursor).close()
    }

    @Test
    fun `resolveContact returns null when contact has no phone number`() {
        val contactUri = mock<Uri>()
        val contactId = "123"
        
        // Mock contact cursor
        val contactCursor: Cursor = mock {
            on { moveToFirst() } doReturn true
            on { getColumnIndexOrThrow(ContactsContract.Contacts._ID) } doReturn 0
            on { getString(0) } doReturn contactId
        }
        
        // Mock empty phone cursor
        val phoneCursor: Cursor = mock {
            on { moveToFirst() } doReturn false // No phone numbers
        }
        
        whenever(
            contentResolver.query(
                eq(contactUri),
                eq(arrayOf(ContactsContract.Contacts._ID)),
                eq(null),
                eq(null),
                eq(null)
            )
        ).thenReturn(contactCursor)
        
        whenever(
            contentResolver.query(
                eq(ContactsContract.CommonDataKinds.Phone.CONTENT_URI),
                eq(arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER)),
                eq("${ContactsContract.CommonDataKinds.Phone.CONTACT_ID} = ?"),
                eq(arrayOf(contactId)),
                eq(null)
            )
        ).thenReturn(phoneCursor)

        val result = contactsRepository.resolveContact(contactUri)
        
        assertNull(result)
        verify(contactCursor).close()
        verify(phoneCursor).close()
    }

    @Test
    fun `resolveContact returns null when contact cursor is empty`() {
        val contactUri = mock<Uri>()
        
        // Mock empty contact cursor
        val contactCursor: Cursor = mock {
            on { moveToFirst() } doReturn false // No contact data
        }
        
        whenever(
            contentResolver.query(
                eq(contactUri),
                eq(arrayOf(ContactsContract.Contacts._ID)),
                eq(null),
                eq(null),
                eq(null)
            )
        ).thenReturn(contactCursor)

        val result = contactsRepository.resolveContact(contactUri)
        
        assertNull(result)
        verify(contactCursor).close()
    }

    @Test
    fun `resolveContact handles null cursors gracefully`() {
        val contactUri = mock<Uri>()
        
        whenever(
            contentResolver.query(
                eq(contactUri),
                eq(arrayOf(ContactsContract.Contacts._ID)),
                eq(null),
                eq(null),
                eq(null)
            )
        ).thenReturn(null) // Database error

        val result = contactsRepository.resolveContact(contactUri)
        
        assertNull(result)
    }
}