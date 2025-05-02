package com.example.peakform.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WarningDialog(
    isOpen: Boolean,
    message: String,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(isOpen) }
    if (isVisible) {
        AlertDialog(
            onDismissRequest = {
                isVisible = false
                onDismiss()
            },
            title = {
                Text(
                    text = "Warning",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            text = { Text(text = message) },
            confirmButton = {
                TextButton(
                    onClick = {
                        isVisible = false
                        onDismiss()
                    },
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Text(
                        text = "OK",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            modifier = modifier
        )
    }
}