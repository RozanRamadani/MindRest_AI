package com.example.features.settings.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.BodyFontFamily
import com.example.core.designsystem.MindRestTheme
import com.example.core.designsystem.NumberFontFamily
import com.example.core.designsystem.components.SectionLabel
import com.example.core.designsystem.components.ToggleRow
import com.example.core.designsystem.components.TopBar

@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit = {},
    dark: Boolean = isSystemInDarkTheme(),
    toggleDark: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Local State
    var notif by remember { mutableStateOf(true) }
    var aiPersonal by remember { mutableStateOf(true) }
    var dataShare by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("settings_screen")
    ) {
        // TopBar (Non-scrolling)
        TopBar(
            title = "Settings",
            onBackClick = onNavigateBack,
            testTag = "settings_top_bar"
        )

        // Scrollable Content Area (space-y-2 = 8.dp)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .testTag("settings_scroll_content"),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // SettingsSection 0: Appearance
            Column {
                SectionLabel(
                    text = "Appearance",
                    testTag = "section_label_appearance"
                )
                SectionCard {
                    ToggleRow(
                        title = "Dark Mode",
                        subtitle = "Night Sky theme",
                        checked = dark,
                        onCheckedChange = { toggleDark() },
                        testTag = "toggle_dark_mode"
                    )
                }
            }

            // SettingsSection 1: Notifications
            Column {
                SectionLabel(
                    text = "Notifications",
                    testTag = "section_label_notifications"
                )
                SectionCard {
                    ToggleRow(
                        title = "Push Notifications",
                        subtitle = "Reminders and insights",
                        checked = notif,
                        onCheckedChange = { notif = !notif },
                        testTag = "toggle_push_notifications"
                    )
                    SectionDivider()
                    ToggleRow(
                        title = "Sleep Reminders",
                        subtitle = null,
                        checked = notif,
                        onCheckedChange = {},
                        testTag = "toggle_sleep_reminders"
                    )
                    SectionDivider()
                    ToggleRow(
                        title = "Daily Check-in",
                        subtitle = null,
                        checked = notif,
                        onCheckedChange = {},
                        testTag = "toggle_daily_checkin"
                    )
                }
            }

            // SettingsSection 2: AI Personalization
            Column {
                SectionLabel(
                    text = "AI Personalization",
                    testTag = "section_label_ai_personalization"
                )
                SectionCard {
                    ToggleRow(
                        title = "AI Learning",
                        subtitle = "Let AI improve from your behavior",
                        checked = aiPersonal,
                        onCheckedChange = { aiPersonal = !aiPersonal },
                        testTag = "toggle_ai_learning"
                    )
                    SectionDivider()
                    ToggleRow(
                        title = "Share Anonymous Data",
                        subtitle = "Help improve MindRest AI",
                        checked = dataShare,
                        onCheckedChange = { dataShare = !dataShare },
                        activeColor = Color(0xFF34C98A), // Spec green active color
                        testTag = "toggle_share_anonymous_data"
                    )
                }
            }

            // SettingsSection 3: Account
            Column {
                SectionLabel(
                    text = "Account",
                    testTag = "section_label_account"
                )
                SectionCard {
                    AccountRow(item = "Language", testTag = "account_row_language")
                    AccountRow(item = "Privacy Policy", testTag = "account_row_privacy")
                    AccountRow(item = "Terms of Service", testTag = "account_row_terms")
                    AccountRow(item = "Export My Data", testTag = "account_row_export")
                }
            }

            // Footer Text
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
                    .testTag("settings_footer"),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "MindRest AI v1.0.0",
                    fontFamily = NumberFontFamily,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Made with ❤️ for mental wellness",
                    fontFamily = BodyFontFamily,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

/**
 * SectionCard wraps rows in a rounded card with horizontal padding only (px-4).
 */
@Composable
private fun SectionCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp) // px-4 = 16dp horizontal only
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            content = content
        )
    }
}

/**
 * SectionDivider is an explicit 1dp divider line using border token color.
 */
@Composable
private fun SectionDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(MaterialTheme.colorScheme.outlineVariant)
    )
}

/**
 * AccountRow renders a full-width clickable button row with chevron.
 */
@Composable
private fun AccountRow(
    item: String,
    onClick: () -> Unit = {},
    testTag: String = "account_row"
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp)
            .testTag(testTag),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item,
            fontFamily = BodyFontFamily,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenLightPreview() {
    MindRestTheme(darkTheme = false) {
        SettingsScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenDarkPreview() {
    MindRestTheme(darkTheme = true) {
        SettingsScreen()
    }
}

