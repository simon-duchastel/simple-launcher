package com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting

import android.provider.ContactsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ModifySettingState.ButtonState
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ModifySettingState.ContactPickerAction
import com.duchastel.simon.simplelauncher.features.settings.ui.modifysetting.ModifySettingState.HomepageActionState

@Composable
fun ModifySettingContent(state: ModifySettingState, modifier: Modifier = Modifier) {
    when (state) {
        is HomepageActionState -> {
            HomepageActionContent(state, modifier)
        }
    }
}

@Composable
private fun HomepageActionContent(state: HomepageActionState, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.PickContact()) { uri ->
        if (uri != null) {
            val cursor = context.contentResolver.query(
                uri,
                arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                null,
                null,
                null
            )

            if (cursor?.moveToFirst() == true) {
                val numberIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                if (numberIndex != -1) {
                    val number = cursor.getString(numberIndex)
                    state.onPhoneNumberChanged(number)
                }
            }
            cursor?.close()
        }
    }

    LaunchedEffect(state.contactPickerAction) {
        if (state.contactPickerAction is ContactPickerAction.Launch) {
            launcher.launch(null)
            state.onContactPickerActionConsumed()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TextField(
            value = state.emoji,
            onValueChange = { state.onEmojiChanged(it) },
            label = { Text("Emoji") },
            isError = state.isEmojiError,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = state.phoneNumber,
                onValueChange = { state.onPhoneNumberChanged(it) },
                label = { Text("Phone number") },
                isError = state.isPhoneNumberError,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = { state.onChooseFromContactsClicked() }) {
                Text("Choose from contacts")
            }
        }
        TextButton(
            onClick = { state.onSaveButtonClicked() },
            enabled = state.saveButtonState is ButtonState.Enabled
        ) {
            if (state.saveButtonState is ButtonState.Loading) {
                CircularProgressIndicator()
            } else {
                Text("Save")
            }
        }
    }
}