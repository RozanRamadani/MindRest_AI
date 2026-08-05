package com.example.core.designsystem.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.MindRestTheme
import com.example.core.designsystem.NumberL
import com.example.core.designsystem.NumberM
import com.example.core.designsystem.NumberXl

/**
 * BaseCard represents the generic unified container matching our standard visual hierarchy.
 */
@Composable
fun BaseCard(
    modifier: Modifier = Modifier,
    radius: Dp = 16.dp,
    padding: Dp = 16.dp,
    onClick: (() -> Unit)? = null,
    testTag: String = "base_card",
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(radius)
    val cardModifier = if (onClick != null) {
        modifier
            .clip(shape)
            .clickable(onClick = onClick)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
            .background(MaterialTheme.colorScheme.surface)
            .padding(padding)
    } else {
        modifier
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, shape)
            .background(MaterialTheme.colorScheme.surface)
            .padding(padding)
    }

    Column(
        modifier = cardModifier.testTag(testTag)
    ) {
        content()
    }
}

/**
 * SleepScoreCard displays the nightly Sleep Score with custom radial progress and hours.
 */
@Composable
fun SleepScoreCard(
    score: Int,
    hours: Int,
    minutes: Int,
    modifier: Modifier = Modifier,
    subtitle: String = "Last Night's Sleep",
    large: Boolean = true,
    testTag: String = "sleep_score_card"
) {
    val bgBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            MaterialTheme.colorScheme.surface
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp))
            .background(bgBrush)
            .padding(20.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    Text(text = "$hours", style = NumberXl, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.alignByBaseline())
                    Text(text = "h", style = NumberM, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.alignByBaseline())
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "$minutes", style = NumberXl, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface, modifier = Modifier.alignByBaseline())
                    Text(text = "m", style = NumberM, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.alignByBaseline())
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            ProgressRing(
                progress = score / 100f,
                size = if (large) 88.dp else 72.dp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * MetricTile represents compact wellness statistics.
 */
@Composable
fun MetricTile(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    iconColor: Color = MaterialTheme.colorScheme.primary,
    testTag: String = "metric_tile"
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .padding(12.dp)
            .testTag(testTag)
    ) {
        Column {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = value,
                style = NumberL,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * AIInsightCard features automated daily insights with navigation options.
 */
@Composable
fun AIInsightCard(
    insight: String,
    onSeeAllClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "ai_insight_card"
) {
    val bgBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
            MaterialTheme.colorScheme.background
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp))
            .background(bgBrush)
            .padding(20.dp)
            .testTag(testTag)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                MoonLogo(size = 36.dp)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "AI Sleep Insights",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = insight,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            SecondaryButton(
                text = "See All AI Recommendations",
                onClick = onSeeAllClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/**
 * QuickActionTile is a tappable square feature link.
 */
@Composable
fun QuickActionTile(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    testTag: String = "quick_action_tile"
) {
    Box(
        modifier = modifier
            .size(75.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(color.copy(alpha = 0.15f))
            .clickable(onClick = onClick)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = color,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                color = color,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp
            )
        }
    }
}

/**
 * PurposeScoreCard displays the Ikigai purpose scale value.
 */
@Composable
fun PurposeScoreCard(
    score: Int,
    description: String,
    modifier: Modifier = Modifier,
    testTag: String = "purpose_score_card"
) {
    val bgBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.25f),
            MaterialTheme.colorScheme.surface
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(24.dp))
            .background(bgBrush)
            .padding(20.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Purpose Score",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            ProgressRing(
                progress = score / 100f,
                size = 80.dp,
                color = com.example.core.designsystem.FeatureJourney
            )
        }
    }
}

/**
 * IkigaiPillarCard details progress for Love, Good At, World Needs, or Pays You.
 */
@Composable
fun IkigaiPillarCard(
    title: String,
    percent: Int,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    items: List<String> = emptyList(),
    testTag: String = "ikigai_pillar_card"
) {
    val animatedPercent by animateFloatAsState(
        targetValue = (percent / 100f).coerceIn(0f, 1f),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "PillarProgressAnim"
    )

    BaseCard(
        modifier = modifier.testTag(testTag),
        radius = 16.dp,
        padding = 12.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
            }
            Text(
                text = "$percent%",
                style = NumberM,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Mini progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.2f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(animatedPercent)
                    .fillMaxHeight()
                    .background(color)
            )
        }

        if (items.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items.forEach { item ->
                    Badge(text = item, color = color, backgroundColor = color.copy(alpha = 0.1f))
                }
            }
        }
    }
}

/**
 * GoalCard manages individual habit logging progress metrics.
 */
@Composable
fun GoalCard(
    title: String,
    progress: Float, // 0f to 1f
    progressText: String,
    icon: ImageVector,
    color: Color,
    onBadgeClick: () -> Unit,
    modifier: Modifier = Modifier,
    isComplete: Boolean = false,
    testTag: String = "goal_card"
) {
    val borderColor = if (isComplete) color.copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioLowBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "GoalCardProgressAnim"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, borderColor, RoundedCornerShape(16.dp))
            .padding(16.dp)
            .testTag(testTag)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(color.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = progressText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Plus or check badge
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(if (isComplete) color.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f))
                        .clickable(onClick = onBadgeClick),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isComplete) Icons.Default.Check else Icons.Default.Add,
                        contentDescription = "Increment",
                        tint = if (isComplete) color else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Goal progress bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.2f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress)
                        .fillMaxHeight()
                        .background(color)
                )
            }
        }
    }
}

/**
 * RecommendationCard details AI-driven tips with categorized headers.
 */
@Composable
fun RecommendationCard(
    category: String,
    title: String,
    body: String,
    icon: ImageVector,
    color: Color,
    tags: List<String>,
    modifier: Modifier = Modifier,
    testTag: String = "recommendation_card"
) {
    var feedbackState by remember { mutableStateOf<Boolean?>(null) }

    BaseCard(
        modifier = modifier.testTag(testTag),
        radius = 16.dp,
        padding = 16.dp
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = category.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = color,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.3.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 20.sp
        )

        if (tags.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                tags.forEach { tag ->
                    Badge(text = tag, color = color, backgroundColor = color.copy(alpha = 0.1f))
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when (feedbackState) {
                    true -> "Thanks for your feedback!"
                    false -> "Feedback recorded"
                    null -> "Was this recommendation helpful?"
                },
                style = MaterialTheme.typography.labelSmall,
                color = if (feedbackState != null) color else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                fontSize = 11.sp,
                fontWeight = if (feedbackState != null) FontWeight.Medium else FontWeight.Normal
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        feedbackState = if (feedbackState == true) null else true
                    },
                    modifier = Modifier
                        .size(28.dp)
                        .testTag("reco_card_helpful_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.ThumbUp,
                        contentDescription = "Helpful",
                        tint = if (feedbackState == true) Color(0xFF34C98A)
                               else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                }

                IconButton(
                    onClick = {
                        feedbackState = if (feedbackState == false) null else false
                    },
                    modifier = Modifier
                        .size(28.dp)
                        .testTag("reco_card_not_helpful_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.ThumbDown,
                        contentDescription = "Not Helpful",
                        tint = if (feedbackState == false) MaterialTheme.colorScheme.error
                               else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}

/**
 * StatCard displays metrics combined with positive/negative trend overlays.
 */
@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    trend: String? = null,
    trendPositive: Boolean = true,
    testTag: String = "stat_card"
) {
    BaseCard(
        modifier = modifier.testTag(testTag),
        radius = 16.dp,
        padding = 16.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
            if (trend != null) {
                Badge(
                    text = trend,
                    color = if (trendPositive) com.example.core.designsystem.SuccessColor else com.example.core.designsystem.DarkDestructive,
                    backgroundColor = if (trendPositive) com.example.core.designsystem.SuccessColor.copy(alpha = 0.15f) else com.example.core.designsystem.DarkDestructive.copy(alpha = 0.15f)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = value,
            style = NumberXl,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/**
 * ProgressSummaryCard is used on Lifestyle layout to overview total goal accomplishments.
 */
@Composable
fun ProgressSummaryCard(
    completed: Int,
    total: Int,
    modifier: Modifier = Modifier,
    testTag: String = "progress_summary_card"
) {
    val bgBrush = Brush.linearGradient(
        colors = listOf(
            com.example.core.designsystem.FeatureLifestyle.copy(alpha = 0.2f),
            com.example.core.designsystem.FeatureJourney.copy(alpha = 0.1f)
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, com.example.core.designsystem.FeatureLifestyle.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
            .background(bgBrush)
            .padding(20.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "TODAY'S PROGRESS",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.3.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "$completed of $total Goals",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${total - completed} goals remaining today",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            ProgressRing(
                progress = if (total > 0) completed / total.toFloat() else 0f,
                size = 80.dp,
                color = com.example.core.designsystem.FeatureLifestyle
            )
        }
    }
}

enum class SleepTimeframe(val label: String) {
    WEEKLY("Weekly"),
    MONTHLY("Monthly"),
    YEARLY("Yearly")
}

/**
 * SleepInsightsCard displays sleep quality trends with a segmented filter for Weekly, Monthly, and Yearly views.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepInsightsCard(
    modifier: Modifier = Modifier,
    initialTimeframe: SleepTimeframe = SleepTimeframe.WEEKLY,
    testTag: String = "sleep_insights_card"
) {
    var selectedTimeframe by remember { mutableStateOf(initialTimeframe) }
    var tipFeedbackState by remember { mutableStateOf<Boolean?>(null) }
    var showCalculationSheet by remember { mutableStateOf(false) }

    val weeklyScores = listOf(72, 78, 65, 82, 80, 88, 84)
    val weeklyLabels = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

    val monthlyScores = listOf(74f, 78f, 81f, 85f)
    val monthlyLabels = listOf("Week 1", "Week 2", "Week 3", "Week 4")

    val yearlyScores = listOf(70f, 72f, 74f, 78f, 80f, 82f, 81f, 83f, 85f, 84f, 86f, 88f)
    val yearlyLabels = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    val avgScore = when (selectedTimeframe) {
        SleepTimeframe.WEEKLY -> weeklyScores.average().toFloat()
        SleepTimeframe.MONTHLY -> monthlyScores.average().toFloat()
        SleepTimeframe.YEARLY -> yearlyScores.average().toFloat()
    }

    val deepSleepPercent = when (selectedTimeframe) {
        SleepTimeframe.WEEKLY -> "28%"
        SleepTimeframe.MONTHLY -> "26%"
        SleepTimeframe.YEARLY -> "27%"
    }

    val consistency = when (selectedTimeframe) {
        SleepTimeframe.WEEKLY -> "92%"
        SleepTimeframe.MONTHLY -> "88%"
        SleepTimeframe.YEARLY -> "85%"
    }

    val weeklyCallouts = listOf(
        ChartCallout(index = 2, text = "⚠️ Dip 65", isMilestone = false),
        ChartCallout(index = 5, text = "⭐ Peak 88", isMilestone = true)
    )

    val monthlyCallouts = listOf(
        ChartCallout(index = 0, text = "Base 74", isMilestone = false),
        ChartCallout(index = 3, text = "Goal 85", isMilestone = true)
    )

    val yearlyCallouts = listOf(
        ChartCallout(index = 0, text = "Min 70", isMilestone = false),
        ChartCallout(index = 11, text = "Peak 88", isMilestone = true)
    )

    BaseCard(
        modifier = modifier.testTag(testTag),
        radius = 20.dp,
        padding = 16.dp
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { showCalculationSheet = true }
                        .padding(vertical = 2.dp)
                        .testTag("sleep_insights_title_btn")
                ) {
                    Text(
                        text = "SLEEP QUALITY TRENDS",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.2.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Sleep Insights",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Calculation info",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Segmented control filter (Weekly / Monthly / Yearly)
                SegmentedControl(
                    items = SleepTimeframe.values().toList(),
                    selectedItem = selectedTimeframe,
                    onItemSelected = { selectedTimeframe = it },
                    itemLabel = { it.label },
                    modifier = Modifier.width(190.dp),
                    testTag = "sleep_insights_timeframe"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Score Summary
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "Average Score",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${avgScore.toInt()}",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "/100",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 4.dp, start = 2.dp)
                        )
                    }
                }

                Badge(
                    text = when {
                        avgScore >= 80 -> "Optimal"
                        avgScore >= 70 -> "Good"
                        else -> "Fair"
                    },
                    color = if (avgScore >= 80) Color(0xFF34C98A) else MaterialTheme.colorScheme.primary,
                    backgroundColor = if (avgScore >= 80) Color(0xFF34C98A).copy(alpha = 0.15f) else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f),
                    testTag = "sleep_quality_status_badge"
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Chart area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(105.dp)
            ) {
                when (selectedTimeframe) {
                    SleepTimeframe.WEEKLY -> {
                        WeeklySleepBarChart(
                            scores = weeklyScores,
                            averageScore = avgScore,
                            callouts = weeklyCallouts,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    SleepTimeframe.MONTHLY -> {
                        AreaTrendChart(
                            values = monthlyScores,
                            color = MaterialTheme.colorScheme.primary,
                            callouts = monthlyCallouts,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                    SleepTimeframe.YEARLY -> {
                        AreaTrendChart(
                            values = yearlyScores,
                            color = com.example.core.designsystem.FeatureJourney,
                            callouts = yearlyCallouts,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            // Axis labels
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val labels = when (selectedTimeframe) {
                    SleepTimeframe.WEEKLY -> weeklyLabels
                    SleepTimeframe.MONTHLY -> monthlyLabels
                    SleepTimeframe.YEARLY -> yearlyLabels
                }
                labels.forEach { label ->
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        fontSize = 10.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))

            // Stats footer row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Deep Sleep",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = deepSleepPercent,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Consistency",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = consistency,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "View Mode",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = selectedTimeframe.label,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))

            // Contextual Sleep Tip Section below Insights Chart
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(com.example.core.designsystem.FeatureJourney.copy(alpha = 0.08f))
                    .border(
                        1.dp,
                        com.example.core.designsystem.FeatureJourney.copy(alpha = 0.2f),
                        RoundedCornerShape(14.dp)
                    )
                    .padding(12.dp)
                    .testTag("sleep_contextual_tip_section"),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(com.example.core.designsystem.FeatureJourney.copy(alpha = 0.18f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "Sleep Tip",
                        tint = com.example.core.designsystem.FeatureJourney,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CONTEXTUAL SLEEP TIP",
                            style = MaterialTheme.typography.labelSmall,
                            color = com.example.core.designsystem.FeatureJourney,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp,
                            letterSpacing = 1.sp
                        )

                        Badge(
                            text = when (selectedTimeframe) {
                                SleepTimeframe.WEEKLY -> "Actionable Tip"
                                SleepTimeframe.MONTHLY -> "Monthly Insight"
                                SleepTimeframe.YEARLY -> "Pattern Analysis"
                            },
                            color = com.example.core.designsystem.FeatureJourney,
                            backgroundColor = com.example.core.designsystem.FeatureJourney.copy(alpha = 0.15f),
                            testTag = "sleep_tip_badge"
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = when (selectedTimeframe) {
                            SleepTimeframe.WEEKLY -> "Mid-Week Deep Sleep Dip Detected"
                            SleepTimeframe.MONTHLY -> "Consistent Bedtime Pattern Observed"
                            SleepTimeframe.YEARLY -> "Seasonal Sleep Efficiency Boost"
                        },
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 13.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = when (selectedTimeframe) {
                            SleepTimeframe.WEEKLY -> "Your deep sleep was 18% lower on Wednesday following an irregular bedtime. Shifting bedtime 20 minutes earlier on weeknights improves sleep consistency."
                            SleepTimeframe.MONTHLY -> "Your average sleep score increased by +7 points this month. Maintaining your 10:30 PM wind-down routine is keeping your REM cycles balanced."
                            SleepTimeframe.YEARLY -> "You achieve 12% higher sleep efficiency during summer months. Exposure to morning sunlight within 30 minutes of waking supports year-round circadian alignment."
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (tipFeedbackState) {
                                true -> "Thanks for your feedback!"
                                false -> "Feedback recorded"
                                null -> "Was this tip helpful?"
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = if (tipFeedbackState != null) com.example.core.designsystem.FeatureJourney else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                            fontSize = 11.sp,
                            fontWeight = if (tipFeedbackState != null) FontWeight.Medium else FontWeight.Normal
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = {
                                    tipFeedbackState = if (tipFeedbackState == true) null else true
                                },
                                modifier = Modifier
                                    .size(28.dp)
                                    .testTag("sleep_tip_helpful_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ThumbUp,
                                    contentDescription = "Helpful",
                                    tint = if (tipFeedbackState == true) Color(0xFF34C98A)
                                           else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            IconButton(
                                onClick = {
                                    tipFeedbackState = if (tipFeedbackState == false) null else false
                                },
                                modifier = Modifier
                                    .size(28.dp)
                                    .testTag("sleep_tip_not_helpful_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ThumbDown,
                                    contentDescription = "Not Helpful",
                                    tint = if (tipFeedbackState == false) MaterialTheme.colorScheme.error
                                           else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCalculationSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            onDismissRequest = { showCalculationSheet = false },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .testTag("sleep_calculation_bottom_sheet")
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Sleep Quality Calculation",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "How daily score & trend values are derived",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    IconButton(
                        onClick = { showCalculationSheet = false },
                        modifier = Modifier.testTag("close_sleep_calc_sheet_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Your Sleep Insights score (0–100) is synthesized from 4 biometric & behavioral pillars to reflect overall sleep health:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                CalculationMetricItem(
                    title = "1. Total Sleep Duration (35%)",
                    description = "Evaluates total hours slept against your targeted sleep goal (baseline 7.5–9 hours for optimal restoration)."
                )

                Spacer(modifier = Modifier.height(10.dp))

                CalculationMetricItem(
                    title = "2. Deep & REM Sleep Ratio (30%)",
                    description = "Measures proportion of slow-wave N3 deep sleep (physical repair) and REM sleep (cognitive & emotional processing)."
                )

                Spacer(modifier = Modifier.height(10.dp))

                CalculationMetricItem(
                    title = "3. Sleep Efficiency (20%)",
                    description = "Ratio of actual time spent sleeping versus total time in bed, penalizing long latency and night awakenings."
                )

                Spacer(modifier = Modifier.height(10.dp))

                CalculationMetricItem(
                    title = "4. Circadian Consistency (15%)",
                    description = "Tracks regularity of bedtime and wake-up times relative to your rolling 7-day baseline average."
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { showCalculationSheet = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("understand_sleep_calc_btn"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Got It", fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun CalculationMetricItem(
    title: String,
    description: String
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )
        }
    }
}

/**
 * Standalone SleepTipCard offering personalized contextual advice based on trend analysis.
 */
@Composable
fun SleepTipCard(
    timeframe: SleepTimeframe = SleepTimeframe.WEEKLY,
    modifier: Modifier = Modifier,
    testTag: String = "sleep_tip_card"
) {
    var feedbackState by remember { mutableStateOf<Boolean?>(null) }

    BaseCard(
        modifier = modifier.testTag(testTag),
        radius = 20.dp,
        padding = 16.dp
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(com.example.core.designsystem.FeatureJourney.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = com.example.core.designsystem.FeatureJourney,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CONTEXTUAL SLEEP TIP",
                        style = MaterialTheme.typography.labelSmall,
                        color = com.example.core.designsystem.FeatureJourney,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.1.sp,
                        fontSize = 10.sp
                    )

                    Badge(
                        text = when (timeframe) {
                            SleepTimeframe.WEEKLY -> "Actionable Tip"
                            SleepTimeframe.MONTHLY -> "Monthly Insight"
                            SleepTimeframe.YEARLY -> "Pattern Analysis"
                        },
                        color = com.example.core.designsystem.FeatureJourney,
                        backgroundColor = com.example.core.designsystem.FeatureJourney.copy(alpha = 0.12f),
                        testTag = "standalone_sleep_tip_badge"
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = when (timeframe) {
                        SleepTimeframe.WEEKLY -> "Mid-Week Deep Sleep Dip Detected"
                        SleepTimeframe.MONTHLY -> "Consistent Bedtime Pattern Observed"
                        SleepTimeframe.YEARLY -> "Seasonal Sleep Efficiency Boost"
                    },
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = when (timeframe) {
                        SleepTimeframe.WEEKLY -> "Your deep sleep was 18% lower on Wednesday following an irregular bedtime. Shifting bedtime 20 minutes earlier on weeknights improves sleep consistency."
                        SleepTimeframe.MONTHLY -> "Your average sleep score increased by +7 points this month. Maintaining your 10:30 PM wind-down routine is keeping your REM cycles balanced."
                        SleepTimeframe.YEARLY -> "You achieve 12% higher sleep efficiency during summer months. Exposure to morning sunlight within 30 minutes of waking supports year-round circadian alignment."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = when (feedbackState) {
                            true -> "Thanks for your feedback!"
                            false -> "Feedback recorded"
                            null -> "Was this tip helpful?"
                        },
                        style = MaterialTheme.typography.labelSmall,
                        color = if (feedbackState != null) com.example.core.designsystem.FeatureJourney else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontWeight = if (feedbackState != null) FontWeight.Medium else FontWeight.Normal
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = {
                                feedbackState = if (feedbackState == true) null else true
                            },
                            modifier = Modifier
                                .size(28.dp)
                                .testTag("standalone_sleep_tip_helpful_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ThumbUp,
                                contentDescription = "Helpful",
                                tint = if (feedbackState == true) Color(0xFF34C98A)
                                       else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        IconButton(
                            onClick = {
                                feedbackState = if (feedbackState == false) null else false
                            },
                            modifier = Modifier
                                .size(28.dp)
                                .testTag("standalone_sleep_tip_not_helpful_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ThumbDown,
                                contentDescription = "Not Helpful",
                                tint = if (feedbackState == false) MaterialTheme.colorScheme.error
                                       else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                modifier = Modifier.size(16.dp)
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
fun CardsPreview() {
    MindRestTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SleepScoreCard(score = 82, hours = 7, minutes = 23)
            SleepInsightsCard()
            AIInsightCard(insight = "Your sleep quality was excellent. Keep it up!", onSeeAllClick = {})
            ProgressSummaryCard(completed = 4, total = 6)
        }
    }
}
