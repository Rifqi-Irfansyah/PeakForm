package com.example.peakform.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.peakform.R
import com.example.peakform.data.model.User
import androidx.compose.foundation.BorderStroke
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.peakform.data.model.UserLeaderboard
import com.example.peakform.viewmodel.VMLeaderboard



@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Leaderboard(
    navController: NavController,
    leaderboardViewModel: VMLeaderboard = viewModel()
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
        ),
        User(
            id = "4",
            name = "Alice Brown",
            email = "alice@example.com",
            token = "token4",
            points = 800
        ),
        User(
            id = "5",
            name = "Charlie Davis",
            email = "charlie@example.com",
            token = "token5",
            points = 700
        ),
        User(
            id = "6",
            name = "Emma Clark",
            email = "emma@example.com",
            token = "token6",
            points = 600
        )
    )

    val loading by leaderboardViewModel.loading.collectAsState()
    val success by leaderboardViewModel.success.collectAsState()
    val error by leaderboardViewModel.error.collectAsState()
    val userLeaderboard by leaderboardViewModel.userLeaderboard.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        leaderboardViewModel.getLeaderboard()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Leaderboard",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                AsyncImage(
                    model = R.drawable.trophy,
                    contentDescription = "Trophy Icon",
                    modifier = Modifier
                        .size(150.dp)
                        .padding(bottom = 16.dp, top = 16.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        itemsIndexed(userLeaderboard) { index, user ->
            LeaderboardCard(user = user, position = index + 1)
        }
    }
}

@Composable
fun LeaderboardCard(user: UserLeaderboard, position: Int) {
    val backgroundColor = when (position) {
        1 -> Color(0xFFFFD700) // Gold for 1st
        2 -> Color(0xFFC0C0C0) // Silver for 2nd
        3 -> Color(0xFFCD7F32) // Bronze for 3rd
        else -> MaterialTheme.colorScheme.surface // Default for others
    }

    val borderColor = when (position) {
        1 -> Color(0xFFB8860B)
        2 -> Color(0xFF808080)
        3 -> Color(0xFF8C5523)
        else -> MaterialTheme.colorScheme.primary
    }

    val cardHeight = when (position) {
        1 -> 70.dp
        2 -> 65.dp
        3 -> 60.dp
        else -> 60.dp
    }

    val imageSize = when (position) {
        1 -> 48.dp
        2 -> 44.dp
        3 -> 40.dp
        else -> 40.dp
    }

    val nameFontSize = when (position) {
        1 -> 18.sp
        2 -> 17.sp
        3 -> 16.sp
        else -> 16.sp
    }

    val pointsFontSize = when (position) {
        1 -> 16.sp
        2 -> 15.sp
        3 -> 14.sp
        else -> 14.sp
    }

    val borderWidth = when (position) {
        1 -> 2.dp
        2 -> 1.5.dp
        3 -> 1.dp
        else -> 1.dp
    }

    val showStar = position == 1

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight),
        shape = RoundedCornerShape(15.dp),
        shadowElevation = when (position) {
            1 -> 6.dp
            2 -> 5.dp
            else -> 4.dp
        },
        color = backgroundColor,
        border = BorderStroke(borderWidth, borderColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$position",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(24.dp),
                    textAlign = TextAlign.Center
                )

//                AsyncImage(
//                    model = "https://example.com/photo${use.id}.jpg",
//                    contentDescription = "Profile Picture",
//                    modifier = Modifier
//                        .size(imageSize)
//                        .clip(CircleShape)
//                        .border(borderWidth, borderColor, CircleShape),
//                    contentScale = ContentScale.Crop
//                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = user.name,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = nameFontSize,
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${user.point} points",
                    style = TextStyle(
                        color = if (position <= 3) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.secondary,
                        fontSize = pointsFontSize,
                        fontWeight = if (position <= 3) FontWeight.SemiBold else FontWeight.Normal
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )
                if (showStar) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "First Place",
                        tint = borderColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}