package com.duchastel.simon.simplelauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.duchastel.simon.simplelauncher.ui.theme.SimpleLauncherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleLauncherTheme {
                LauncherApp()
            }
        }
    }
}

@Composable
fun LauncherApp() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Hello World!")
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SimpleLauncherTheme {
        LauncherApp()
    }
}
