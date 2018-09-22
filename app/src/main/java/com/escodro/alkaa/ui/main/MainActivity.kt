package com.escodro.alkaa.ui.main

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.escodro.alkaa.R
import com.escodro.alkaa.common.extension.close
import com.escodro.alkaa.common.extension.isOpen
import com.escodro.alkaa.common.extension.navigateSingleTop
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.ui.task.list.TaskListFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

/**
 * Main application [AppCompatActivity].
 */
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()

    private var drawerSelectedItem = 0

    @Suppress("LateinitUsage")
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("onCreate()")

        setContentView(R.layout.activity_main)
        initComponents()
        viewModel.loadCategories(onListLoaded = { updateList(it) })
    }

    private fun initComponents() {
        Timber.d("initComponents()")

        val host = supportFragmentManager
            .findFragmentById(R.id.fragment_main_navigation) as? NavHostFragment? ?: return

        navController = host.navController
        setupActionBar()
        updateDrawer()

        navigationview_main_drawer
            .setNavigationItemSelectedListener { item -> navigateToItem(item) }
    }

    private fun setupActionBar() {
        Timber.d("setupActionBar()")

        setSupportActionBar(toolbar_main_toolbar)
        NavigationUI.setupActionBarWithNavController(this, navController, drawer_layout_main_parent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Timber.d("onOptionsItemSelected() - item  = ${item.title}")

        val shouldHandle = NavigationUI.onNavDestinationSelected(item, navController)
        return shouldHandle || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp() =
        NavigationUI.navigateUp(drawer_layout_main_parent, navController)

    override fun onBackPressed() {
        if (drawer_layout_main_parent.isOpen()) {
            drawer_layout_main_parent.close()
        } else {
            super.onBackPressed()
        }
    }

    /**
     * Navigates to [TaskListFragment] opening only the tasks related to the category selected.
     * The back stack is cleared when the navigation is made.
     *
     * @return `true` indicating that the event was handled
     */
    private fun navigateToItem(item: MenuItem): Boolean {
        Timber.d("navigateToItem() - item  = ${item.title}")

        drawerSelectedItem = item.itemId
        val bundle = bundleOf(
            TaskListFragment.EXTRA_CATEGORY_ID to item.itemId.toLong(),
            TaskListFragment.EXTRA_CATEGORY_NAME to item.title
        )
        navController.navigateSingleTop(R.id.taskListFragment, bundle)
        drawer_layout_main_parent.close()

        return true
    }

    private fun updateList(list: List<Category>) {
        Timber.d("updateList() - Size = ${list.size}")

        val menu = navigationview_main_drawer.menu
        menu.clear()
        menu.add(Menu.NONE, 0, Menu.NONE, R.string.drawer_menu_all_tasks).isCheckable = true
        list.forEach { menu.add(Menu.NONE, it.id.toInt(), Menu.NONE, it.name).isCheckable = true }
        navigationview_main_drawer.setCheckedItem(drawerSelectedItem)
    }

    /**
     * Updates the [android.support.v4.widget.DrawerLayout] to create a effect where the drawer
     * stands still and the main content slides out.
     */
    private fun updateDrawer() {
        Timber.d("updateDrawer()")

        val toggle = object : ActionBarDrawerToggle(this, drawer_layout_main_parent, 0, 0) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)

                val drawerWidth = layout_main_drawercontainer.width
                navigationview_main_drawer.translationX = drawerWidth * (1 - slideOffset)
                layout_main_content.translationX = drawerWidth * slideOffset
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)

                viewModel.loadCategories(onListLoaded = { updateList(it) })
            }
        }
        drawer_layout_main_parent.apply {
            setScrimColor(Color.TRANSPARENT)
            drawerElevation = 0F
            addDrawerListener(toggle)
            toggle.syncState()
        }
    }
}
