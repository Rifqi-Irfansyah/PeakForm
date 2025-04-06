package com.example.peakform.data.model


sealed class PopupStatus {
    object Loading : PopupStatus()
    object Success : PopupStatus()
    object Error: PopupStatus()
}
