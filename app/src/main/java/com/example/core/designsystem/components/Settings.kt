package com.example.core.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.MindRestTheme

/**
 * SettingsSection wraps settings lists inside a structured layout container.
 */
@Composable
fun SettingsSection(
    title: String,
    modifier: Modifier = Modifier,
    testTag: String = "settings_section",
    content: @Composable ColumnScope.() -> Unit
) {
    BaseCard(
        modifier = modifier.testTag(testTag),
        radius = 20.dp,
        padding = 16.dp
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            content = content
        )
    }
}

/**
 * AccountLinkRow represents linked account/synchronization credentials.
 */
@Composable
fun AccountLinkRow(
    label: String,
    accountName: String,
    onLinkClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLinked: Boolean = false,
    testTag: String = "account_link_row"
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp))
            .padding(16.dp)
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (isLinked) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = accountName,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Text(
                text = if (isLinked) "Linked" else "Connect",
                color = if (isLinked) com.example.core.designsystem.SuccessColor else MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { onLinkClick() }
                    .padding(horizontal = 4.dp)
                    .testTag("${testTag}_action")
            )
        }
    }
}

/**
 * AppVersionFooter places muted copyright text at settings footers.
 */
@Composable
fun AppVersionFooter(
    appName: String = "MindRest AI",
    version: String = "1.0.0 (Build 2407)",
    modifier: Modifier = Modifier,
    testTag: String = "app_version_footer"
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp)
            .testTag(testTag),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = appName,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = "Version $version",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
            fontSize = 11.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsPreview() {
    MindRestTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SettingsSection(title = "Cloud Synchronization") {
                AccountLinkRow(
                    label = "Google Account",
                    accountName = "user@gmail.com",
                    isLinked = true,
                    onLinkClick = {}
                )
            }
            AppVersionFooter()
        }
    }
}
