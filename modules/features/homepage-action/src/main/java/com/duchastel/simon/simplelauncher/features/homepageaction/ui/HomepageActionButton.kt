package com.duchastel.simon.simplelauncher.features.homepageaction.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.duchastel.simon.simplelauncher.libs.ui.extensions.bounceClickable
import com.duchastel.simon.simplelauncher.libs.ui.extensions.rememberBounceClickableScope

@Composable
fun HomepageButton(state: HomepageActionState, modifier: Modifier) {
    val bounceClickableScope = rememberBounceClickableScope()
    val buttonIsPressed by bounceClickableScope.collectIsPressedAsState()
    Text(
        text = state.emoji,
        fontSize = 100.sp,
        color = MaterialTheme.colorScheme.onSurface.run {
            if (buttonIsPressed) {
                copy(alpha = 0.38f)
            } else {
                this
            }
        },
        modifier = modifier.bounceClickable(
            bounceClickableScope = bounceClickableScope,
            onClick = state.onClick,
            onDoubleClick = state.onDoubleClick,
            onLongClick = state.onLongClick,
        )
    )
}
