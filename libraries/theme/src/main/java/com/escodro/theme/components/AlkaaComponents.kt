package com.escodro.theme.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.escodro.theme.R

/**
 * Default content containing a icon and a text showing some full screen information.
 * Component usually used for error, info or empty list screens.
 *
 * @param icon icon to be shown
 * @param iconContentDescription the icon content description
 * @param header the text header to be shown
 */
@Composable
fun DefaultIconTextContent(
    icon: ImageVector,
    @StringRes iconContentDescription: Int,
    @StringRes header: Int
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(id = iconContentDescription),
            modifier = Modifier.size(128.dp)
        )
        Text(
            text = stringResource(id = header),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * Basic loading screen to be used when the screen is loading, making the transition smoother.
 */
@Composable
fun AlkaaLoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), content = {})
}

/**
 * TopAppBar for screens that need a back button.
 *
 * @param onUpPressed function to be called when the back/up button is clicked
 */
@Composable
fun AlkaaToolbar(onUpPressed: () -> Unit) {
    TopAppBar(backgroundColor = Color.Transparent, elevation = 0.dp) {
        IconButton(onClick = onUpPressed, modifier = Modifier.align(Alignment.CenterVertically)) {
            Icon(
                imageVector = Icons.Rounded.ArrowBack,
                contentDescription = stringResource(id = R.string.back_arrow_content_description)
            )
        }
    }
}
