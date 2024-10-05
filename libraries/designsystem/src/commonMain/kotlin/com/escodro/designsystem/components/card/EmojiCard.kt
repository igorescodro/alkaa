package com.escodro.designsystem.components.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.escodro.designsystem.components.background.EmojiBackground

@Composable
fun EmojiCard(
    title: String,
    emojis: List<String>,
    color: Color,
    modifier: Modifier = Modifier,
) {
    var rootSize by remember { mutableStateOf(IntSize.Zero) }
    val textSize = rememberTextSize(rootSize = rootSize)
    val cardTitle = if (emojis.isEmpty()) title else "${emojis.firstOrNull()} $title"

    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        modifier = modifier.onGloballyPositioned { coordinates -> rootSize = coordinates.size },
    ) {
        Column {
            EmojiBackground(
                emojis = emojis,
                color = color,
                modifier = Modifier
                    .weight(3f)
                    .padding(1.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 32f,
                            topEnd = 32f,
                            bottomEnd = 0f,
                            bottomStart = 0f,
                        ),
                    ),
            )
            Text(
                text = cardTitle,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = textSize.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 4.dp)
                    .wrapContentSize(align = Alignment.CenterStart),
            )
        }
    }
}

@Composable
private fun rememberTextSize(rootSize: IntSize): Float =
    remember(rootSize) {
        (rootSize.width.coerceAtMost(rootSize.height) / 30).coerceAtLeast(12).toFloat()
    }
