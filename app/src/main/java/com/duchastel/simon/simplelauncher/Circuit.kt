package com.duchastel.simon.simplelauncher

import com.duchastel.simon.simplelauncher.features.homepageaction.HomepageActionPresenter
import com.duchastel.simon.simplelauncher.features.homepageaction.HomepageActionScreen
import com.slack.circuit.runtime.CircuitContext
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.Screen
import com.slack.circuit.runtime.presenter.Presenter
import com.slack.circuit.runtime.ui.Ui
import com.slack.circuit.runtime.ui.ui

class Circuit(private val homepageActionPresenterFactory: HomepageActionPresenter.Factory) {
    val circuit = com.slack.circuit.runtime.Circuit.Builder()
        .addPresenterFactory(presenterFactory())
        .addUiFactory(uiFactory())
        .build()

    private fun presenterFactory(): Presenter.Factory {
        return Presenter.Factory {
            screen, navigator, context ->
            when (screen) {
                is HomepageActionScreen -> homepageActionPresenterFactory.create(context.activity)
                else -> null
            }
        }
    }

    private fun uiFactory(): Ui.Factory {
        return Ui.Factory {
            screen, context ->
            when (screen) {
                is HomepageActionScreen -> ui<HomepageActionScreen.State> { state, modifier -> com.duchastel.simon.simplelauncher.features.homepageaction.HomepageAction(state, modifier) }
                else -> null
            }
        }
    }
}
