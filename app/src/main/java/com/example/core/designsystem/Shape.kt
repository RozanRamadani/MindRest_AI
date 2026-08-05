package com.example.core.designsystem

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

@Immutable
data class Shapes(
    val none: CornerBasedShape = RoundedCornerShape(0.dp),
    val xs: CornerBasedShape = RoundedCornerShape(6.dp),
    val sm: CornerBasedShape = RoundedCornerShape(10.dp),
    val md: CornerBasedShape = RoundedCornerShape(16.dp),
    val lg: CornerBasedShape = RoundedCornerShape(20.dp),
    val xl: CornerBasedShape = RoundedCornerShape(24.dp),
    val xxl: CornerBasedShape = RoundedCornerShape(32.dp),
    val full: CornerBasedShape = RoundedCornerShape(50)
)

val LocalShapes = staticCompositionLocalOf { Shapes() }
