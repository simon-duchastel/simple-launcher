package com.duchastel.simon.simplelauncher.features.homepageaction.ui

import com.duchastel.simon.simplelauncher.libs.sms.data.SmsRepository
import com.slack.circuit.test.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.mockito.kotlin.eq
import org.mockito.kotlin.any

@OptIn(ExperimentalCoroutinesApi::class)
class HomepageActionPresenterTest {
    private val smsRepository: SmsRepository = mock()
    private val presenter = HomepageActionPresenter(smsRepository)

    @Test
    fun `onClick calls sendSms on repository`() = runTest {
        whenever(smsRepository.sendSms(any(), any())).thenReturn(true)

        presenter.test {
            val state = awaitItem()
            state.onClick.invoke()
            verify(smsRepository).sendSms(eq(""), eq("(Ignore) Test message from Simple Launcher"))
        }
    }
}
