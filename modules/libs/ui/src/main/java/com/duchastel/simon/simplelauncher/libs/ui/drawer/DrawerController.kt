package com.duchastel.simon.simplelauncher.libs.ui.drawer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Coordinates close requests for the launcher app drawer between the activity
 * and the Compose UI.
 */
interface DrawerController {
    val closeRequests: Flow<Unit>
    fun requestClose()
}

@Singleton
class DrawerControllerImpl @Inject constructor() : DrawerController {
    private val _closeRequests = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    override val closeRequests: Flow<Unit> = _closeRequests

    override fun requestClose() {
        _closeRequests.tryEmit(Unit)
    }
}
