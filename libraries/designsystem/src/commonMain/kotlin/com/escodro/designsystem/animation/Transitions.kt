package com.escodro.designsystem.animation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

/**
 * Transition used to slide in horizontally the content. The content will slide from the left to the
 * right and fade in.
 */
val SlideInHorizontallyTransition: EnterTransition = slideInHorizontally(
    initialOffsetX = { it },
    animationSpec = tween(
        durationMillis = 300,
        easing = LinearEasing,
    ),
) +
    fadeIn()

/**
 * Transition used to slide out horizontally the content. The content will slide from the right to
 * the left and fade out.
 */
val SlideOutHorizontallyTransition: ExitTransition = fadeOut() +
    slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(
            durationMillis = 300,
            easing = LinearEasing,
        ),
    )

/**
 * Transition used to slide in vertically the content. The content will slide from the top to the
 * bottom and fade in.
 */
val TopBarEnterTransition: EnterTransition = fadeIn(
    animationSpec = tween(
        delayMillis = 300,
        durationMillis = 600,
    ),
) +
    slideInVertically(
        animationSpec = tween(
            delayMillis = 300,
            durationMillis = 600,
        ),
    )

/**
 * Transition used to slide out vertically the content. The content will slide from the bottom to the
 * top and fade out.
 */
val TopBarExitTransition: ExitTransition = slideOutVertically(
    animationSpec = tween(
        delayMillis = 0,
        durationMillis = 600,
    ),
) +
    fadeOut(
        animationSpec = tween(
            delayMillis = 0,
            durationMillis = 600,
        ),
    )
