package com.escodro.tracker.presentation.components

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.escodro.tracker.model.Tracker

@Composable
@Suppress("MagicNumber")
internal fun TaskGraph(list: List<Tracker.CategoryInfo>, modifier: Modifier = Modifier) {
    val currentState = remember {
        MutableTransitionState(AnimatedCircleProgress.START)
            .apply { targetState = AnimatedCircleProgress.END }
    }

    val defaultColor = MaterialTheme.colors.onSecondary
    val stroke = with(LocalDensity.current) { Stroke(32.dp.toPx()) }
    val transition = updateTransition(currentState, label = LabelUpdateTransition)
    val angleOffset by angleOffset(transition)
    val shift by shift(transition)
    val dividerLength = if (list.count() == 1) 0F else DividerLengthInDegrees

    Canvas(modifier = modifier) {
        val innerRadius = (size.minDimension - stroke.width) / 2
        val halfSize = size / 2.0f
        val topLeft = Offset(x = halfSize.width - innerRadius, y = halfSize.height - innerRadius)
        val size = Size(width = innerRadius * 2, height = innerRadius * 2)
        var startAngle = shift - 90f

        list.forEach { categoryInfo ->
            val sweep = categoryInfo.percentage * angleOffset
            drawArc(
                color = categoryInfo.color?.let { Color(it) } ?: defaultColor,
                startAngle = startAngle + dividerLength / 2,
                sweepAngle = sweep - dividerLength,
                topLeft = topLeft,
                size = size,
                useCenter = false,
                style = stroke
            )
            startAngle += sweep
        }
    }
}

@Composable
@Suppress("MagicNumber")
private fun shift(transition: Transition<AnimatedCircleProgress>) =
    transition.animateFloat(
        transitionSpec = {
            tween(
                delayMillis = 500,
                durationMillis = 900,
                easing = CubicBezierEasing(0f, 0.75f, 0.35f, 0.85f)
            )
        },
        label = LabelShiftAnimation
    ) { progress ->
        if (progress == AnimatedCircleProgress.START) {
            0f
        } else {
            30f
        }
    }

@Composable
@Suppress("MagicNumber")
private fun angleOffset(transition: Transition<AnimatedCircleProgress>) =
    transition.animateFloat(
        transitionSpec = {
            tween(
                delayMillis = 500,
                durationMillis = 900,
                easing = LinearOutSlowInEasing
            )
        },
        label = LabelAngleOffsetAnimation
    ) { progress ->
        if (progress == AnimatedCircleProgress.START) {
            0f
        } else {
            360f
        }
    }

private const val DividerLengthInDegrees = 1.8f

internal const val LabelAngleOffsetAnimation = "AngleOffsetAnimation"

internal const val LabelShiftAnimation = "ShiftAnimation"

internal const val LabelUpdateTransition = "UpdateTransition"

private enum class AnimatedCircleProgress { START, END }
