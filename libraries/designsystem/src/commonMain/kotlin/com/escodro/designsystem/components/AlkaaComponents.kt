package com.escodro.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * Default content containing a icon and a text showing some full screen information.
 * Component usually used for error, info or empty list screens.
 *
 * @param icon icon to be shown
 * @param iconContentDescription the icon content description
 * @param header the text header to be shown
 * @param modifier modifier to be set
 */
@Composable
fun DefaultIconTextContent(
    icon: ImageVector,
    iconContentDescription: String,
    header: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = iconContentDescription,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = header,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}

/**
 * Basic loading screen to be used when the screen is loading, making the transition smoother.
 */
@Composable
fun AlkaaLoadingContent(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), content = {})
}

/**
 * TopAppBar for screens that need a back button.
 *
 * @param onUpPress function to be called when the back/up button is clicked
 * @param modifier Compose modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlkaaToolbar(onUpPress: () -> Unit, modifier: Modifier = Modifier) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onUpPress) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back", // TODO use localized string
                )
            }
        },
        modifier = modifier,
    )
}

/**
 * Floating Action button do add new elements.
 *
 * @param contentDescription string resource to describe the add button
 * @param modifier Compose modifier
 * @param onClick function to be called on the click
 */
@Composable
fun AddFloatingButton(
    contentDescription: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    FloatingActionButton(
        containerColor = MaterialTheme.colorScheme.primary,
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = contentDescription,
        )
    }
}

/**
 * TextField input for Alkaa forms.
 *
 * @param label text field label
 * @param text text to be shown
 * @param onTextChange function to update text
 * @param modifier text field modifier
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AlkaaInputTextField(
    label: String,
    text: String,
    onTextChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        label = { Text(text = label) },
        value = text,
        onValueChange = onTextChange,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        modifier = modifier,
    )
}
