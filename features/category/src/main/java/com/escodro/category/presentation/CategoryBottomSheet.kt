package com.escodro.category.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.escodro.category.R
import com.escodro.categoryapi.model.Category
import com.escodro.theme.AlkaaTheme

/**
 * Alkaa Category Bottom Sheet.
 */
@Composable
fun CategoryBottomSheet(category: Category) {
    val colorList = CategoryColors.values().map { it.value }
    CategorySheetContent(
        colorList = colorList,
        category = category,
        onCategoryChange = { _, _ -> }
    )
}

@Composable
private fun CategorySheetContent(
    colorList: List<Color>,
    category: Category,
    onCategoryChange: (name: String, color: Color) -> Unit
) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.SpaceAround) {
        var name by rememberSaveable(category) { mutableStateOf(category.name) }
        CategoryNameField(name = name, onNameChange = { name = it })

        var color by remember(category) { mutableStateOf(Color(category.color)) }
        CategoryColorSelector(colorList = colorList, value = color, onValueChange = { color = it })

        CategorySaveButton(currentColor = color, onClick = { onCategoryChange(name, color) })
    }
}

@Composable
private fun CategoryNameField(name: String, onNameChange: (String) -> Unit) {
    OutlinedTextField(
        label = { Text(text = stringResource(id = R.string.category_sheet_title)) },
        value = name,
        onValueChange = onNameChange,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun CategorySaveButton(currentColor: Color, onClick: () -> Unit) {
    val colorState = animateColorAsState(targetValue = currentColor)
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = colorState.value),
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(
            text = stringResource(id = R.string.category_sheet_save),
            color = MaterialTheme.colors.background
        )
    }
}

@Composable
private fun CategoryColorSelector(
    colorList: List<Color>,
    value: Color,
    onValueChange: (Color) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        items(
            items = colorList,
            itemContent = { color ->
                val optionSelected = color == value
                CategoryColorItem(color, optionSelected, onClick = { onValueChange(color) })
            }
        )
    }
}

@Composable
private fun CategoryColorItem(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(color = color)
                .selectable(
                    role = Role.RadioButton,
                    selected = isSelected,
                    onClick = onClick
                )
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colors.background)
            )
        }
    }
}

@Suppress("UndocumentedPublicFunction")
@Preview
@Composable
fun CategorySheetContentPreview() {
    AlkaaTheme {
        Surface(modifier = Modifier.height(256.dp)) {
            val category = Category(name = "Movies to watch", color = android.graphics.Color.YELLOW)
            CategorySheetContent(
                colorList = CategoryColors.values().map { it.value },
                category = category,
                onCategoryChange = { _, _ -> }
            )
        }
    }
}
