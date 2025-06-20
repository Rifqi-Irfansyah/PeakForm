package com.example.peakform.screens
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.peakform.navigation.Screens
import com.example.peakform.R
import com.example.peakform.viewmodel.VMHome
import com.example.peakform.ui.components.CardImage
import com.example.peakform.ui.theme.NavigationBarMediumTheme
import com.example.peakform.viewmodel.VMUser
import kotlinx.coroutines.delay
import com.example.peakform.ui.components.PointStreakInfo


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(navController: NavController, userViewModel: VMUser, viewModel: VMHome = viewModel()) {
    val schedule by viewModel.scheduleStatus.collectAsState()
    val user = userViewModel.user

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
        }
    }

    LaunchedEffect(user) {
        user?.id?.let {
            viewModel.setUserId(it)
        }
    }

    NavigationBarMediumTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { },
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.trophy),
                        contentDescription = "Trophy",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxSize().padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(modifier = Modifier.height(45.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Box(modifier = Modifier.height(5.dp))
                    Text(
                        text = "Hello, ${user?.name ?: "User"}!",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = getMotivationalMessage(user?.streak ?: 0),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                        ),
                        textAlign = TextAlign.Start
                    )
                }

                PointStreakInfo(streak = user?.streak ?: 0, points = user?.point ?: 0, rank = user?.rank ?: 0)

                if (schedule) {
                    CardImage(
                        backgroundRes = R.drawable.cardschedules,
                        title = "YOUR\nSCHEDULE",
                        titleColor = Color.White,
                        onClick = { navController.navigate(Screens.ShowSchedule.route) }
                    )
                    CardImage(
                        backgroundRes = R.drawable.cardnotification,
                        title = "ADJUST\nNOTIFICATION",
                        titleColor = Color.White,
                        onClick = { navController.navigate(Screens.Notification.route) }
                    )
                } else {
                    CardImage(
                        backgroundRes = R.drawable.cardgoal,
                        title = "SET \nYOUR GOAL",
                        titleColor = Color.White,
                        onClick = { navController.navigate(Screens.MakeSchedule.route) }
                    )
                }
            }
        }
    }
}

@Composable
fun getMotivationalMessage(streak: Int): String {
    return when (streak) {
        0 -> "Start your journey with a single step!"
        in 1..2 -> "Great start! Keep pushing forward!"
        in 3..5 -> "You're building momentum, keep it up!"
        in 6..8 -> "You're on fire! Aim for the stars!"
        in 9..10 -> "Unstoppable! You're a fitness legend!"
        else -> "Keep shining, you're doing amazing!"
    }
}