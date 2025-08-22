package com.escodro.designsystem.components.background

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun EmojiBackground(
    emojis: List<String>,
    color: Color,
    modifier: Modifier = Modifier,
) {
    var rootSize by remember { mutableStateOf(IntSize.Zero) }
    val iconSize = calculateIconSize(rootSize)
    val shuffledEmojis = rememberEmojiList(emojis)

    Box(
        modifier = modifier
            .background(color = color)
            .onGloballyPositioned { coordinates ->
                rootSize = coordinates.size
            }.fillMaxSize(),
    ) {
        LazyHorizontalStaggeredGrid(
            rows = StaggeredGridCells.Adaptive(iconSize.dp),
        ) {
            items(shuffledEmojis) { emoji ->
                EmojiText(
                    emoji = emoji,
                    filterColor = color,
                    emojiSize = iconSize,
                )
            }
        }
    }
}

@Composable
private fun EmojiText(
    emoji: String,
    filterColor: Color,
    emojiSize: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = emoji,
        style = MaterialTheme.typography.bodyLarge.copy(fontSize = (emojiSize * 0.8).sp),
        modifier = modifier
            .padding(
                start = getRandomPadding(factor = emojiSize),
                end = getRandomPadding(factor = emojiSize),
            ).scale(1f)
            .rotate(getRandomRotation())
            .graphicsLayer(alpha = 0.9f)
            .grayScale(filterColor),
    )
}

@Composable
private fun rememberEmojiList(list: List<String>): List<String> = remember(list) {
    val defaultEmojis = listOf("❓", "🧐", "❗️", "🙈", "🤷", "⚠️")
    val emojis = list.ifEmpty { defaultEmojis }
    emojis.flatMap { emoji -> List(16) { emoji } }.shuffled()
}

private fun calculateIconSize(size: IntSize): Int =
    (size.width.coerceAtMost(size.height) / 16).coerceAtLeast(32)

private fun getRandomPadding(factor: Int): Dp =
    Random.nextInt(factor / 16, factor / 8).dp

private fun getRandomRotation(): Float =
    Random.nextInt(-25, 25).toFloat()

private fun darkenColor(color: Color, factor: Float): Color {
    val darkFactor = 1f - factor.coerceIn(0f, 1f)
    return Color(
        red = color.red * darkFactor,
        green = color.green * darkFactor,
        blue = color.blue * darkFactor,
        alpha = color.alpha,
    )
}

private fun Modifier.grayScale(filterColor: Color): Modifier {
    val saturationMatrix = ColorMatrix().apply { setToSaturation(0f) }
    val saturationFilter = ColorFilter.colorMatrix(colorMatrix = saturationMatrix)
    val paint = Paint().apply { this.colorFilter = saturationFilter }

    return drawWithCache {
        val canvasBounds = Rect(Offset.Zero, size)
        onDrawWithContent {
            drawIntoCanvas {
                it.saveLayer(bounds = canvasBounds, paint = paint)
                drawContent()
                it.restore()
            }
            drawRect(
                color = darkenColor(filterColor, 0.4f).copy(alpha = 0.9f),
                blendMode = BlendMode.SrcAtop,
            )
        }
    }
}
