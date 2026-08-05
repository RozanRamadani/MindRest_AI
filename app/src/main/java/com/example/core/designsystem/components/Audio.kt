package com.example.core.designsystem.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.MindRestTheme
import kotlin.math.sin

/**
 * WaveformVisualizer draws 50 vertical bars that animate organically to represent active playback.
 */
@Composable
fun WaveformVisualizer(
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    testTag: String = "waveform_visualizer"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "WaveformAnimation")
    
    // We animate a single offset factor to drive organic sine-wave propagation across 50 bars.
    val animationOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "WaveformOffset"
    )

    val primaryColor = MaterialTheme.colorScheme.primary
    val primaryLightColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp)
            .testTag(testTag)
    ) {
        val barCount = 50
        val gap = 2.dp.toPx()
        val totalWidth = size.width
        val barWidth = (totalWidth - (barCount - 1) * gap) / barCount
        val maxHeight = size.height

        for (i in 0 until barCount) {
            // Static height formula + dynamic offset factor when playing
            val factor = if (isPlaying) {
                0.2f + 0.3f * sin(i * 0.5f + animationOffset) + 0.3f * sin(i * 0.2f - animationOffset)
            } else {
                0.2f + 0.15f * sin(i * 0.5f) // Paused frozen state
            }
            
            // Constrain heights to 20% - 80% range
            val barHeight = (factor.coerceIn(0.2f, 0.8f)) * maxHeight
            val x = i * (barWidth + gap)
            val y = (maxHeight - barHeight) / 2f

            // First 20 bars use primary, remaining 30 use primaryLight
            val barColor = if (i < 20) primaryColor else primaryLightColor

            drawRoundRect(
                color = barColor,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f)
            )
        }
    }
}

/**
 * FeaturedAudioPlayer displays current track details, transport controls, and dynamic waveforms.
 */
@Composable
fun FeaturedAudioPlayer(
    title: String,
    category: String,
    emoji: String,
    isPlaying: Boolean,
    progress: Float, // 0f to 1f
    currentTimeText: String,
    durationText: String,
    onPlayPauseClick: () -> Unit,
    onSkipBackClick: () -> Unit,
    onSkipForwardClick: () -> Unit,
    onShuffleClick: () -> Unit,
    onRepeatClick: () -> Unit,
    modifier: Modifier = Modifier,
    isShuffleActive: Boolean = false,
    isRepeatActive: Boolean = false,
    testTag: String = "featured_audio_player"
) {
    val bgBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF0D1A2E), // Custom navy
            Color(0xFF1A1040)  // Custom deep purple
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
            .background(bgBrush)
            .padding(20.dp)
            .testTag(testTag)
    ) {
        Column {
            // Track Info Row
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(com.example.core.designsystem.FeatureRelaxation.copy(alpha = 0.14f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji, fontSize = 28.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = category.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.6f),
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = title,
                        fontFamily = MaterialTheme.typography.headlineMedium.fontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Waveform Visualizer
            WaveformVisualizer(isPlaying = isPlaying, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(20.dp))

            // Progress Bar Track
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.15f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.primary)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Time Displays
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = currentTimeText, color = Color.White.copy(alpha = 0.6f), style = MaterialTheme.typography.labelMedium)
                Text(text = durationText, color = Color.White.copy(alpha = 0.6f), style = MaterialTheme.typography.labelMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Transport Controls
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Shuffle Button
                Text(
                    text = "🔀",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .clickable(onClick = onShuffleClick)
                        .padding(8.dp)
                        .testTag("${testTag}_shuffle")
                )

                // Skip Back Button
                Text(
                    text = "⏮",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .clickable(onClick = onSkipBackClick)
                        .padding(8.dp)
                        .testTag("${testTag}_skip_back")
                )

                // Play / Pause Circle
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .clickable(onClick = onPlayPauseClick)
                        .testTag("${testTag}_play_pause"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isPlaying) "⏸" else "▶",
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }

                // Skip Forward Button
                Text(
                    text = "⏭",
                    fontSize = 20.sp,
                    modifier = Modifier
                        .clickable(onClick = onSkipForwardClick)
                        .padding(8.dp)
                        .testTag("${testTag}_skip_forward")
                )

                // Repeat Button
                Text(
                    text = "🔁",
                    fontSize = 18.sp,
                    modifier = Modifier
                        .clickable(onClick = onRepeatClick)
                        .padding(8.dp)
                        .testTag("${testTag}_repeat")
                )
            }
        }
    }
}

/**
 * AudioTrackRow is a list item representing a relaxation audio track.
 */
@Composable
fun AudioTrackRow(
    title: String,
    category: String,
    duration: String,
    emoji: String,
    isActive: Boolean,
    isPlaying: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "audio_track_row"
) {
    val containerColor = if (isActive) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surface
    val borderColor = if (isActive) MaterialTheme.colorScheme.primary.copy(alpha = 0.25f) else MaterialTheme.colorScheme.outlineVariant

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(containerColor)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(12.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(com.example.core.designsystem.FeatureRelaxation.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = emoji, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "$category · $duration",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = if (isActive && isPlaying) "Pause" else "Play",
                color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.testTag("${testTag}_play_action")
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AudioPreview() {
    MindRestTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FeaturedAudioPlayer(
                title = "Ocean Waves",
                category = "Nature",
                emoji = "🌊",
                isPlaying = true,
                progress = 0.33f,
                currentTimeText = "15:00",
                durationText = "45:00",
                onPlayPauseClick = {},
                onSkipBackClick = {},
                onSkipForwardClick = {},
                onShuffleClick = {},
                onRepeatClick = {}
            )
            AudioTrackRow(
                title = "Ocean Waves",
                category = "Nature",
                duration = "45:00",
                emoji = "🌊",
                isActive = true,
                isPlaying = true,
                onClick = {}
            )
        }
    }
}
