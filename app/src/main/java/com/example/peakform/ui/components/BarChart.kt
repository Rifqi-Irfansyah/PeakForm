package com.example.peakform.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.ceil

@Composable
fun BarChart(data: Map<String, Int>) {
    val sortedData = data.entries.sortedByDescending { it.value }.associate { it.key to it.value }
    val maxValue = sortedData.values.maxOrNull()?.takeIf { it > 0 } ?: 1

    val barWidth = 40.dp
    val chartHeight = 200.dp
    val spacing = 20.dp
    val minItemWidth = barWidth + spacing

    val axisColor = MaterialTheme.colorScheme.onSurface
    val gridColor = axisColor.copy(alpha = 0.2f)
    val barColor = MaterialTheme.colorScheme.primary
    val shadowColor = axisColor.copy(alpha = 0.1f)

    val yAxisMax = when {
        maxValue <= 5 -> 5
        maxValue <= 10 -> 10
        maxValue <= 20 -> (ceil(maxValue / 5.0) * 5).toInt()
        else -> (ceil(maxValue / 10.0) * 10).toInt()
    }

    val yAxisSteps = when {
        yAxisMax <= 5 -> 1
        yAxisMax <= 10 -> 2
        yAxisMax <= 20 -> 5
        else -> 10
    }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(
            text = "Exercise Frequency",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            // Y-axis
            Column(
                modifier = Modifier
                    .width(40.dp)
                    .height(chartHeight + 40.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                val stepCount = yAxisMax / yAxisSteps
                for (i in stepCount downTo 0) {
                    Box(
                        modifier = Modifier.height(chartHeight / (stepCount + 1)),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = "${i * yAxisSteps}",
                            fontSize = 10.sp,
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }

                // Y-axis label (rotated to avoid truncation)
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        rotate(degrees = -90f, pivot = Offset(size.width / 2, size.height / 2)) {
                            drawContext.canvas.nativeCanvas.apply {
                                val paint = android.graphics.Paint().apply {
                                    color = axisColor.toArgb()
                                    textSize = 11.sp.toPx()
                                    textAlign = android.graphics.Paint.Align.CENTER
                                    typeface = android.graphics.Typeface.DEFAULT_BOLD
                                }
                                drawText(
                                    "Frequency",
                                    size.width / 2,
                                    size.height / 2 + paint.textSize / 3,
                                    paint
                                )
                            }
                        }
                    }
                }
            }

            val scrollState = rememberScrollState()
            Column {
                Row(
                    modifier = Modifier
                        .horizontalScroll(scrollState)
                ) {
                    Column {
                        // Chart and bars
                        Box(
                            modifier = Modifier
                                .width(minItemWidth * sortedData.size)
                                .height(chartHeight)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawChartGrid(
                                    yAxisMax, yAxisSteps, axisColor, gridColor
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 1.dp),
                                horizontalArrangement = Arrangement.spacedBy(spacing),
                                verticalAlignment = Alignment.Bottom
                            ) {
                                sortedData.forEach { (label, value) ->
                                    val heightRatio = value.toFloat() / yAxisMax

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Bottom,
                                        modifier = Modifier.height(chartHeight)
                                    ) {
                                        if (value > 0) {
                                            Text(
                                                text = value.toString(),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Medium,
                                                modifier = Modifier.padding(bottom = 4.dp)
                                            )
                                        }

                                        Canvas(
                                            modifier = Modifier
                                                .height(chartHeight * heightRatio)
                                                .width(barWidth)
                                        ) {
                                            drawRoundRect(
                                                color = shadowColor,
                                                size = size.copy(width = size.width + 2f, height = size.height + 2f),
                                                cornerRadius = CornerRadius(8f)
                                            )
                                            drawRoundRect(
                                                color = barColor,
                                                size = size,
                                                cornerRadius = CornerRadius(8f)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // X-axis labels
                        Row(
                            modifier = Modifier
                                .width(minItemWidth * sortedData.size)
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.spacedBy(spacing)
                        ) {
                            sortedData.keys.forEach { label ->
                                Box(
                                    modifier = Modifier
                                        .width(barWidth) // Align with bar width
                                        .padding(horizontal = 2.dp),
                                    contentAlignment = Alignment.TopCenter
                                ) {
                                    Text(
                                        text = label,
                                        fontSize = 10.sp,
                                        textAlign = TextAlign.Center,
                                        maxLines = 2,
                                        lineHeight = 12.sp,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Exercise Type",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .width(minItemWidth * sortedData.size)
                                .padding(top = 12.dp)
                        )
                    }
                }

                if (sortedData.size > 4) {
                    Spacer(modifier = Modifier.height(8.dp))

                    val indicatorHeight = 4.dp
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(indicatorHeight)
                            .padding(horizontal = 40.dp)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawRoundRect(
                                color = gridColor,
                                size = size,
                                cornerRadius = CornerRadius(4f)
                            )

                            val scrollRatio = if (scrollState.maxValue > 0) {
                                scrollState.value.toFloat() / scrollState.maxValue
                            } else 0f

                            val visibleRatio = this.size.width / (minItemWidth.toPx() * sortedData.size)
                            val indicatorWidth = size.width * visibleRatio
                            val indicatorOffsetX = scrollRatio * (size.width - indicatorWidth)

                            drawRoundRect(
                                color = barColor,
                                topLeft = Offset(indicatorOffsetX, 0f),
                                size = androidx.compose.ui.geometry.Size(indicatorWidth, size.height),
                                cornerRadius = CornerRadius(4f)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun DrawScope.drawChartGrid(
    yAxisMax: Int,
    yAxisSteps: Int,
    axisColor: Color,
    gridColor: Color
) {
    val steps = yAxisMax / yAxisSteps
    val stepHeight = size.height / (steps + 1)

    for (i in 0..steps) {
        val y = size.height - (i * stepHeight)
        drawLine(
            color = if (i == 0) axisColor else gridColor,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = if (i == 0) 2f else 1f
        )
    }

    drawLine(
        color = axisColor,
        start = Offset(0f, 0f),
        end = Offset(0f, size.height),
        strokeWidth = 2f
    )
}