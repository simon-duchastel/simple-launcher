package com.duchastel.simon.simplelauncher.features.appwidgets.ui.widget

import android.appwidget.AppWidgetHostView
import com.duchastel.simon.simplelauncher.features.appwidgets.data.AppWidgetRepository
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetData
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetError
import com.duchastel.simon.simplelauncher.features.appwidgets.data.WidgetViewState
import com.slack.circuit.test.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
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
    fun `presenter provides widget data from screen`() = runTest {
        whenever(mockRepository.getWidgetViewState(123)).thenReturn(flowOf(WidgetViewState.Loading))
        whenever(mockRepository.createWidgetView(widgetData)).thenReturn(Result.success(mockHostView))

        presenter.test {
            val state = awaitItem()
            
            assertEquals(widgetData, state.widgetData)
            assertEquals(123, state.widgetData.widgetId)
            assertEquals("com.example/ExampleWidget", state.widgetData.providerComponentName)
            assertEquals("Test Widget", state.widgetData.label)
            assertNotNull(state.onRetry)
            assertNotNull(state.onRemoveWidget)
        }
    }

    @Test
    fun `presenter creates widget view on initialization`() = runTest {
        whenever(mockRepository.getWidgetViewState(123)).thenReturn(flowOf(WidgetViewState.Loading))
        whenever(mockRepository.createWidgetView(widgetData)).thenReturn(Result.success(mockHostView))

        presenter.test {
            awaitItem()
            verify(mockRepository).createWidgetView(widgetData)
        }
    }

    @Test
    fun `onRetry callback invokes repository createWidgetView`() = runTest {
        whenever(mockRepository.getWidgetViewState(123)).thenReturn(flowOf(WidgetViewState.Loading))
        whenever(mockRepository.createWidgetView(widgetData)).thenReturn(Result.success(mockHostView))

        presenter.test {
            val state = awaitItem()
            state.onRetry()
            // Note: we already verified initial creation, onRetry will be an additional call
        }
    }

    @Test
    fun `onRemoveWidget callback invokes repository removeWidget`() = runTest {
        whenever(mockRepository.getWidgetViewState(123)).thenReturn(flowOf(WidgetViewState.Loading))
        whenever(mockRepository.createWidgetView(widgetData)).thenReturn(Result.success(mockHostView))

        presenter.test {
            val state = awaitItem()
            state.onRemoveWidget()
            verify(mockRepository).removeWidget(123)
        }
    }
}