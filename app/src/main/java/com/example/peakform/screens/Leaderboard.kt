package com.example.peakform.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.peakform.data.model.PopupStatus
import com.example.peakform.data.model.User
import com.example.peakform.ui.components.Popup
import com.example.peakform.viewmodel.VMLeaderboard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Leaderboard(
    navController: NavController
) {
    val leaderboardData = listOf(
        User(
            id = "1",
            name = "John Doe",
            email = "john@example.com",
            token = "token1",
            points = 1500
        ),
        User(
            id = "2",
            name = "Jane Smith",
            email = "jane@example.com",
            token = "token2",
            points = 1200
        ),
        User(
            id = "3",
            name = "Bob Wilson",
            email = "bob@example.com",
            token = "token3",
            points = 900
        )
    )

    val loading = false
    val success = true
    val error: String? = null
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            if (loading) {
                Popup(
                    status = PopupStatus.Loading,
                    popupMessage = "Loading leaderboard..."
                )
            }
            if (success) {
                Popup(
                    status = PopupStatus.Success,
                    popupMessage = "Leaderboard loaded successfully!"
                )
                coroutineScope.launch {
                    delay(2000L)
                }
            }
            if (error != null) {
                Popup(
                    status = PopupStatus.Error,
                    popupMessage = error,
                )
                coroutineScope.launch {
                    delay(3000L)
                }
            }

            Text(
                text = "Leaderboard",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        items(leaderboardData) { user ->
            LeaderboardCard(user)
        }
    }
}

@Composable
fun LeaderboardCard(user: User) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(8.dp),
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = "https://example.com/photo${user.id}.jpg", // URL dummy
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.name,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "${user.points} points",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 16.sp
                    )
                )
            }
        }
    }
}