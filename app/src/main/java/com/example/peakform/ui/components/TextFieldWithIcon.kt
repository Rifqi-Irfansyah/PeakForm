package com.example.peakform.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun TextFieldWithIcon(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val isFocused = remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        shape = MaterialTheme.shapes.large,
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged {
                isFocused.value = it.isFocused
            },
        trailingIcon = {
            val iconColor = if (isFocused.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor
            )
        }
    )
}