package com.duchastel.simon.simplelauncher.features.appwidgets.ui.widget

import android.appwidget.AppWidgetHostView
import com.duchastel.simon.simplelauncher.features.appwidgets.data.AppWidgetRepository
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetError
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetViewState
import com.slack.circuit.test.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class AppWidgetPresenterTest {

    private val mockRepository = mock<AppWidgetRepository>()
    private val mockHostView = mock<AppWidgetHostView>()

    private val widgetData = WidgetData(
        widgetId = 123,
        providerComponentName = "com.example/ExampleWidget",
        width = 200,
        height = 100,
        label = "Test Widget"
    )

    private val screen = AppWidgetScreen(widgetData)
    private lateinit var presenter: AppWidgetPresenter

    @Before
    fun setUp() {
        presenter = AppWidgetPresenter(screen, mockRepository)
    }

    @Test
    fun `state contains widget data from screen`() = runTest {
        whenever(mockRepository.getWidgetViewState(123)).thenReturn(flowOf(WidgetViewState.Loading))
        whenever(mockRepository.createWidgetView(widgetData)).thenReturn(Result.success(mockHostView))

        presenter.test {
            val state = expectMostRecentItem()
            
            assert(state.widgetData == widgetData)
            
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `state shows loading initially`() = runTest {
        whenever(mockRepository.getWidgetViewState(123)).thenReturn(flowOf(WidgetViewState.Loading))
        whenever(mockRepository.createWidgetView(widgetData)).thenReturn(Result.success(mockHostView))

        presenter.test {
            val state = expectMostRecentItem()
            
            assert(state.widgetViewState == WidgetViewState.Loading)
            
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `state updates when widget view state changes`() = runTest {
        val successState = WidgetViewState.Success(widgetData)
        whenever(mockRepository.getWidgetViewState(123)).thenReturn(
            flowOf(WidgetViewState.Loading, successState)
        )
        whenever(mockRepository.createWidgetView(widgetData)).thenReturn(Result.success(mockHostView))

        presenter.test {
            awaitItem() // Skip initial loading state
            runCurrent()
            
            val state = expectMostRecentItem()
            assert(state.widgetViewState == successState)
            
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `state contains error when repository returns error`() = runTest {
        val error = WidgetError.ProviderNotFound
        val errorState = WidgetViewState.Error(error)
        whenever(mockRepository.getWidgetViewState(123)).thenReturn(flowOf(errorState))
        whenever(mockRepository.createWidgetView(widgetData)).thenReturn(Result.success(mockHostView))

        presenter.test {
            val state = expectMostRecentItem()
            
            assert(state.widgetViewState == errorState)
            
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `widget creation is triggered on widget id change`() = runTest {
        whenever(mockRepository.getWidgetViewState(123)).thenReturn(flowOf(WidgetViewState.Loading))
        whenever(mockRepository.createWidgetView(widgetData)).thenReturn(Result.success(mockHostView))

        presenter.test {
            advanceUntilIdle()
            
            verify(mockRepository).createWidgetView(widgetData)
            
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onRetry calls repository to create widget view again`() = runTest {
        whenever(mockRepository.getWidgetViewState(123)).thenReturn(flowOf(WidgetViewState.Loading))
        whenever(mockRepository.createWidgetView(widgetData)).thenReturn(Result.success(mockHostView))

        presenter.test {
            val state = expectMostRecentItem()
            
            state.onRetry()
            runCurrent()
            
            verify(mockRepository, times(2)).createWidgetView(widgetData)
            
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `onRemoveWidget calls repository to remove widget`() = runTest {
        whenever(mockRepository.getWidgetViewState(123)).thenReturn(flowOf(WidgetViewState.Loading))
        whenever(mockRepository.createWidgetView(widgetData)).thenReturn(Result.success(mockHostView))

        presenter.test {
            val state = expectMostRecentItem()
            
            state.onRemoveWidget()
            runCurrent()
            
            verify(mockRepository).removeWidget(123)
            
            cancelAndConsumeRemainingEvents()
        }
    }
}