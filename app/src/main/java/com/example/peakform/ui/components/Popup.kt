package com.example.peakform.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.peakform.R
import com.example.peakform.data.model.PopupStatus

@Composable
fun Popup(
    status: PopupStatus,
    popupMessage: String,
) {
    when (status) {
        is PopupStatus.Loading -> PopupContent(animate = R.raw.loading, text = "Please wait...")
        is PopupStatus.Success -> PopupContent(animate = R.raw.success, text = "Success\n$popupMessage")
        is PopupStatus.Error -> PopupContent(animate = R.raw.error, text = "Error\n$popupMessage")
    }
}

@Composable
fun PopupContent(animate: Int, text: String) {
    AlertDialog(
        onDismissRequest = { },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LottieAnimation(
                    modifier = Modifier.size(200.dp),
                    composition = rememberLottieComposition(LottieCompositionSpec.RawRes(animate)).value,
                    iterations = LottieConstants.IterateForever
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text,
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        },
        confirmButton = { }
    )
}
