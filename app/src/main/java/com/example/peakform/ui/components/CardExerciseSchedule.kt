package com.example.peakform.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.peakform.R
import com.example.peakform.api.ExerciseService
import com.example.peakform.data.model.Exercises
import com.example.peakform.data.model.Schedule

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CardExerciseSchedule(
    exercises: Exercises
) {
    val context = LocalContext.current

    val imageLoader = ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .build()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable {  },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val fullImageUrl = exercises.image.let { "${ExerciseService.getBaseUrlForImages()}$it" }

            val request = ImageRequest.Builder(context)
                .data(fullImageUrl)
                .crossfade(true)
                .build()

            SubcomposeAsyncImage(
                model = request,
                imageLoader = imageLoader,
                contentDescription = "Exercise Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(100.dp)
                    .weight(1f),
                loading = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.secondary)
                    )
                },
                error = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.errorContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Failed to load",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            )
            Column (
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(3f)
                    .padding(start = 16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ){
                Text(
                    text = exercises.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color =  MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = exercises.set.toString() + " x " + exercises.repetition.toString() + " reps",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color =  MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Image(
                painter = painterResource(id = R.drawable.icon_edit),
                contentDescription = "Logo Vector",
                modifier = Modifier
                    .size(20.dp)
                    .weight(0.5f)
                    .clickable {  }
            )
        }
    }
}

