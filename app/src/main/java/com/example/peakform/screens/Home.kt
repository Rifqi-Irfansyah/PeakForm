package com.example.peakform.screens
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(navController: NavController, userViewModel: VMUser, viewModel: VMHome = viewModel()) {
    val schedule by viewModel.scheduleStatus.collectAsState()
    val user = userViewModel.user
    val currentHour = LocalTime.now().hour
    val greeting = when {
        currentHour < 12 -> "Good Morning"
        currentHour < 17 -> "Good Afternoon"
        else -> "Good Evening"
    }

    NavigationBarMediumTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
        ) {
            // ========== HEADER ==========
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
                        painter = painterResource(id = R.drawable.trophy), // ganti dengan icon trophy kamu
                        contentDescription = "Trophy",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { },
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Column {
                    Text(
                        text = "$greeting, ${user?.name ?: "Fauzan"}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                    Text(
                        text = "Let's stay active today!",
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(), // Takes remaining height
                color = Color.Transparent
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.background,
                        )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        StreakBar(streak = 5) // Static streak for now
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CardImage(
                                backgroundRes = R.drawable.cardlist,
                                title = "LIST\nEXERCISE",
                                titleColor = Color.White,
                                onClick = { }
                            )
                            if (schedule) {
                                CardImage(
                                    backgroundRes = R.drawable.cardschedules,
                                    title = "YOUR\nSCHEDULE",
                                    titleColor = Color.White,
                                    onClick = { navController.navigate(Screens.ShowSchedule.route) }
                                )
                            } else {
                                CardImage(
                                    backgroundRes = R.drawable.cardgoal,
                                    title = "SET\nYOUR GOAL",
                                    titleColor = Color.White,
                                    onClick = { navController.navigate(Screens.MakeSchedule.route) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StreakBar(streak: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Streak",
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier
                .padding(bottom = 2.dp)
                .align(Alignment.Start)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(28.dp)
                    .clip(RoundedCornerShape(topStart = 14.dp, bottomStart = 14.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(14.dp))
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    repeat(10) { index ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .background(
                                    if (index < streak) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background
                                )
                        )
                    }
                }
            }
            Text(
                text = "$streak/10",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

//@Composable
//@Preview
//fun Preview(){
//    Home(
//        navController = rememberNavController(),
//        userViewModel = TODO(),
//        viewModel = TODO()
//    )
//}