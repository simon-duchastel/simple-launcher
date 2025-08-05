package com.duchastel.simon.simplelauncher.features.homepageaction.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.sp

@Composable
fun HomepageButton(state: HomepageActionState, modifier: Modifier) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale = remember { Animatable(1f) }
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            when (interaction) {
                is PressInteraction.Press -> scale.animateTo(0.9f)
                is PressInteraction.Release -> scale.animateTo(1f)
                is PressInteraction.Cancel -> scale.animateTo(1f)
            }
        }
    }

    Text(
        text = "😘",
        fontSize = 100.sp,
        modifier = modifier
            .scale(scale.value)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = state.onClick
            ),
    )
}
