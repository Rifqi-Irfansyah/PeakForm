package com.example.peakform.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    val isFocused = remember { mutableStateOf(false) }
    val passwordVisible = remember { mutableStateOf(false) }

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
        visualTransformation = if (passwordVisible.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val iconColor = if (isFocused.value) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            val icon = if (passwordVisible.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff

            IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor
                )
            }
        }
    )
}