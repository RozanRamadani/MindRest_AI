package com.example.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.MindRestTheme

/**
 * ReminderItem represents a compact summary item inside Home dashboard lists.
 */
@Composable
fun ReminderItem(
    title: String,
    timeText: String,
    isCompleted: Boolean,
    onToggleClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "reminder_item"
) {
    val alpha = if (isCompleted) 0.6f else 1.0f
    val textDecoration = if (isCompleted) TextDecoration.LineThrough else TextDecoration.None

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp)
            .alpha(alpha)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Bullet dot indicator
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(
                        if (isCompleted) MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                        else MaterialTheme.colorScheme.primary
                    )
                    .clickable(onClick = onToggleClick)
                    .testTag("${testTag}_dot")
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                textDecoration = textDecoration,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Text(
            text = timeText,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
            style = MaterialTheme.typography.bodySmall
        )
    }
}

/**
 * RemindersSection aggregates multiple reminders on the dashboard with a clean standard card background.
 */
@Composable
fun RemindersSection(
    reminders: List<ReminderItemData>,
    onReminderToggle: (Int) -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "reminders_section"
) {
    BaseCard(
        modifier = modifier.testTag(testTag),
        radius = 16.dp,
        padding = 16.dp
    ) {
        Text(
            text = "Today's Reminders",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(12.dp))

        if (reminders.isEmpty()) {
            Text(
                text = "No reminders set for today.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                reminders.forEachIndexed { index, data ->
                    ReminderItem(
                        title = data.title,
                        timeText = data.timeText,
                        isCompleted = data.isCompleted,
                        onToggleClick = { onReminderToggle(index) }
                    )
                }
            }
        }
    }
}

data class ReminderItemData(
    val title: String,
    val timeText: String,
    val isCompleted: Boolean
)

/**
 * TimelineReminder couples vertical timeline connector dots with customized ReminderCards.
 */
@Composable
fun TimelineReminder(
    title: String,
    timeText: String,
    emoji: String,
    color: Color,
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "timeline_reminder"
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Timeline Dot
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(if (enabled) color.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant)
                .border(2.dp, if (enabled) color else MaterialTheme.colorScheme.outlineVariant, CircleShape)
                .testTag("${testTag}_dot"),
            contentAlignment = Alignment.Center
        ) {
            Text(text = emoji, fontSize = 18.sp)
        }

        // Reminder Card
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(
                    width = 1.dp,
                    color = if (enabled) color.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outlineVariant,
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(12.dp)
                .testTag("${testTag}_card")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = timeText,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                ToggleSwitch(
                    checked = enabled,
                    onCheckedChange = onEnabledChange,
                    activeColor = color,
                    testTag = "${testTag}_switch"
                )
            }
        }
    }
}

/**
 * TimelineConnectorLine is a simple visual vertical rule.
 */
@Composable
fun TimelineConnectorLine(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outlineVariant
) {
    Box(
        modifier = modifier
            .width(2.dp)
            .fillMaxHeight()
            .background(color)
    )
}

@Preview(showBackground = true)
@Composable
fun ReminderPreview() {
    MindRestTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            RemindersSection(
                reminders = listOf(
                    ReminderItemData("Morning walk", "07:30 AM", true),
                    ReminderItemData("Breathing exercises", "09:00 PM", false)
                ),
                onReminderToggle = {}
            )

            TimelineReminder(
                title = "Sleep Habit Routine",
                timeText = "10:30 PM",
                emoji = "💤",
                color = com.example.core.designsystem.FeatureReminder,
                enabled = true,
                onEnabledChange = {}
            )
        }
    }
}
