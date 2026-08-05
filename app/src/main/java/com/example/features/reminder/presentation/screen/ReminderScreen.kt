package com.example.features.reminder.presentation.screen

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.core.designsystem.*
import com.example.core.designsystem.components.*
import com.example.features.reminder.BedtimeNotificationHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var isBedtimeReminderEnabled by remember { mutableStateOf(true) }
    var isMorningCheckInEnabled by remember { mutableStateOf(true) }
    var isMiddayResetEnabled by remember { mutableStateOf(false) }
    var isEveningJournalEnabled by remember { mutableStateOf(true) }

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                true
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasNotificationPermission = isGranted
        if (isGranted) {
            Toast.makeText(context, "Notification permission granted", Toast.LENGTH_SHORT).show()
            BedtimeNotificationHelper.scheduleBedtimeNotification(context)
        } else {
            Toast.makeText(context, "Permission denied. Notifications will not show.", Toast.LENGTH_LONG).show()
        }
    }

    // Schedule notification when enabled
    LaunchedEffect(isBedtimeReminderEnabled) {
        if (isBedtimeReminderEnabled) {
            BedtimeNotificationHelper.scheduleBedtimeNotification(context)
        } else {
            BedtimeNotificationHelper.cancelBedtimeNotification(context)
        }
    }

    Scaffold(
        topBar = {
            TopBar(
                title = "Reminder Settings",
                onBackClick = onNavigateBack,
                testTag = "reminder_top_bar"
            )
        },
        modifier = modifier.fillMaxSize().testTag("reminder_screen")
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Permission Banner if Android 13+ and not granted
            if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                BaseCard(
                    radius = 16.dp,
                    padding = 16.dp,
                    modifier = Modifier.background(
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(16.dp)
                    ),
                    testTag = "permission_card"
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Notification Permission Required",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                        Text(
                            text = "To receive wind-down prompts 30 minutes before bedtime, please allow notifications for MindRest.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        PrimaryButton(
                            text = "Grant Permission",
                            onClick = { permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) },
                            modifier = Modifier.fillMaxWidth().height(36.dp)
                        )
                    }
                }
            }

            // 1. Bedtime Wind-Down Notification Scheduler Card
            BaseCard(
                radius = 20.dp,
                padding = 16.dp,
                testTag = "bedtime_scheduler_card"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Bedtime,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Bedtime Wind-Down Prompt",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "30 mins before ideal bedtime",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Switch(
                            checked = isBedtimeReminderEnabled,
                            onCheckedChange = { enabled ->
                                isBedtimeReminderEnabled = enabled
                                if (enabled && !hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                }
                            },
                            modifier = Modifier.testTag("bedtime_switch")
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                    // Schedule Breakdown Info
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Ideal Bedtime Target",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "11:15 PM",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Notification Scheduled",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = if (isBedtimeReminderEnabled) "10:45 PM Daily" else "Disabled",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isBedtimeReminderEnabled) SuccessColor else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Status Chip
                    if (isBedtimeReminderEnabled) {
                        Badge(
                            text = "⏰ Active: Alarm scheduled at 10:45 PM daily",
                            color = SuccessColor,
                            backgroundColor = SuccessColor.copy(alpha = 0.15f)
                        )
                    }

                    // Test Action Button
                    SecondaryButton(
                        text = "Trigger Test Notification Now",
                        onClick = {
                            if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                            } else {
                                BedtimeNotificationHelper.triggerTestNotification(context)
                                Toast.makeText(context, "Test notification sent!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().testTag("test_notification_btn")
                    )
                }
            }

            // 2. Additional Mindful Routine Reminders
            BaseCard(
                radius = 20.dp,
                padding = 16.dp,
                testTag = "routine_reminders_card"
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    SectionLabel(text = "Daily Routine Reminders")

                    ToggleRow(
                        title = "Morning Mindful Check-in (07:30 AM)",
                        subtitle = "Gentle alert to record mood and intentions upon waking",
                        checked = isMorningCheckInEnabled,
                        onCheckedChange = { isMorningCheckInEnabled = it }
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                    ToggleRow(
                        title = "Midday Reset & Breathing (01:00 PM)",
                        subtitle = "Prompt for a 3-minute diaphragmatic breathing pause",
                        checked = isMiddayResetEnabled,
                        onCheckedChange = { isMiddayResetEnabled = it }
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

                    ToggleRow(
                        title = "Evening Reflection & Journal (09:00 PM)",
                        subtitle = "CBT-based journaling prompt to clear thoughts before sleep",
                        checked = isEveningJournalEnabled,
                        onCheckedChange = { isEveningJournalEnabled = it }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReminderPreview() {
    MindRestTheme {
        ReminderScreen(onNavigateBack = {})
    }
}
