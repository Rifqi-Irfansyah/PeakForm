package com.example.peakform.ui.components

import android.graphics.drawable.shapes.Shape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.size
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import com.example.peakform.data.model.Exercise
import com.example.peakform.data.model.Exercises
import androidx.compose.ui.Modifier
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.peakform.api.ExerciseService
import kotlin.math.round

@Composable
fun DropdownExercise(
    exercises: List<Exercise>,
    selectedExercise: Exercises?,
    onExerciseSelected: (Exercise) -> Unit
) {
    var selectExercise by remember { mutableStateOf<Exercise?>(null) }
    var expanded by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    Box {
        OutlinedTextField(
            shape = RoundedCornerShape(15.dp),
            value = selectExercise?.name ?: selectedExercise?.name ?: "Choose Exercise",
            onValueChange = {},
            readOnly = true,
            label = { Text("Exercise") },
            trailingIcon = {
                IconButton(
                    onClick = { expanded = !expanded },
                    interactionSource = interactionSource,
                ){
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Dropdown",
                    )
                }

            },
        )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .padding(10.dp)
                    .height(400.dp)
                    .align(alignment = Alignment.Center)
            ) {
            exercises.forEach { exercise ->
                val context = LocalContext.current
                val imageLoader = ImageLoader.Builder(context)
                    .components {
                        add(SvgDecoder.Factory())
                    }
                    .build()
                val fullImageUrl = exercise.image.let { "${ExerciseService.getBaseUrlForImages()}$it" }

                val request = ImageRequest.Builder(context)
                    .data(fullImageUrl)
                    .crossfade(true)
                    .build()

                DropdownMenuItem(
                    modifier = Modifier
                        .height(100.dp),
                    leadingIcon = {
                        SubcomposeAsyncImage(
                            model = request,
                            imageLoader = imageLoader,

                            contentDescription = exercise.name,
                            modifier = Modifier.size(54.dp),
                            contentScale = ContentScale.Crop,
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
                    },
                    text = { Text(exercise.name) },
                    onClick = {
                        selectExercise = exercise
                        onExerciseSelected(exercise)
                        expanded = false
                    }
                )
            }
        }
    }
}
