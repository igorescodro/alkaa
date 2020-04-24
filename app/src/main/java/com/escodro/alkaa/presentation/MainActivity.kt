package com.escodro.alkaa.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.escodro.alkaa.R
import com.escodro.alkaa.model.Category
import com.escodro.core.extension.close
import com.escodro.core.extension.hideKeyboard
import com.escodro.core.extension.isOpen
import com.escodro.core.extension.navigateSingleTop
import com.escodro.core.viewmodel.ToolbarViewModel
import com.escodro.splitinstall.SplitInstall
import com.escodro.task.presentation.list.TaskListFragment
import com.escodro.task.presentation.list.TaskListState
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * Main application [AppCompatActivity].
 */
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    private val sharedViewModel: ToolbarViewModel by viewModel()

    private var drawerSelectedItem = 0

    @Suppress("LateinitUsage")
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")

        setContentView(R.layout.activity_main)
        initComponents()
        viewModel.loadCategories(onListLoaded = ::updateDrawerList)
    }

    private fun initComponents() {
        Timber.d("initComponents()")

        val host = supportFragmentManager
            .findFragmentById(R.id.fragment_main_navigation) as? NavHostFragment? ?: return

        navController = host.navController
        setupActionBar()
        updateDrawer()

        navigationview_main_drawer.setNavigationItemSelectedListener { item -> navigate(item) }
    }

    private fun setupActionBar() {
        Timber.d("setupActionBar()")

        setSupportActionBar(toolbar_main_toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout_main_parent)

        navController.addOnDestinationChangedListener { _, dest, _ -> onNavigate(dest) }
        sharedViewModel.actionBarTitle.observe(this, Observer { updateToolbarTitle(it) })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Timber.d("onOptionsItemSelected() - item  = ${item.title}")

        val shouldHandle = NavigationUI.onNavDestinationSelected(item, navController)
        return shouldHandle || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp() =
        NavigationUI.navigateUp(navController, drawer_layout_main_parent)

    override fun onBackPressed() {
        if (drawer_layout_main_parent.isOpen()) {
            drawer_layout_main_parent.close()
        } else {
            super.onBackPressed()
        }
    }

    private fun onNavigate(dest: NavDestination) {
        val title = dest.label as? String?
        Timber.d("onNavigate() - title  = $title")

        // If the destination label is null ignore it. All the destinations without label are being
        // handled by the shared ViewModel.
        title?.let { updateToolbarTitle(title) }
        hideKeyboard()
    }

    private fun updateToolbarTitle(title: String?) {
        Timber.d("updateToolbarTitle() - title  = $title")

        toolbar_title.text = title
    }

    private fun updateDrawerList(list: List<Category>) {
        Timber.d("updateDrawerList() - Size = ${list.size}")

        val menu = navigationview_main_drawer.menu
        menu.clear()

        menu.add(GROUP_TASKS, ALL_TASKS_ITEM, Menu.NONE, R.string.drawer_menu_all_tasks)
        list.forEach { menu.add(GROUP_TASKS, it.id.toInt(), Menu.NONE, it.name) }
        menu.add(GROUP_TASKS, COMPLETED_ITEM, Menu.NONE, R.string.drawer_menu_completed_tasks)
        menu.add(GROUP_SETTINGS, CATEGORY_ITEM, Menu.NONE, R.string.drawer_menu_manage_categories)
        menu.add(GROUP_SETTINGS, TRACKER_ITEM, Menu.NONE, R.string.drawer_menu_tracker)
        menu.add(GROUP_SETTINGS, PREFERENCE_ITEM, Menu.NONE, R.string.drawer_menu_preferences)

        menu.setGroupCheckable(GROUP_TASKS, true, true)
        menu.setGroupCheckable(GROUP_SETTINGS, true, true)
        navigationview_main_drawer.setCheckedItem(drawerSelectedItem)
    }

    /**
     * Navigates to [TaskListFragment] opening only the tasks related to the category selected.
     * The back stack is cleared when the navigation is made.
     *
     * @return `true` indicating that the event was handled
     */
    private fun navigate(item: MenuItem): Boolean {
        Timber.d("navigate() - item  = ${item.title}")

        drawerSelectedItem = item.itemId
        when (drawerSelectedItem) {
            CATEGORY_ITEM -> navController.navigateSingleTop(R.id.nav_graph_category)
            TRACKER_ITEM -> openDynamicFeature()
            PREFERENCE_ITEM -> navController.navigateSingleTop(R.id.nav_graph_preference)
            else -> navigateToCategory(item)
        }

        drawer_layout_main_parent.close()
        return true
    }

    private fun navigateToCategory(item: MenuItem) {
        val state = when (item.itemId) {
            ALL_TASKS_ITEM -> TaskListState.ShowAllTasks
            COMPLETED_ITEM -> TaskListState.ShowCompletedTasks
            else -> TaskListState.ShowTaskByCategory(item.itemId.toLong())
        }

        val bundle = bundleOf(
            TaskListFragment.EXTRA_TASK_LIST_STATE to state,
            TaskListFragment.EXTRA_CATEGORY_NAME to item.title
        )
        navController.navigateSingleTop(R.id.taskListFragment, bundle)
    }

    /**
     * Updates the [androidx.drawerlayout.widget.DrawerLayout] to create a effect where the drawer
     * stands still and the main content slides out.
     */
    private fun updateDrawer() {
        Timber.d("updateDrawer()")

        val drawerListener = object : DrawerLayout.SimpleDrawerListener() {

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)

                val drawerWidth = layout_main_drawercontainer.width
                navigationview_main_drawer.translationX = drawerWidth * (1 - slideOffset)
                layout_main_content.translationX = drawerWidth * slideOffset
            }
        }

        drawer_layout_main_parent.apply {
            setScrimColor(getColor(R.color.gray_shadow_light))
            addDrawerListener(drawerListener)
        }
    }

    private fun openDynamicFeature() {
        SplitInstall(this).loadFeature(FEATURE_TRACKER) {
            onFeatureReady { navController.navigate(R.id.nav_graph_tracker) }
        }
    }

    companion object {

        // Drawer Menu Constants

        private const val ALL_TASKS_ITEM = 0

        private const val COMPLETED_ITEM = -1

        private const val CATEGORY_ITEM = -2

        private const val TRACKER_ITEM = -3

        private const val PREFERENCE_ITEM = -4

        // Drawer Menu Groups

        private const val GROUP_TASKS = 1

        private const val GROUP_SETTINGS = 2

        // Dynamic Features

        private const val FEATURE_TRACKER = "tracker"
    }
}
