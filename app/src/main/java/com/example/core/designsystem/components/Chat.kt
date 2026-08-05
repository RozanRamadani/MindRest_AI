package com.example.core.designsystem.components

import android.content.Intent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BorderColor
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.designsystem.MindRestTheme
import kotlinx.coroutines.launch

/**
 * AIAvatar represents the mini logo display for conversational prompts.
 */
@Composable
fun AIAvatar(
    modifier: Modifier = Modifier,
    testTag: String = "ai_avatar"
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f))
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        MoonLogo(size = 24.dp)
    }
}

/**
 * ChatBubble encapsulates either an AI (Left-aligned) or User (Right-aligned) conversation bubble.
 */
@Composable
fun ChatBubble(
    text: String,
    isUser: Boolean,
    modifier: Modifier = Modifier,
    timestamp: String? = null,
    testTag: String = "chat_bubble",
    rawTextToCopy: String? = null,
    onCopyClick: (() -> Unit)? = null,
    onFeedbackClick: ((isPositive: Boolean) -> Unit)? = null,
    onRegenerateClick: (() -> Unit)? = null,
    onShareClick: (() -> Unit)? = null,
    onTtsClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null,
    onReportClick: (() -> Unit)? = null,
    onHighlightClick: ((String) -> Unit)? = null,
    learnMoreResources: List<String>? = null,
    contentSlot: @Composable (() -> Unit)? = null
) {
    val isDark = MaterialTheme.colorScheme.background.luminance() < 0.5f
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val density = LocalDensity.current.density
    val copyTargetText = rawTextToCopy ?: text
    var userFeedback by remember { mutableStateOf<Boolean?>(null) }
    var isSpeaking by remember { mutableStateOf(false) }
    var isHighlighted by remember { mutableStateOf(false) }
    var ttsInstance by remember { mutableStateOf<TextToSpeech?>(null) }
    var showContextMenu by remember { mutableStateOf(false) }
    var isLearnMoreExpanded by remember { mutableStateOf(false) }

    val defaultSleepResources = remember {
        listOf(
            "National Sleep Foundation: Sleep Hygiene Essentials",
            "NIH Guide: Understanding Circadian Rhythms & Melatonin",
            "Harvard Medical School: Sleep Stages & Health Benefits"
        )
    }
    val resourcesToDisplay = learnMoreResources ?: defaultSleepResources

    DisposableEffect(context) {
        val tts = TextToSpeech(context) { _ -> }
        ttsInstance = tts
        onDispose {
            tts.stop()
            tts.shutdown()
        }
    }

    val animatedAlpha = remember { Animatable(0f) }
    val animatedOffsetY = remember { Animatable(16f) }

    LaunchedEffect(Unit) {
        launch {
            animatedAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            )
        }
        launch {
            animatedOffsetY.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing)
            )
        }
    }

    val aiBgColor = if (isDark) {
        MaterialTheme.colorScheme.surface
    } else {
        Color(0xFFF0EDF9) // Light Lavender Tint
    }

    val aiTextColor = if (isDark) {
        MaterialTheme.colorScheme.onSurface
    } else {
        Color(0xFF1A1530)
    }

    val contentColor = if (isUser) Color.White else aiTextColor

    val userBrush = if (isDark) {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFF5850E7),
                Color(0xFF4338CA)
            )
        )
    } else {
        Brush.linearGradient(
            colors = listOf(
                MaterialTheme.colorScheme.primary,
                Color(0xFF4F46E5)
            )
        )
    }

    val shape = if (isUser) {
        // Flat bottom-right corner (16dp all except bottom-right 4dp)
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 16.dp,
            bottomEnd = 4.dp
        )
    } else {
        // Flat bottom-left corner
        RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 4.dp,
            bottomEnd = 16.dp
        )
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .graphicsLayer {
                alpha = animatedAlpha.value
                translationY = animatedOffsetY.value * density
            },
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        if (!isUser) {
            AIAvatar(modifier = Modifier.padding(end = 4.dp))
        }

        Column(
            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Box(
                modifier = Modifier
                    .clip(shape)
                    .then(
                        if (isUser) Modifier.background(userBrush)
                        else Modifier.background(aiBgColor)
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onLongPress = {
                                showContextMenu = true
                            }
                        )
                    }
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .testTag(testTag)
            ) {
                DropdownMenu(
                    expanded = showContextMenu,
                    onDismissRequest = { showContextMenu = false },
                    modifier = Modifier.testTag("chat_bubble_context_menu")
                ) {
                    DropdownMenuItem(
                        text = { Text("Highlight to Journal") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.BorderColor,
                                contentDescription = "Highlight to Journal",
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        },
                        onClick = {
                            showContextMenu = false
                            isHighlighted = !isHighlighted
                            if (isHighlighted) {
                                onHighlightClick?.invoke(copyTargetText)
                                Toast.makeText(context, "Saved to MindRest Journal!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Highlight removed", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.testTag("context_menu_highlight")
                    )
                    DropdownMenuItem(
                        text = { Text("Delete Message") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Message",
                                tint = MaterialTheme.colorScheme.error
                            )
                        },
                        onClick = {
                            showContextMenu = false
                            if (onDeleteClick != null) {
                                onDeleteClick()
                            } else {
                                Toast.makeText(context, "Message deleted", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.testTag("context_menu_delete")
                    )
                    DropdownMenuItem(
                        text = { Text("Report Content") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Flag,
                                contentDescription = "Report Content"
                            )
                        },
                        onClick = {
                            showContextMenu = false
                            if (onReportClick != null) {
                                onReportClick()
                            } else {
                                Toast.makeText(context, "Content reported", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.testTag("context_menu_report")
                    )
                }

                CompositionLocalProvider(LocalContentColor provides contentColor) {
                    Column {
                        if (isHighlighted) {
                            Surface(
                                color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = if (isDark) 0.35f else 0.7f),
                                shape = RoundedCornerShape(6.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 6.dp)
                                    .testTag("highlight_badge")
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bookmark,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Highlighted in MindRest Journal",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                        }

                        if (text.isNotEmpty()) {
                            Text(
                                text = text,
                                color = contentColor,
                                fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                                fontSize = 14.sp,
                                lineHeight = 20.sp
                            )
                        }
                        if (contentSlot != null) {
                            if (text.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            contentSlot()
                        }

                        if (!isUser && resourcesToDisplay.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(
                                color = contentColor.copy(alpha = 0.15f),
                                thickness = 1.dp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .clickable { isLearnMoreExpanded = !isLearnMoreExpanded }
                                    .padding(vertical = 4.dp, horizontal = 2.dp)
                                    .testTag("learn_more_button"),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Book,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Learn More (Sleep Science)",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontSize = 12.sp
                                    )
                                }
                                Icon(
                                    imageVector = if (isLearnMoreExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = if (isLearnMoreExpanded) "Collapse Learn More" else "Expand Learn More",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            AnimatedVisibility(
                                visible = isLearnMoreExpanded,
                                enter = fadeIn() + expandVertically(),
                                exit = fadeOut() + shrinkVertically()
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 4.dp, bottom = 2.dp)
                                        .testTag("learn_more_content")
                                ) {
                                    resourcesToDisplay.forEach { resource ->
                                        Surface(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 2.dp)
                                                .clip(RoundedCornerShape(6.dp))
                                                .clickable {
                                                    Toast.makeText(context, "Opening resource: $resource", Toast.LENGTH_SHORT).show()
                                                },
                                            color = contentColor.copy(alpha = 0.05f),
                                            shape = RoundedCornerShape(6.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "•",
                                                    fontSize = 12.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Text(
                                                    text = resource,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontSize = 11.sp,
                                                    color = contentColor.copy(alpha = 0.9f)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (!isUser) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Thumbs Up feedback button
                                IconButton(
                                    onClick = {
                                        val newFeedback = if (userFeedback == true) null else true
                                        userFeedback = newFeedback
                                        if (newFeedback == true) {
                                            onFeedbackClick?.invoke(true)
                                            Toast.makeText(context, "Thanks for your feedback!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier
                                        .size(24.dp)
                                        .testTag("thumbs_up_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ThumbUp,
                                        contentDescription = "Thumbs Up",
                                        tint = if (userFeedback == true) MaterialTheme.colorScheme.primary else contentColor.copy(alpha = 0.6f),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(4.dp))

                                // Thumbs Down feedback button
                                IconButton(
                                    onClick = {
                                        val newFeedback = if (userFeedback == false) null else false
                                        userFeedback = newFeedback
                                        if (newFeedback == false) {
                                            onFeedbackClick?.invoke(false)
                                            Toast.makeText(context, "Thanks for your feedback!", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier
                                        .size(24.dp)
                                        .testTag("thumbs_down_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ThumbDown,
                                        contentDescription = "Thumbs Down",
                                        tint = if (userFeedback == false) MaterialTheme.colorScheme.error else contentColor.copy(alpha = 0.6f),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }

                                if (copyTargetText.isNotEmpty() || onCopyClick != null) {
                                    Spacer(modifier = Modifier.width(4.dp))

                                    // Copy to clipboard button
                                    IconButton(
                                        onClick = {
                                            if (onCopyClick != null) {
                                                onCopyClick()
                                            } else {
                                                clipboardManager.setText(AnnotatedString(copyTargetText))
                                                Toast.makeText(context, "Copied to clipboard", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        modifier = Modifier
                                            .size(24.dp)
                                            .testTag("copy_to_clipboard_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.ContentCopy,
                                            contentDescription = "Copy to Clipboard",
                                            tint = contentColor.copy(alpha = 0.6f),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.width(4.dp))

                                // Regenerate response button
                                IconButton(
                                    onClick = {
                                        if (onRegenerateClick != null) {
                                            onRegenerateClick()
                                        } else {
                                            Toast.makeText(context, "Regenerating response...", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier
                                        .size(24.dp)
                                        .testTag("regenerate_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Regenerate Response",
                                        tint = contentColor.copy(alpha = 0.6f),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(4.dp))

                                // Share button
                                IconButton(
                                    onClick = {
                                        if (onShareClick != null) {
                                            onShareClick()
                                        } else if (copyTargetText.isNotEmpty()) {
                                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                                type = "text/plain"
                                                putExtra(Intent.EXTRA_TEXT, copyTargetText)
                                            }
                                            context.startActivity(Intent.createChooser(shareIntent, "Share Insight"))
                                        }
                                    },
                                    modifier = Modifier
                                        .size(24.dp)
                                        .testTag("share_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Share,
                                        contentDescription = "Share",
                                        tint = contentColor.copy(alpha = 0.6f),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(4.dp))

                                // Text-to-speech Play button
                                IconButton(
                                    onClick = {
                                        if (onTtsClick != null) {
                                            onTtsClick()
                                        } else {
                                            if (isSpeaking) {
                                                ttsInstance?.stop()
                                                isSpeaking = false
                                                Toast.makeText(context, "Audio stopped", Toast.LENGTH_SHORT).show()
                                            } else if (copyTargetText.isNotEmpty()) {
                                                ttsInstance?.language = java.util.Locale.US
                                                ttsInstance?.speak(copyTargetText, TextToSpeech.QUEUE_FLUSH, null, "TTS_MSG_ID")
                                                isSpeaking = true
                                                Toast.makeText(context, "Playing audio...", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    },
                                    modifier = Modifier
                                        .size(24.dp)
                                        .testTag("tts_play_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                        contentDescription = "Text to Speech Play",
                                        tint = if (isSpeaking) MaterialTheme.colorScheme.primary else contentColor.copy(alpha = 0.6f),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(4.dp))

                                // Highlight Segment button
                                IconButton(
                                    onClick = {
                                        isHighlighted = !isHighlighted
                                        if (isHighlighted) {
                                            onHighlightClick?.invoke(copyTargetText)
                                            Toast.makeText(context, "Saved to MindRest Journal!", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Highlight removed", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    modifier = Modifier
                                        .size(24.dp)
                                        .testTag("highlight_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.BorderColor,
                                        contentDescription = "Highlight Segment",
                                        tint = if (isHighlighted) MaterialTheme.colorScheme.tertiary else contentColor.copy(alpha = 0.6f),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (timestamp != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = timestamp,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isDark) Color(0xFFA0AEC0) else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }
    }
}

/**
 * MoodPickerWidget provides interactive mood buttons inside conversations.
 */
@Composable
fun MoodPickerWidget(
    selectedMoodLabel: String,
    onMoodSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "mood_picker_widget"
) {
    val moods = listOf(
        Pair("😔", "Sad"),
        Pair("😟", "Anxious"),
        Pair("😐", "Neutral"),
        Pair("😊", "Good"),
        Pair("😄", "Great")
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag(testTag)
    ) {
        Text(
            text = "Select your current mood:",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            moods.forEach { (emoji, label) ->
                MoodButton(
                    emoji = emoji,
                    label = label,
                    selected = label == selectedMoodLabel,
                    onClick = { onMoodSelected(label) },
                    size = 48.dp,
                    emojiSize = 22,
                    testTag = "${testTag}_button"
                )
            }
        }
    }
}

/**
 * CBTReflectionWidget houses Situation, Automatic Thought, and Balanced Response inputs.
 */
@Composable
fun CBTReflectionWidget(
    situation: String,
    automaticThought: String,
    balancedResponse: String,
    onSituationChange: (String) -> Unit,
    onAutomaticThoughtChange: (String) -> Unit,
    onBalancedResponseChange: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "cbt_reflection_widget"
) {
    BaseCard(
        modifier = modifier.testTag(testTag),
        radius = 16.dp,
        padding = 12.dp
    ) {
        Text(
            text = "CBT Thought Record",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        CBTField(
            label = "1. Situation / Trigger",
            value = situation,
            onValueChange = onSituationChange,
            placeholder = "What happened?",
            testTag = "${testTag}_situation"
        )

        Spacer(modifier = Modifier.height(8.dp))

        CBTField(
            label = "2. Automatic Negative Thought",
            value = automaticThought,
            onValueChange = onAutomaticThoughtChange,
            placeholder = "What are you telling yourself?",
            testTag = "${testTag}_automatic_thought"
        )

        Spacer(modifier = Modifier.height(8.dp))

        CBTField(
            label = "3. Balanced Alternative Perspective",
            value = balancedResponse,
            onValueChange = onBalancedResponseChange,
            placeholder = "What is a more realistic view?",
            testTag = "${testTag}_balanced_response"
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            text = "Submit Thought Record",
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun CBTField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    testTag: String
) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, fontSize = 13.sp) },
            textStyle = TextStyle(fontSize = 14.sp),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .testTag(testTag)
        )
    }
}

/**
 * DailyGratitudeWidget allows logging daily gratitude milestones.
 */
@Composable
fun DailyGratitudeWidget(
    items: List<String>, // Exactly 3 items expected
    onItemChange: (Int, String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = "daily_gratitude_widget"
) {
    BaseCard(
        modifier = modifier.testTag(testTag),
        radius = 16.dp,
        padding = 12.dp
    ) {
        Text(
            text = "Daily Gratitude List",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = com.example.core.designsystem.FeatureRelaxation
        )

        Spacer(modifier = Modifier.height(12.dp))

        for (i in 0 until 3) {
            val itemValue = items.getOrElse(i) { "" }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${i + 1}.",
                    style = MaterialTheme.typography.titleSmall,
                    color = com.example.core.designsystem.FeatureRelaxation,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(20.dp)
                )
                OutlinedTextField(
                    value = itemValue,
                    onValueChange = { onItemChange(i, it) },
                    placeholder = { Text("I am grateful for...", fontSize = 13.sp) },
                    textStyle = TextStyle(fontSize = 14.sp),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp)
                        .testTag("${testTag}_field_$i")
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            text = "Share Gratitude",
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatPreview() {
    MindRestTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ChatBubble(text = "Hello! I am your companion.", isUser = false, timestamp = "10:12 AM")
            ChatBubble(text = "I would like to do reflection.", isUser = true, timestamp = "10:13 AM")
            ChatBubble(
                text = "",
                isUser = false,
                contentSlot = {
                    MoodPickerWidget(selectedMoodLabel = "Neutral", onMoodSelected = {})
                }
            )
        }
    }
}
