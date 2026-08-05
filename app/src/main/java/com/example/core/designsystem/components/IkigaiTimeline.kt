package com.example.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.MindRestTheme

/**
 * AIRecommendationCard displays personalized suggestions with bolded actions on the Ikigai screen.
 */
@Composable
fun AIRecommendationCard(
    body: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "ai_recommendation_card"
) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f))
            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), shape)
            .padding(16.dp)
            .testTag(testTag)
    ) {
        Column {
            Text(
                text = "AI RECOMMENDATION",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Spacer(modifier = Modifier.height(6.dp))
            body()
        }
    }
}

data class MilestoneItem(
    val title: String,
    val description: String,
    val isCompleted: Boolean
)

/**
 * MilestoneTimeline displays a visual vertical timeline connecting progress milestones.
 */
@Composable
fun MilestoneTimeline(
    items: List<MilestoneItem>,
    modifier: Modifier = Modifier,
    testTag: String = "milestone_timeline"
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        items.forEachIndexed { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Left Timeline Connector & Dot Column
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(32.dp)
                ) {
                    // Dot
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(
                                if (item.isCompleted) MaterialTheme.colorScheme.primary 
                                else MaterialTheme.colorScheme.surface
                            )
                            .border(
                                width = 2.dp,
                                color = if (item.isCompleted) MaterialTheme.colorScheme.primary 
                                        else MaterialTheme.colorScheme.outlineVariant,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (item.isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Completed",
                                tint = Color.White,
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }

                    // Vertical line connecting next dot
                    if (index < items.size - 1) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(44.dp)
                                .background(MaterialTheme.colorScheme.outlineVariant)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title + Description Content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(bottom = 8.dp)
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (item.isCompleted) MaterialTheme.colorScheme.onBackground 
                                else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IkigaiTimelinePreview() {
    MindRestTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AIRecommendationCard(
                body = {
                    Text(
                        text = "Explore educational programs to align your passion with real-world pay scales.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            )

            MilestoneTimeline(
                items = listOf(
                    MilestoneItem("Discover Love Pillar", "Log at least 3 things you love.", true),
                    MilestoneItem("Discover Career Focus", "Identify high-paying opportunities.", false)
                )
            )
        }
    }
}
