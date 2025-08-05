package com.duchastel.simon.simplelauncher.features.homepageaction.ui

import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

@Composable
fun HomepageButton(state: HomepageActionState, modifier: Modifier) {
    Text(
        text = state.emoji,
        fontSize = 100.sp,
        modifier = modifier.clickable(
            onClick = state.onClick
        ),
    )
}
