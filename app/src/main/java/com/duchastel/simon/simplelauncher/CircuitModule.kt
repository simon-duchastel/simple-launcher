package com.duchastel.simon.simplelauncher

import com.duchastel.simon.simplelauncher.features.homepageaction.HomepageActionPresenter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CircuitModule {

    @Provides
    fun provideCircuit(homepageActionPresenterFactory: HomepageActionPresenter.Factory): Circuit {
        return Circuit(homepageActionPresenterFactory)
    }
}
