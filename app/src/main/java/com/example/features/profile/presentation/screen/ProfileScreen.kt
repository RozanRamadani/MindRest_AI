package com.example.features.profile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GetApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.FeatureJournaling
import com.example.core.designsystem.FeatureJourney
import com.example.core.designsystem.FeatureLifestyle
import com.example.core.designsystem.MindRestTheme
import com.example.core.designsystem.components.*

@Composable
fun ProfileScreen(
    onNavigateToSettings: () -> Unit = {},
    onNavigateToStatistics: () -> Unit = {},
    onNavigateToAchievements: () -> Unit = {},
    onSignOut: () -> Unit = {},
    onBackClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("profile_screen")
    ) {
        // 1. Non-scrolling TopBar with Settings Action Slot
        TopBar(
            title = "Profile",
            onBackClick = onBackClick,
            actionSlot = {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
                        .clickable(onClick = onNavigateToSettings)
                        .padding(8.dp)
                        .testTag("topbar_settings_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            },
            testTag = "profile_top_bar"
        )

        // 2. Scrollable Content Area (No padding at root so ProfileHeader gradient spans edge-to-edge)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .testTag("profile_scroll_content")
        ) {
            // Hero Profile Header
            ProfileHeader(
                name = "Aria Kusuma",
                email = "aria@email.com",
                emoji = "🧘",
                premium = true,
                testTag = "profile_header"
            )

            // Content Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 24.dp)
                    .testTag("profile_content_section"),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 3. StatsGrid (3 columns)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_stats_grid"),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatTile(
                        value = "14",
                        label = "Sleep Streak days",
                        icon = Icons.Default.LocalFireDepartment,
                        modifier = Modifier.weight(1f),
                        testTag = "stat_tile_sleep_streak"
                    )
                    StatTile(
                        value = "Lv.3",
                        label = "Purpose Level ",
                        icon = Icons.Default.Star,
                        modifier = Modifier.weight(1f),
                        testTag = "stat_tile_purpose_level"
                    )
                    StatTile(
                        value = "42",
                        label = "Journal Entries total",
                        icon = Icons.Default.MenuBook,
                        modifier = Modifier.weight(1f),
                        testTag = "stat_tile_journal_entries"
                    )
                }

                // 4. AchievementsCard
                BaseCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNavigateToAchievements)
                        .testTag("achievements_card"),
                    radius = 16.dp,
                    padding = 16.dp
                ) {
                    SectionLabel(
                        text = "Achievements",
                        testTag = "achievements_section_label"
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("achievements_grid"),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AchievementBadge(
                            emoji = "🌙",
                            label = "7-Day Streak",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f),
                            testTag = "achievement_streak"
                        )
                        AchievementBadge(
                            emoji = "🧠",
                            label = "CBT Master",
                            color = FeatureJournaling,
                            modifier = Modifier.weight(1f),
                            testTag = "achievement_cbt"
                        )
                        AchievementBadge(
                            emoji = "💤",
                            label = "Deep Sleeper",
                            color = FeatureJourney,
                            modifier = Modifier.weight(1f),
                            testTag = "achievement_sleep"
                        )
                        AchievementBadge(
                            emoji = "✨",
                            label = "Ikigai Seeker",
                            color = FeatureLifestyle,
                            modifier = Modifier.weight(1f),
                            testTag = "achievement_ikigai"
                        )
                    }
                }

                // 5. Settings Rows (5 items)
                SettingsRow(
                    label = "App Settings",
                    icon = Icons.Default.Settings,
                    onClick = onNavigateToSettings,
                    testTag = "settings_row_app_settings"
                )
                SettingsRow(
                    label = "Language",
                    icon = Icons.Default.Language,
                    onClick = {},
                    testTag = "settings_row_language"
                )
                SettingsRow(
                    label = "Privacy",
                    icon = Icons.Default.Security,
                    onClick = {},
                    testTag = "settings_row_privacy"
                )
                SettingsRow(
                    label = "Export Data",
                    icon = Icons.Default.GetApp,
                    onClick = {},
                    testTag = "settings_row_export"
                )
                SettingsRow(
                    label = "About MindRest",
                    icon = Icons.Default.Info,
                    onClick = {},
                    testTag = "settings_row_about"
                )

                // 6. SignOutButton
                SignOutRow(
                    onClick = onSignOut,
                    testTag = "sign_out_button"
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenLightPreview() {
    MindRestTheme(darkTheme = false) {
        ProfileScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenDarkPreview() {
    MindRestTheme(darkTheme = true) {
        ProfileScreen()
    }
}

