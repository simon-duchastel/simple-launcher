package com.duchastel.simon.simplelauncher.features.homepageaction.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.duchastel.simon.simplelauncher.features.homepageaction.ui.HomepageActionState.EmojiButtonState
import com.duchastel.simon.simplelauncher.libs.ui.extensions.bounceClickable

@Composable
fun HomepageButton(state: HomepageActionState, modifier: Modifier) {
    Text(
        text = state.emoji,
        fontSize = 100.sp,
        color = MaterialTheme.colorScheme.onSurface.run {
            if (state.emojiButtonState is EmojiButtonState.Loading) {
                copy(alpha = 0.38f)
            } else {
                this
            }
        },
        modifier = modifier.run {
            if (state.emojiButtonState is EmojiButtonState.Ready) {
                bounceClickable(
                    onClick = state.emojiButtonState.onClick,
                    onDoubleClick = state.emojiButtonState.onDoubleClick,
                    onLongClick = state.emojiButtonState.onLongClick,
                )
            } else {
                this
            }
        }
    )
}
