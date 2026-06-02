package com.example.ui.theme

import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Custom effects defining the Playful & Cartoony design code
 */

// Thick chunky cartoon border shape (24dp corners by default)
val CartoonCardShape = RoundedCornerShape(24.dp)
val CartoonButtonShape = RoundedCornerShape(20.dp)
val CartoonInputShape = RoundedCornerShape(16.dp)

/**
 * Creates a beautiful flat 3D offset shadow beneath components, mimicking
 * the premium goofy cartoony visual language with pixel perfection.
 */
fun Modifier.cartoonShadow(
    shadowColor: Color = CuteCocoaCharcoal,
    offsetX: Dp = 6.dp,
    offsetY: Dp = 6.dp,
    shapeRadius: Dp = 24.dp
): Modifier = this.drawBehind {
    // Draw the offset shadow block first
    drawRoundRect(
        color = shadowColor,
        topLeft = Offset(offsetX.toPx(), offsetY.toPx()),
        size = size,
        cornerRadius = CornerRadius(shapeRadius.toPx(), shapeRadius.toPx())
    )
}

/**
 * A highly polished, custom spring-bouncing click modifier.
 * Simulates gooey bouncing animations perfectly when clicked!
 */
fun Modifier.bouncyClickable(
    interactionSource: MutableInteractionSource? = null,
    enabled: Boolean = true,
    onClick: () -> Unit
): Modifier = composed {
    val actualInteractionSource = interactionSource ?: remember { MutableInteractionSource() }
    val isPressed by actualInteractionSource.collectIsPressedAsState()
    
    // Animate scale under press with a medium-bouncy spring
    val scaleAnim by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1.0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "scale"
    )

    // Translate/offset downwards upon tap to simulate organic rubber pushing down
    val offsetAnim by animateDpAsState(
        targetValue = if (isPressed) 3.dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "offset"
    )

    this
        .scale(scaleAnim)
        .offset(y = offsetAnim)
        .clip(CartoonButtonShape)
        .clickable(
            interactionSource = actualInteractionSource,
            indication = null, // Disable default gray box ripple to let our bouncy scale outline shine
            enabled = enabled,
            onClick = onClick
        )
}
