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
import com.example.peakform.API.ExerciseService

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CardExercise(
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

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onClick() }
            .background(MaterialTheme.colorScheme.surface)
    ) {
        val fullImageUrl = imageUrl?.let { "${ExerciseService.getBaseUrlForImages()}$it" }

        if (imageUrl != null) {
            val request = ImageRequest.Builder(context)
                .data(fullImageUrl)
                .crossfade(true)
                .build()

            SubcomposeAsyncImage(
                model = request,
                imageLoader = imageLoader,
                contentDescription = "Exercise Background Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize(),
                loading = {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                    )
                },
                error = {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(MaterialTheme.colorScheme.errorContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Failed to load background",
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            )
        } else {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(MaterialTheme.colorScheme.errorContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No background available",
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        Text(
            text = title,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = titleColor ?: MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(20.dp)
        )
    }
}
