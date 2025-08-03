package com.duchastel.simon.simplelauncher

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.duchastel.simon.simplelauncher.features.homepageaction.ui.HomepageActionScreen
import com.slack.circuit.foundation.CircuitContent

@Composable
fun Homepage() {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Text(text = "Hello world")
        CircuitContent(HomepageActionScreen)
    }
}

@Preview
@Composable
fun HomepagePreview() {
    Homepage()
}