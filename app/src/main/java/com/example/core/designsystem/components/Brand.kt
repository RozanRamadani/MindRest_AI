package com.example.core.designsystem.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.core.designsystem.MindRestTheme
import com.example.core.designsystem.LocalSpacing

/**
 * MoonLogo represents the brand identity mark for MindRest AI.
 * It is a fully programmatic vector drawing consisting of a crescent moon and accent elements
 * rendered on a circular background.
 *
 * @param size The physical size of the logo.
 * @param modifier Modifier for external layouts.
 * @param backgroundColor Optional custom background color override (defaults to primary_light).
 */
@Composable
fun MoonLogo(
    size: Dp = 40.dp,
    modifier: Modifier = Modifier,
    backgroundColor: Color? = null
) {
    val actualBgColor = backgroundColor ?: com.example.core.designsystem.LightSecondary // Or resolved based on theme

    // To ensure exact token-referenced colors, let's fetch them from MaterialTheme or the design tokens
    val moonColor = com.example.core.designsystem.LightPrimary
    val accentColor = com.example.core.designsystem.LightAccent

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(actualBgColor)
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val scale = size.toPx() / 24f

            // Drawing the crescent moon path
            // Equivalent path data: M14,6 C10.686,6 8,8.686 8,12 C8,15.314 10.686,18 14,18 C15.04,18 16.02,17.73 16.87,17.26 C15.64,16.5 14.83,15.15 14.83,13.6 C14.83,11.2 16.77,9.26 19.17,9.26 C19.24,9.26 19.31,9.26 19.38,9.27 C18.32,7.32 16.31,6 14,6Z
            withTransform({
                scale(scale, scale, Offset.Zero)
            }) {
                val moonPath = Path().apply {
                    moveTo(14f, 6f)
                    cubicTo(10.686f, 6f, 8f, 8.686f, 8f, 12f)
                    cubicTo(8f, 15.314f, 10.686f, 18f, 14f, 18f)
                    cubicTo(15.04f, 18f, 16.02f, 17.73f, 16.87f, 17.26f)
                    cubicTo(15.64f, 16.5f, 14.83f, 15.15f, 14.83f, 13.6f)
                    cubicTo(14.83f, 11.2f, 16.77f, 9.26f, 19.17f, 9.26f)
                    cubicTo(19.24f, 9.26f, 19.31f, 9.26f, 19.38f, 9.27f)
                    cubicTo(18.32f, 7.32f, 16.31f, 6f, 14f, 6f)
                    close()
                }
                drawPath(moonPath, color = moonColor)

                // Large accent dot: M17,7.5 C17,8.328 16.328,9 15.5,9 C14.672,9 14,8.328 14,7.5 C14,6.672 14.672,6 15.5,6 C16.328,6 17,6.672 17,7.5Z
                drawCircle(
                    color = accentColor,
                    radius = 1.5f,
                    center = Offset(15.5f, 7.5f)
                )

                // Small accent dot: M20.5,12 C20.5,12.552 20.052,13 19.5,13 C18.948,13 18.5,12.552 18.5,12 C18.5,11.448 18.948,11 19.5,11 C20.052,11 20.5,11.448 20.5,12Z
                drawCircle(
                    color = moonColor.copy(alpha = 0.5f),
                    radius = 1f,
                    center = Offset(19.5f, 12f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MoonLogoPreview() {
    MindRestTheme {
        MoonLogo()
    }
}
