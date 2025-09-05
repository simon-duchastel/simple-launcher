package com.duchastel.simon.simplelauncher.features.appwidgets.data

import kotlinx.serialization.Serializable

@Serializable
sealed class WidgetError {
    @Serializable
    data object ProviderNotFound : WidgetError()
    
    @Serializable
    data object PermissionDenied : WidgetError()
    
    @Serializable
    data object HostCreationFailed : WidgetError()
    
    @Serializable
    data object WidgetBindingFailed : WidgetError()
    
    @Serializable
    data class Unknown(val message: String) : WidgetError()
}

@Serializable
sealed class WidgetViewState {
    @Serializable
    data object Loading : WidgetViewState()
    
    @Serializable
    data class Success(val widgetData: WidgetData) : WidgetViewState()
    
    @Serializable
    data class Error(val error: WidgetError) : WidgetViewState()
}

@Serializable
sealed class BindingState {
    @Serializable
    data object NotBound : BindingState()
    
    @Serializable
    data object Bound : BindingState()
    
    @Serializable
    data object BindingFailed : BindingState()
    
    @Serializable
    data object ProviderNotFound : BindingState()
}

@Serializable
data class WidgetDisplayState(
    val selectedWidget: WidgetData?,
    val boundWidget: WidgetData?,
    val bindingState: BindingState,
    val error: WidgetError?
)