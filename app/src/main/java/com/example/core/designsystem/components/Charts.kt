package com.example.core.designsystem.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.MindRestTheme
import com.example.core.designsystem.NumberS
import com.example.core.designsystem.SuccessColor

/**
 * SleepStageRadialChart draws 3 concentric arcs for sleep stage distributions using Compose Canvas.
 */
@Composable
fun SleepStageRadialChart(
    lightValue: Float, // 0f to 1f
    deepValue: Float,  // 0f to 1f
    remValue: Float,   // 0f to 1f
    modifier: Modifier = Modifier,
    testTag: String = "sleep_stage_radial_chart"
) {
    var animState by remember { mutableStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = animState,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "RadialChartAnim"
    )

    LaunchedEffect(lightValue, deepValue, remValue) {
        animState = 0f
        animState = 1f
    }

    val lightColor = Color(0xFFEB845C) // Salmon Light Sleep
    val deepColor = com.example.core.designsystem.FeatureJourney // Purple Deep Sleep
    val remColor = MaterialTheme.colorScheme.primary // REM Sleep

    Box(
        modifier = modifier
            .size(100.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val strokeWidth = 8.dp.toPx()
            
            // Background base tracks
            drawCircle(Color.Gray.copy(alpha = 0.1f), radius = 45.dp.toPx(), style = Stroke(strokeWidth))
            drawCircle(Color.Gray.copy(alpha = 0.1f), radius = 35.dp.toPx(), style = Stroke(strokeWidth))
            drawCircle(Color.Gray.copy(alpha = 0.1f), radius = 25.dp.toPx(), style = Stroke(strokeWidth))

            // Light Arc (Outer)
            drawArc(
                color = lightColor,
                startAngle = 270f,
                sweepAngle = lightValue * 360f * animatedProgress,
                useCenter = false,
                topLeft = Offset(center.x - 45.dp.toPx(), center.y - 45.dp.toPx()),
                size = Size(90.dp.toPx(), 90.dp.toPx()),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Deep Arc (Mid)
            drawArc(
                color = deepColor,
                startAngle = 270f,
                sweepAngle = deepValue * 360f * animatedProgress,
                useCenter = false,
                topLeft = Offset(center.x - 35.dp.toPx(), center.y - 35.dp.toPx()),
                size = Size(70.dp.toPx(), 70.dp.toPx()),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // REM Arc (Inner)
            drawArc(
                color = remColor,
                startAngle = 270f,
                sweepAngle = remValue * 360f * animatedProgress,
                useCenter = false,
                topLeft = Offset(center.x - 25.dp.toPx(), center.y - 25.dp.toPx()),
                size = Size(50.dp.toPx(), 50.dp.toPx()),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }
    }
}

data class DailySleepStageData(
    val day: String,
    val lightHours: Float,
    val deepHours: Float,
    val remHours: Float
)

/**
 * SleepBarChart draws a stacked weekly bar chart with smooth entry animation.
 */
@Composable
fun SleepBarChart(
    weeklyData: List<DailySleepStageData>,
    modifier: Modifier = Modifier,
    testTag: String = "sleep_bar_chart"
) {
    var animState by remember { mutableStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = animState,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "SleepBarChartAnim"
    )

    LaunchedEffect(weeklyData) {
        animState = 0f
        animState = 1f
    }

    val lightColor = Color(0xFFEB845C)
    val deepColor = com.example.core.designsystem.FeatureJourney
    val remColor = MaterialTheme.colorScheme.primary

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .testTag(testTag)
    ) {
        val barCount = weeklyData.size
        val gap = 12.dp.toPx()
        val totalWidth = size.width
        val barWidth = (totalWidth - (barCount - 1) * gap) / barCount
        val maxHeight = size.height - 20.dp.toPx() // Reserve some space for labels

        weeklyData.forEachIndexed { i, data ->
            val totalHours = data.lightHours + data.deepHours + data.remHours
            if (totalHours > 0) {
                val scale = (maxHeight / 10f) * animatedProgress // Scale assuming max 10 hours of sleep

                val lightHeight = data.lightHours * scale
                val deepHeight = data.deepHours * scale
                val remHeight = data.remHours * scale

                val x = i * (barWidth + gap)

                // Stack: REM on bottom, Deep in middle, Light on top
                // 1. REM Bar
                val remY = maxHeight - remHeight
                drawRect(
                    color = remColor,
                    topLeft = Offset(x, remY),
                    size = Size(barWidth, remHeight)
                )

                // 2. Deep Bar
                val deepY = remY - deepHeight
                drawRect(
                    color = deepColor,
                    topLeft = Offset(x, deepY),
                    size = Size(barWidth, deepHeight)
                )

                // 3. Light Bar (with rounded top corners and 0.7 opacity)
                val lightY = deepY - lightHeight
                drawRoundRect(
                    color = lightColor.copy(alpha = 0.7f),
                    topLeft = Offset(x, lightY),
                    size = Size(barWidth, lightHeight),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )
            }
        }
    }
}

data class ChartCallout(
    val index: Int,
    val text: String,
    val isMilestone: Boolean // true for milestone/peak (green/purple), false for anomaly/dip (amber/coral)
)

/**
 * WeeklySleepBarChart displays simple rounded single bars representing sleep scores over 7 days.
 * Includes a dashed horizontal reference line representing the average and callout annotations for milestones/anomalies.
 */
@Composable
fun WeeklySleepBarChart(
    scores: List<Int>, // Expecting 7 values
    averageScore: Float,
    modifier: Modifier = Modifier,
    callouts: List<ChartCallout> = emptyList(),
    testTag: String = "weekly_sleep_bar_chart"
) {
    var animState by remember { mutableStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = animState,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "WeeklyBarAnim"
    )

    LaunchedEffect(scores) {
        animState = 0f
        animState = 1f
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val milestoneColor = Color(0xFF34C98A)
    val anomalyColor = Color(0xFFFF8A65)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .testTag(testTag)
    ) {
        val totalWidth = maxWidth
        val topReserved = 22.dp
        val barCount = scores.size
        val gap = 8.dp
        val barWidth = (totalWidth - (gap * (barCount - 1))) / barCount

        Canvas(modifier = Modifier.fillMaxSize()) {
            val topPx = topReserved.toPx()
            val h = size.height - topPx
            val w = size.width
            val gapPx = gap.toPx()
            val bWidthPx = (w - (gapPx * (barCount - 1))) / barCount

            scores.forEachIndexed { i, score ->
                val callout = callouts.find { it.index == i }
                val barColor = when {
                    callout != null && callout.isMilestone -> milestoneColor
                    callout != null && !callout.isMilestone -> anomalyColor
                    else -> primaryColor.copy(alpha = 0.75f)
                }

                val barHeight = (score / 100f) * h * animatedProgress
                val x = i * (bWidthPx + gapPx)
                val y = topPx + (h - barHeight)

                drawRoundRect(
                    color = barColor,
                    topLeft = Offset(x, y),
                    size = Size(bWidthPx, barHeight),
                    cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
                )
            }

            // Draw horizontal dashed reference line for average
            val refY = topPx + h - (averageScore / 100f) * h
            drawLine(
                color = primaryColor.copy(alpha = 0.5f),
                start = Offset(0f, refY),
                end = Offset(w, refY),
                strokeWidth = 1.5.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)
            )
        }

        // Overlay callout annotations
        callouts.forEach { callout ->
            if (callout.index in scores.indices) {
                val xOffset = (barWidth + gap) * callout.index + (barWidth / 2) - 30.dp

                Box(
                    modifier = Modifier
                        .offset(x = xOffset.coerceAtLeast(0.dp), y = 0.dp)
                        .graphicsLayer {
                            alpha = animatedProgress
                            scaleX = 0.7f + (0.3f * animatedProgress)
                            scaleY = 0.7f + (0.3f * animatedProgress)
                        }
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (callout.isMilestone) milestoneColor.copy(alpha = 0.18f)
                            else anomalyColor.copy(alpha = 0.18f)
                        )
                        .border(
                            1.dp,
                            if (callout.isMilestone) milestoneColor else anomalyColor,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = callout.text,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (callout.isMilestone) milestoneColor else anomalyColor
                    )
                }
            }
        }
    }
}

/**
 * AreaTrendChart draws a smooth bezier curve area chart with callout annotations and interactive tap tooltips.
 */
@Composable
fun AreaTrendChart(
    values: List<Float>, // Typically 4 values for W1 - W4 or Mon-Sun
    color: Color,
    modifier: Modifier = Modifier,
    labels: List<String> = emptyList(),
    callouts: List<ChartCallout> = emptyList(),
    overlayValues: List<Float>? = null,
    overlayColor: Color = Color.Gray.copy(alpha = 0.5f),
    testTag: String = "area_trend_chart"
) {
    var animState by remember { mutableStateOf(0f) }
    var selectedIndex by remember(values) { mutableStateOf<Int?>(null) }

    val animatedProgress by animateFloatAsState(
        targetValue = animState,
        animationSpec = tween(durationMillis = 850, easing = FastOutSlowInEasing),
        label = "AreaTrendAnim"
    )

    LaunchedEffect(values) {
        animState = 0f
        animState = 1f
    }

    val milestoneColor = Color(0xFF34C98A)
    val anomalyColor = Color(0xFFFF8A65)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
            .pointerInput(values) {
                detectTapGestures { offset ->
                    if (values.size >= 2) {
                        val xInterval = size.width / (values.size - 1)
                        val tappedIndex = kotlin.math.round(offset.x / xInterval).toInt().coerceIn(0, values.size - 1)
                        selectedIndex = if (selectedIndex == tappedIndex) null else tappedIndex
                    }
                }
            }
            .testTag(testTag)
    ) {
        val totalWidth = maxWidth
        val topReserved = 22.dp

        Canvas(modifier = Modifier.fillMaxSize()) {
            if (values.size < 2) return@Canvas

            val topPx = topReserved.toPx()
            val w = size.width
            val h = size.height - topPx
            val pointsCount = values.size
            val xInterval = w / (pointsCount - 1)

            val path = Path()
            val areaPath = Path()

            values.forEachIndexed { i, value ->
                val percent = ((value / 100f) * animatedProgress).coerceIn(0f, 1f)
                val cx = i * xInterval
                val cy = topPx + (h - (percent * h))

                if (i == 0) {
                    path.moveTo(cx, cy)
                    areaPath.moveTo(cx, topPx + h)
                    areaPath.lineTo(cx, cy)
                } else {
                    val prevX = (i - 1) * xInterval
                    val prevPercent = ((values[i - 1] / 100f) * animatedProgress).coerceIn(0f, 1f)
                    val prevY = topPx + (h - (prevPercent * h))

                    val controlX1 = prevX + xInterval / 2f
                    val controlY1 = prevY
                    val controlX2 = prevX + xInterval / 2f
                    val controlY2 = cy

                    path.cubicTo(controlX1, controlY1, controlX2, controlY2, cx, cy)
                    areaPath.cubicTo(controlX1, controlY1, controlX2, controlY2, cx, cy)
                }

                if (i == pointsCount - 1) {
                    areaPath.lineTo(cx, topPx + h)
                    areaPath.close()
                }
            }

            // 0. Draw comparative overlay path if present
            if (overlayValues != null && overlayValues.size >= 2) {
                val overlayPath = Path()
                val overlayPointsCount = overlayValues.size
                val overlayXInterval = w / (overlayPointsCount - 1)

                overlayValues.forEachIndexed { i, value ->
                    val percent = ((value / 100f) * animatedProgress).coerceIn(0f, 1f)
                    val cx = i * overlayXInterval
                    val cy = topPx + (h - (percent * h))

                    if (i == 0) {
                        overlayPath.moveTo(cx, cy)
                    } else {
                        val prevX = (i - 1) * overlayXInterval
                        val prevPercent = ((overlayValues[i - 1] / 100f) * animatedProgress).coerceIn(0f, 1f)
                        val prevY = topPx + (h - (prevPercent * h))

                        val controlX1 = prevX + overlayXInterval / 2f
                        val controlY1 = prevY
                        val controlX2 = prevX + overlayXInterval / 2f
                        val controlY2 = cy

                        overlayPath.cubicTo(controlX1, controlY1, controlX2, controlY2, cx, cy)
                    }
                }

                drawPath(
                    path = overlayPath,
                    color = overlayColor,
                    style = Stroke(
                        width = 1.8.dp.toPx(),
                        cap = StrokeCap.Round,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f), 0f)
                    )
                )

                // Draw faint overlay dots
                overlayValues.forEachIndexed { i, value ->
                    val percent = ((value / 100f) * animatedProgress).coerceIn(0f, 1f)
                    val cx = i * overlayXInterval
                    val cy = topPx + (h - (percent * h))
                    drawCircle(color = overlayColor, radius = 3.dp.toPx(), center = Offset(cx, cy))
                }
            }

            // 1. Draw gradient area fill
            drawPath(
                path = areaPath,
                brush = Brush.verticalGradient(
                    colors = listOf(color.copy(alpha = 0.3f), Color.Transparent)
                )
            )

            // 2. Draw the spline line
            drawPath(
                path = path,
                color = color,
                style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
            )

            // 3. Draw selected index guideline and active dot highlight
            if (selectedIndex != null && selectedIndex!! in values.indices) {
                val idx = selectedIndex!!
                val valPercent = ((values[idx] / 100f) * animatedProgress).coerceIn(0f, 1f)
                val cx = idx * xInterval
                val cy = topPx + (h - (valPercent * h))

                // Vertical guideline
                drawLine(
                    color = color.copy(alpha = 0.5f),
                    start = Offset(cx, topPx),
                    end = Offset(cx, topPx + h),
                    strokeWidth = 1.5.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
                )

                // Active dot highlight
                drawCircle(color = color.copy(alpha = 0.25f), radius = 10.dp.toPx(), center = Offset(cx, cy))
                drawCircle(color = color, radius = 5.5.dp.toPx(), center = Offset(cx, cy))
                drawCircle(color = Color.White, radius = 2.5.dp.toPx(), center = Offset(cx, cy))

                // Overlay dot highlight if present
                if (overlayValues != null && idx in overlayValues.indices) {
                    val overlayPercent = ((overlayValues[idx] / 100f) * animatedProgress).coerceIn(0f, 1f)
                    val oCy = topPx + (h - (overlayPercent * h))
                    drawCircle(color = overlayColor, radius = 5.dp.toPx(), center = Offset(cx, oCy))
                    drawCircle(color = Color.White, radius = 2.dp.toPx(), center = Offset(cx, oCy))
                }
            }

            // 4. Draw callout dots
            callouts.forEach { callout ->
                if (callout.index in values.indices && callout.index != selectedIndex) {
                    val valPercent = ((values[callout.index] / 100f) * animatedProgress).coerceIn(0f, 1f)
                    val cx = callout.index * xInterval
                    val cy = topPx + (h - (valPercent * h))
                    val dotColor = if (callout.isMilestone) milestoneColor else anomalyColor

                    drawCircle(color = dotColor, radius = 5.dp.toPx(), center = Offset(cx, cy))
                    drawCircle(color = Color.White, radius = 2.5.dp.toPx(), center = Offset(cx, cy))
                }
            }
        }

        // Overlay callout annotation badges
        callouts.forEach { callout ->
            if (callout.index in values.indices && callout.index != selectedIndex) {
                val pointsCount = values.size
                val xInterval = totalWidth / (pointsCount - 1)
                val xOffset = xInterval * callout.index - 26.dp

                Box(
                    modifier = Modifier
                        .offset(x = xOffset.coerceIn(0.dp, totalWidth - 55.dp), y = 0.dp)
                        .graphicsLayer {
                            alpha = animatedProgress
                            scaleX = 0.7f + (0.3f * animatedProgress)
                            scaleY = 0.7f + (0.3f * animatedProgress)
                        }
                        .clip(RoundedCornerShape(6.dp))
                        .background(
                            if (callout.isMilestone) milestoneColor.copy(alpha = 0.18f)
                            else anomalyColor.copy(alpha = 0.18f)
                        )
                        .border(
                            1.dp,
                            if (callout.isMilestone) milestoneColor else anomalyColor,
                            RoundedCornerShape(6.dp)
                        )
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = callout.text,
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (callout.isMilestone) milestoneColor else anomalyColor
                    )
                }
            }
        }

        // Interactive Tooltip Badge when a point is selected
        if (selectedIndex != null && selectedIndex!! in values.indices) {
            val idx = selectedIndex!!
            val pointVal = values[idx].toInt()
            val labelText = labels.getOrNull(idx)
            val pointsCount = values.size
            val xInterval = totalWidth / (pointsCount - 1)
            val rawXOffset = xInterval * idx - 45.dp
            val tooltipXOffset = rawXOffset.coerceIn(0.dp, totalWidth - 95.dp)

            Surface(
                modifier = Modifier
                    .offset(x = tooltipXOffset, y = 0.dp)
                    .shadow(6.dp, RoundedCornerShape(8.dp))
                    .clip(RoundedCornerShape(8.dp)),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (labelText != null) {
                        Text(
                            text = labelText,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(color, CircleShape)
                        )
                        Text(
                            text = "$pointVal / 100",
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (overlayValues != null && idx in overlayValues.indices) {
                            val prevVal = overlayValues[idx].toInt()
                            val diff = pointVal - prevVal
                            val diffText = if (diff >= 0) "+$diff" else "$diff"
                            Text(
                                text = "($diffText vs prev)",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (diff >= 0) SuccessColor else MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChartsPreview() {
    MindRestTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SleepStageRadialChart(lightValue = 0.45f, deepValue = 0.30f, remValue = 0.25f)
            SleepBarChart(
                weeklyData = listOf(
                    DailySleepStageData("Mon", 3.5f, 1.5f, 1f),
                    DailySleepStageData("Tue", 4f, 2f, 1.5f),
                    DailySleepStageData("Wed", 3f, 1f, 1f),
                    DailySleepStageData("Thu", 4.5f, 2.5f, 2f)
                )
            )
            WeeklySleepBarChart(scores = listOf(62, 68, 74, 71, 65, 80, 84), averageScore = 72f)
            AreaTrendChart(values = listOf(72f, 75f, 80f, 84f), color = Color.Magenta)
        }
    }
}
