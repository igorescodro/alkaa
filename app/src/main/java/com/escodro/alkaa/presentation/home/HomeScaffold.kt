package com.escodro.alkaa.presentation.home

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.escodro.alkaa.model.HomeSection
import com.escodro.designsystem.WindowSize

@Composable
internal fun CompactScaffold(
    modifier: Modifier,
    homeSection: HomeSection,
    actions: HomeActions,
    onShowBottomSheet: (SheetContentState) -> Unit,
    navItems: List<HomeSection>
) {
    Scaffold(
        topBar = {
            AlkaaTopBar(currentSection = homeSection)
        },
        content = {
            AlkaaContent(WindowSize.Compact, homeSection, modifier, actions, onShowBottomSheet)
        },
        bottomBar = {
            AlkaaBottomNav(
                currentSection = homeSection,
                onSectionSelect = actions.setCurrentSection,
                items = navItems
            )
        }
    )
}

@Composable
internal fun ExpandedScaffold(
    modifier: Modifier,
    homeSection: HomeSection,
    actions: HomeActions,
    onShowBottomSheet: (SheetContentState) -> Unit,
    navItems: List<HomeSection>
) {
    Scaffold(
        topBar = {
            AlkaaTopBar(currentSection = homeSection)
        },
        content = {
            Row(modifier = Modifier.fillMaxSize()) {
                AlkaaNavRail(
                    currentSection = homeSection,
                    onSectionSelect = actions.setCurrentSection,
                    items = navItems
                )
                AlkaaContent(WindowSize.Expanded, homeSection, modifier, actions, onShowBottomSheet)
            }
        },
    )
}
