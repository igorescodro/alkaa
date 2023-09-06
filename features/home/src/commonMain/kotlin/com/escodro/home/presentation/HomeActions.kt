package com.escodro.home.presentation

/**
 * Actions to be performed on the Home screen.
 */
internal interface HomeActions {
    /**
     * Action to be performed when a task is clicked.
     */
    val onTaskClick: (Long) -> Unit

    /**
     * Action to be performed when the about item is clicked.
     */
    val onAboutClick: () -> Unit

    /**
     * Action to be performed when the task tracker item is clicked.
     */
    val onTrackerClick: () -> Unit

    /**
     * Action to be performed when the open source item is clicked.
     */
    val onOpenSourceClick: () -> Unit

    /**
     * Action to be performed when the task bottom sheet is opened.
     */
    val onTaskSheetOpen: () -> Unit

    /**
     * Action to be performed when the category bottom sheet is opened.
     */
    val onCategorySheetOpen: (Long?) -> Unit

    /**
     * Action to be performed when the current bottom nav section is changed.
     */
    val setCurrentSection: (HomeSection) -> Unit
}
