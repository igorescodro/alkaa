package com.escodro.alkaa.presentation.home

import androidx.compose.animation.animate
import androidx.compose.foundation.Icon
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.Stack
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.BottomAppBar
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.savedinstancestate.savedInstanceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.ui.tooling.preview.Preview
import com.escodro.alkaa.model.HomeSection
import com.escodro.theme.AlkaaTheme

@Composable
fun Home() {
    val (currentSection, setCurrentSection) = savedInstanceState { HomeSection.Tasks }
    val navItems = HomeSection.values().toList()
    Scaffold(
        topBar = {
            AlkaaTopBar(currentSection = currentSection)
        },
        bottomBar = {
            AlkaaBottomNav(
                currentSection = currentSection,
                onSectionSelected = setCurrentSection,
                items = navItems
            )
        }) {}
}

@Composable
private fun AlkaaTopBar(currentSection: HomeSection) {
    TopAppBar(backgroundColor = MaterialTheme.colors.background, elevation = 0.dp) {
        Stack(modifier = Modifier.fillMaxSize()) {
            Text(
                modifier = Modifier.gravity(Alignment.Center),
                style = MaterialTheme.typography.h5,
                text = stringResource(currentSection.title)
            )
        }
    }
}

@Composable
private fun AlkaaBottomNav(
    currentSection: HomeSection,
    onSectionSelected: (HomeSection) -> Unit,
    items: List<HomeSection>
) {
    BottomAppBar(backgroundColor = MaterialTheme.colors.background) {
        items.forEach { section ->
            val selected = section == currentSection
            val tint = animate(
                if (selected) {
                    MaterialTheme.colors.primary
                } else {
                    MaterialTheme.colors.onSecondary
                }
            )

            IconButton(onClick = { onSectionSelected(section) }, modifier = Modifier.weight(1f)) {
                Icon(section.icon, tint = tint)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AlkaaTopBarPreview() {
    AlkaaTheme {
        AlkaaTopBar(HomeSection.Tasks)
    }
}

@Preview(showBackground = true)
@Composable
fun AlkaaBottomNavPreview() {
    AlkaaTheme {
        AlkaaBottomNav(
            currentSection = HomeSection.Tasks,
            onSectionSelected = {},
            items = HomeSection.values().toList()
        )
    }
}
