package com.example.peakform.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.peakform.ui.components.BarChart
import com.example.peakform.viewmodel.VMStats
import com.example.peakform.viewmodel.VMUser

@Composable
fun Stats(vmUser: VMUser, viewModel: VMStats = viewModel()) {
    val userId = vmUser.user?.id

    if (userId.isNullOrBlank()) {
        Text("User not logged in", color = Color.Red)
        return
    }

    LaunchedEffect(userId) {
        viewModel.fetchStats(userId)
    }

    val stats = viewModel.stats
    val error = viewModel.errorMessage

    when {
        stats != null -> {
            Column(modifier = Modifier.padding(16.dp)) {
                Spacer(Modifier.height(16.dp))
                Spacer(Modifier.height(8.dp))

                BarChart(data = stats.exerciseCounter)
            }
        }

        error != null -> {
            Text("Error: $error", color = Color.Red)
        }

        else -> {
            CircularProgressIndicator()
        }
    }
}