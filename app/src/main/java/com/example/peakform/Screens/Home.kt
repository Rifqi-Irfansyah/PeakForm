package com.example.peakform.Screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.peakform.Navigation.Screens
import com.example.peakform.R
import com.example.peakform.ViewModel.VMHome
import com.example.peakform.ui.components.CardImage
import com.example.peakform.ui.theme.NavigationBarMediumTheme

@Composable
fun Home(navController: NavController, viewModel: VMHome = viewModel()){
    val schedule by viewModel.scheduleStatus.collectAsState()

    NavigationBarMediumTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CardImage(
                    backgroundRes = R.drawable.cardlist,
                    title = "LIST \nEXERCISE",
                    titleColor = Color.White,
                    onClick = {}
                )

                if(schedule){
                    CardImage(
                        backgroundRes = R.drawable.cardgoal,
                        title = "YOUR\nSCHEDULE",
                        titleColor = Color.White,
                        onClick = { navController.navigate(Screens.MakeSchedule.route)}
                    )
                }
                else{
                    CardImage(
                        backgroundRes = R.drawable.cardgoal,
                        title = "SET \nYOUR GOAL",
                        titleColor = Color.White,
                        onClick = { navController.navigate(Screens.MakeSchedule.route)}
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun Preview(){
    Home(navController = rememberNavController())
}