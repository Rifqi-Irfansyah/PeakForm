package com.example.peakform.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.peakform.R
import com.example.peakform.ui.components.CompPopup

@Composable
fun PopupState(isLoading:Boolean, isSuccess: Boolean, isError: String, text:String){
    if (isLoading) {
        CompPopup(R.raw.loading, "Please wait...")
    }
    if (isSuccess) {
        CompPopup(R.raw.success, "Success\n$text")
    }
    if (isError != "") {
        CompPopup(R.raw.error, "Error\n$isError")
    }
}

@Composable
@Preview
fun PreviewPopupState(){
    PopupState(false, true, "", "create schedule")
}