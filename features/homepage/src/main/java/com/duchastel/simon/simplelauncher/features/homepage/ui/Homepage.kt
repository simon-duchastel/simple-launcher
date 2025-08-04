package com.duchastel.simon.simplelauncher.features.homepage.ui

import android.net.Uri
import android.os.Parcelable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.duchastel.simon.simplelauncher.features.homepageaction.ui.HomepageActionScreen
import com.slack.circuit.foundation.CircuitContent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.screen.Screen
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomepageScreen(
    val backgroundImageUri: Uri? = null,
) : Screen, Parcelable

data class HomepageState(
    val text: String = "Hello, World!",
    val backgroundImageUri: Uri? = null
) : CircuitUiState

@Composable
internal fun Homepage(state: HomepageState) {
    Box(modifier = Modifier.fillMaxSize()) {
        state.backgroundImageUri?.let { uri ->
            AsyncImage(
                model = uri,
                contentDescription = "Background Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        CircuitContent(
            HomepageActionScreen,
            modifier = Modifier
                .padding(
                    horizontal = 60.dp,
                    vertical = 100.dp,
                )
                .align(Alignment.TopEnd)
                .rotate(15f)
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = "Welcome back...")
        }
    }
}

class HomepagePresenter internal constructor(
    private val screen: HomepageScreen,
) : Presenter<HomepageState> {

    @Composable
    override fun present(): HomepageState {
        return HomepageState(backgroundImageUri = screen.backgroundImageUri)
    }
}
