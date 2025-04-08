package com.example.peakform.ui.components

import android.annotation.SuppressLint
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.peakform.api.ExerciseService

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CardExerciseSchedule(
    imageUrl: String?,
    title: String,
    titleColor: Color? = null,
    onClick: () -> Unit
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
            .height(180.dp)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor ?: MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            val fullImageUrl = imageUrl?.let { "${ExerciseService.getBaseUrlForImages()}$it" }

            if (imageUrl != null) {
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
                        .height(140.dp)
                        .aspectRatio(1f),
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
            } else {
                Box(
                    modifier = Modifier
                        .height(140.dp)
                        .aspectRatio(1f)
                        .background(MaterialTheme.colorScheme.errorContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No image",
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}

