package com.duchastel.simon.simplelauncher.features.homepageaction

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent

class HomepageActionPresenter @AssistedInject constructor(
    @Assisted private val activity: Activity
) : Presenter<HomepageActionScreen.State> {

    @Composable
    override fun present(): HomepageActionScreen.State {
        return HomepageActionScreen.State {
            event ->
            when (event) {
                HomepageActionScreen.Event.Kiss -> {
                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("smsto:")
                        putExtra("sms_body", "<3")
                    }
                    activity.startActivity(intent)
                }
            }
        }
    }

    @CircuitInject(HomepageActionScreen::class, SingletonComponent::class)
    @AssistedFactory
    interface Factory {
        fun create(activity: Activity): HomepageActionPresenter
    }
}
