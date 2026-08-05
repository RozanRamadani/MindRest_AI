package com.example.features.home.presentation.screen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.DisplayFontFamily
import com.example.core.designsystem.MindRestTheme
import com.example.core.designsystem.NumberM
import com.example.core.designsystem.components.PrimaryButton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DailyCheckInBottomSheet(
    onDismissRequest: () -> Unit,
    onSave: (selectedEmotions: List<String>, sleepDurationHours: Float) -> Unit,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState()
) {
    // Local state for selected emotions (up to 3)
    val availableEmotions = listOf(
        "Lelah", "Tenang", "Cemas", "Bersyukur", 
        "Overthinking", "Damai", "Fokus", "Bersemangat"
    )
    val selectedEmotions = remember { mutableStateListOf("Tenang") }

    // Local state for sleep duration slider (0..12 hours)
    var sleepDurationHours by remember { mutableFloatStateOf(7.5f) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        modifier = modifier.testTag("daily_checkin_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. HEADER
            Text(
                text = "Check-in Hari Ini",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = DisplayFontFamily,
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .testTag("checkin_header_title")
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 2. SECTION 1: FLOATING EMOTION BUBBLES
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Apa yang paling mendominasi pikiranmu?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Pilih maks. 3 emosi",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 2.dp, bottom = 12.dp)
                )

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("emotion_bubbles_flow_row"),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    availableEmotions.forEach { emotion ->
                        val isSelected = selectedEmotions.contains(emotion)
                        val chipBgColor = if (isSelected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                        val chipTextColor = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        }

                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(chipBgColor)
                                .border(
                                    width = if (isSelected) 1.5.dp else 0.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                                    shape = CircleShape
                                )
                                .clickable {
                                    if (isSelected) {
                                        selectedEmotions.remove(emotion)
                                    } else {
                                        if (selectedEmotions.size < 3) {
                                            selectedEmotions.add(emotion)
                                        }
                                    }
                                }
                                .padding(horizontal = 18.dp, vertical = 10.dp)
                                .testTag("emotion_chip_$emotion"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = emotion,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = chipTextColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 3. SECTION 2: CIRCULAR SLEEP DIAL
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Kualitas istirahat semalam",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Circular Canvas Dial
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .testTag("circular_sleep_dial_box"),
                    contentAlignment = Alignment.Center
                ) {
                    val trackColor = MaterialTheme.colorScheme.surfaceVariant
                    val primaryColor = MaterialTheme.colorScheme.primary
                    val animatedAngle by animateFloatAsState(
                        targetValue = (sleepDurationHours / 12f) * 360f,
                        label = "sleep_dial_arc"
                    )

                    Canvas(modifier = Modifier.size(200.dp)) {
                        val strokeWidthPx = 18.dp.toPx()
                        val diameter = size.minDimension - strokeWidthPx
                        val topLeft = Offset(
                            x = (size.width - diameter) / 2f,
                            y = (size.height - diameter) / 2f
                        )
                        val arcSize = Size(diameter, diameter)

                        // Draw background track
                        drawArc(
                            color = trackColor,
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidthPx)
                        )

                        // Draw primary sleep arc
                        drawArc(
                            color = primaryColor,
                            startAngle = -90f,
                            sweepAngle = animatedAngle,
                            useCenter = false,
                            topLeft = topLeft,
                            size = arcSize,
                            style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
                        )
                    }

                    // Center text displaying sleep duration
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val formattedDuration = if (sleepDurationHours % 1f == 0f) {
                            "${sleepDurationHours.toInt()}h"
                        } else {
                            String.format("%.1fh", sleepDurationHours)
                        }
                        Text(
                            text = formattedDuration,
                            style = NumberM.copy(
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Tidur",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Material Slider controlling sleep duration (0..12 hours)
                Slider(
                    value = sleepDurationHours,
                    onValueChange = { sleepDurationHours = Math.round(it * 2f) / 2f }, // step in 0.5h
                    valueRange = 0f..12f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                        .testTag("sleep_duration_slider")
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "0 jam",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "12 jam",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // 4. FOOTER: PRIMARY BUTTON
            PrimaryButton(
                text = "Simpan Jurnal Pagi",
                onClick = {
                    onSave(selectedEmotions.toList(), sleepDurationHours)
                    onDismissRequest()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("save_morning_journal_button")
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun DailyCheckInBottomSheetPreview() {
    MindRestTheme {
        DailyCheckInBottomSheet(
            onDismissRequest = {},
            onSave = { _, _ -> }
        )
    }
}
