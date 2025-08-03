package com.duchastel.simon.simplelauncher

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.duchastel.simon.simplelauncher.features.homepageaction.ui.HomepageActionScreen
import com.slack.circuit.foundation.CircuitContent

@Composable
fun Homepage() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "Welcome back...")
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            CircuitContent(
                HomepageActionScreen,
                modifier = Modifier
                    .padding(
                        horizontal = 80.dp,
                        vertical = 100.dp,
                    )
                    .align(Alignment.TopEnd)
                    .rotate(15f)
            )
        }
    }
}

@Preview
@Composable
fun HomepagePreview() {
    Homepage()
}