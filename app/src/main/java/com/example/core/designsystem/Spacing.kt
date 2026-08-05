package com.example.core.designsystem

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Spacing(
    val space0: Dp = 0.dp,
    val space1: Dp = 4.dp,
    val space2: Dp = 8.dp,
    val space3: Dp = 12.dp,
    val space4: Dp = 16.dp,
    val space5: Dp = 20.dp,
    val space6: Dp = 24.dp,
    val space8: Dp = 32.dp,
    val space10: Dp = 40.dp,
    val space12: Dp = 48.dp,
    val space16: Dp = 64.dp,
    val space20: Dp = 80.dp,
    
    val screenHorizontal: Dp = 20.dp,
    val screenTop: Dp = 16.dp,
    val screenBottom: Dp = 24.dp,
    val cardPadding: Dp = 20.dp,
    val sectionGap: Dp = 32.dp,
    val componentGap: Dp = 12.dp
)

val LocalSpacing = staticCompositionLocalOf { Spacing() }
