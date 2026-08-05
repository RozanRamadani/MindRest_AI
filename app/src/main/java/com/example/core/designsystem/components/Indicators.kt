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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.MindRestTheme
import com.example.core.designsystem.NumberM

/**
 * ProgressRing displays a percentage completion score inside a circular progress arc.
 */
@Composable
fun ProgressRing(
    progress: Float, // 0f to 1.0f
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = 7.dp,
    testTag: String = "progress_ring"
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow),
        label = "ProgressAnimation"
    )

    Box(
        modifier = modifier
            .size(size)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidthPx = strokeWidth.toPx()
            val diameter = size.toPx() - strokeWidthPx
            val topLeft = Offset(strokeWidthPx / 2, strokeWidthPx / 2)
            val rectSize = Size(diameter, diameter)

            // Background track (color at 20% opacity)
            drawArc(
                color = color.copy(alpha = 0.2f),
                startAngle = 270f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = rectSize,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )

            // Foreground progress arc
            drawArc(
                color = color,
                startAngle = 270f,
                sweepAngle = animatedProgress * 360f,
                useCenter = false,
                topLeft = topLeft,
                size = rectSize,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )
        }

        // Centered Text Label always using DM Mono Style (monospace)
        val percentText = "${(progress * 100).toInt()}%"
        Text(
            text = percentText,
            style = NumberM,
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = (size.value * 0.22f).sp
        )
    }
}

/**
 * ToggleSwitch is a binary custom switch using our designated shape tokens and sizes.
 */
@Composable
fun ToggleSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    testTag: String = "toggle_switch"
) {
    val thumbOffset by animateDpAsState(
        targetValue = if (checked) 24.dp else 4.dp,
        animationSpec = tween(150, easing = LinearOutSlowInEasing),
        label = "ThumbSlide"
    )

    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    val inactiveTrackColor = if (isDark) Color(0xFF2D3748) else Color(0xFFCBD5E0)
    val trackColor = if (checked) activeColor else inactiveTrackColor

    Box(
        modifier = modifier
            .size(width = 44.dp, height = 24.dp)
            .clip(CircleShape)
            .background(trackColor)
            .clickable { onCheckedChange(!checked) }
            .testTag(testTag),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .padding(start = thumbOffset)
                .size(16.dp)
                .clip(CircleShape)
                .background(Color.White)
        )
    }
}

/**
 * ToggleRow groups a Text Label (with optional subtitle) on the left
 * with a ToggleSwitch on the right.
 */
@Composable
fun ToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    activeColor: Color = MaterialTheme.colorScheme.primary,
    testTag: String = "toggle_row"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Medium
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        ToggleSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            activeColor = activeColor,
            testTag = "${testTag}_switch"
        )
    }
}

/**
 * Badge presents compact inline tags, status indicators, and counts.
 */
@Composable
fun Badge(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f),
    testTag: String = "badge"
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 2.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp
        )
    }
}

/**
 * SectionLabel displays uppercase section categories.
 */
@Composable
fun SectionLabel(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
    testTag: String = "section_label"
) {
    Text(
        text = text.uppercase(),
        color = color,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.3.sp,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .testTag(testTag)
    )
}

/**
 * SkeletonLoader mimics loading shapes with a smooth pulsing animation.
 */
@Composable
fun SkeletonLoader(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    testTag: String = "skeleton_loader"
) {
    val infiniteTransition = rememberInfiniteTransition(label = "SkeletonShimmer")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AlphaPulsing"
    )

    Box(
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha))
            .testTag(testTag)
    )
}

/**
 * EmptyState represents a visual screen block when no items are available.
 */
@Composable
fun EmptyState(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    illustration: @Composable (() -> Unit)? = null,
    testTag: String = "empty_state"
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (illustration != null) {
            illustration()
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        if (actionLabel != null && onActionClick != null) {
            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(
                text = actionLabel,
                onClick = onActionClick,
                modifier = Modifier.widthIn(min = 160.dp)
            )
        }
    }
}

/**
 * ErrorState visualizes networking or loading issues.
 */
@Composable
fun ErrorState(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "error_state"
) {
    EmptyState(
        title = "Something Went Wrong",
        description = message,
        actionLabel = "Retry",
        onActionClick = onRetryClick,
        illustration = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(48.dp)
            )
        },
        modifier = modifier,
        testTag = testTag
    )
}

@Preview(showBackground = true)
@Composable
fun IndicatorsPreview() {
    MindRestTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ProgressRing(progress = 0.45f, size = 72.dp)
                ProgressRing(progress = 0.82f, size = 80.dp)
            }
            ToggleRow(
                title = "Dark Mode",
                subtitle = "Optimized eye-friendly UI",
                checked = true,
                onCheckedChange = {}
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Badge(text = "Premium")
                Badge(text = "New Insight", color = Color.Magenta)
            }
            SectionLabel(text = "Today's Insights")
            SkeletonLoader(modifier = Modifier.fillMaxWidth().height(48.dp))
        }
    }
}
