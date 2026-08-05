package com.example.core.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.MindRestTheme

/**
 * PrimaryButton is the main call-to-action button for MindRest AI.
 * It uses a diagonal gradient, specific corner rounding, and supports loading states.
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loading: Boolean = false,
    small: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
    testTag: String = "primary_button"
) {
    val shape = RoundedCornerShape(14.dp)
    val height = if (small) 40.dp else 48.dp
    val fontSize = if (small) 14.sp else 16.sp
    
    val primaryColor = MaterialTheme.colorScheme.primary
    // Diagonal linear gradient (primary -> lighter primary variant)
    val brush = if (enabled && !loading) {
        Brush.linearGradient(
            colors = listOf(primaryColor, primaryColor.copy(alpha = 0.8f))
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Gray.copy(alpha = 0.4f), Color.Gray.copy(alpha = 0.4f))
        )
    }

    Box(
        modifier = modifier
            .height(height)
            .clip(shape)
            .background(brush)
            .clickable(
                enabled = enabled && !loading,
                onClick = onClick,
                role = Role.Button
            )
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (loading) {
                CircularProgressIndicator(
                    color = Color.White,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                if (leadingIcon != null) {
                    leadingIcon()
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(
                    text = text,
                    color = if (enabled) Color.White else Color.White.copy(alpha = 0.6f),
                    fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = fontSize,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * SecondaryButton is an alternative action button with primary colors at reduced opacity.
 */
@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    testTag: String = "secondary_button"
) {
    val shape = RoundedCornerShape(14.dp)
    val primaryColor = MaterialTheme.colorScheme.primary
    val containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
    val strokeColor = primaryColor.copy(alpha = 0.3f)

    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp)
            .testTag(testTag),
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = primaryColor,
            disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
            disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, strokeColor)
    ) {
        Text(
            text = text,
            fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        )
    }
}

/**
 * SendButton is a styled circular button displaying different colors
 * based on whether the input text is entered (active state) or empty.
 */
@Composable
fun SendButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    active: Boolean = false,
    testTag: String = "send_button"
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val backgroundColor = if (active) primaryColor else MaterialTheme.colorScheme.surfaceVariant
    val iconColor = if (active) Color.White else MaterialTheme.colorScheme.onSurfaceVariant

    Box(
        modifier = modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(enabled = active, onClick = onClick, role = Role.Button)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.Send,
            contentDescription = "Send",
            tint = iconColor,
            modifier = Modifier.size(20.dp)
        )
    }
}

/**
 * MoodButton is a circular emoji button with scaling animations and selected indicators.
 */
@Composable
fun MoodButton(
    emoji: String,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp,
    emojiSize: Int = 20,
    testTag: String = "mood_button"
) {
    val scale by animateFloatAsState(targetValue = if (selected) 1.1f else 1.0f, label = "ScaleAnimation")
    val spacing = size * 0.1f
    
    val shape = CircleShape
    val primaryColor = MaterialTheme.colorScheme.primary
    val backgroundColor = MaterialTheme.colorScheme.surfaceVariant

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .testTag("${testTag}_$label")
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .scale(scale)
                .clip(shape)
                .background(backgroundColor)
                .border(
                    width = if (selected) 2.dp else 0.dp,
                    color = if (selected) primaryColor else Color.Transparent,
                    shape = shape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = emojiSize.sp,
                textAlign = TextAlign.Center
            )
        }
        if (selected) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = primaryColor,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

/**
 * SegmentedControl provides a multi-option tab bar / filter toggle.
 */
@Composable
fun <T> SegmentedControl(
    items: List<T>,
    selectedItem: T,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String,
    modifier: Modifier = Modifier,
    testTag: String = "segmented_control"
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
            .padding(3.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items.forEach { item ->
            val isSelected = item == selectedItem
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(28.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    )
                    .clickable { onItemSelected(item) }
                    .testTag("${testTag}_${itemLabel(item).lowercase()}"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = itemLabel(item),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonsPreview() {
    MindRestTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PrimaryButton(text = "Primary Button", onClick = {})
            PrimaryButton(text = "Primary Loading", onClick = {}, loading = true)
            SecondaryButton(text = "Secondary Button", onClick = {})
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SendButton(onClick = {}, active = false)
                SendButton(onClick = {}, active = true)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                MoodButton(emoji = "😐", label = "Neutral", selected = false, onClick = {})
                MoodButton(emoji = "😄", label = "Great", selected = true, onClick = {})
            }
        }
    }
}
