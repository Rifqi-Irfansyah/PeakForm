package com.example.peakform.ui.components

import android.graphics.Insets.add
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.peakform.api.ExerciseService
import com.example.peakform.data.model.Exercise

@Composable
fun MultiSelectExercise(
    title: String,
    items: List<Exercise>,
    selectedItems: Set<Int>,
    onItemSelected: (Int) -> Unit,
    onItemUnselected: (Int) -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .components { add(SvgDecoder.Factory()) }.build()


    Dialog(onDismissRequest = { onDismiss() }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                LazyColumn(
                    modifier = Modifier
                        .heightIn(max = 500.dp)
                ) {
                    itemsIndexed(items) { index, item ->
                        val isSelected = selectedItems.contains(index + 1)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp)
                                .clickable {
                                    if (isSelected) {
                                        onItemUnselected(index + 1)
                                    } else {
                                        onItemSelected(index + 1)
                                    }
                                }
                                .clip(shape = RoundedCornerShape(15.dp))
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.secondary
                                    else MaterialTheme.colorScheme.surface
                                )
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = {
                                    if (it) {
                                        onItemSelected(index + 1)
                                    } else {
                                        onItemUnselected(index + 1)
                                    }
                                }
                            )
                            Row(){
                                Text(
                                    text = item.name,
                                    modifier = Modifier
                                        .weight(1f)
                                        .align(Alignment.CenterVertically)
                                )

                                val fullImageUrl = item.image.let { "${ExerciseService.getBaseUrlForImages()}$it" }
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
                                        .height(50.dp)
                                        .weight(0.5f)
                                        .align(Alignment.CenterVertically),
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
                            }
                        }
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = { onDismiss() }) {
                        Text("Cancel")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { onConfirm() }) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}