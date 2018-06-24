package com.escodro.alkaa.ui.main

import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.escodro.alkaa.R
import com.escodro.alkaa.data.local.model.Category
import com.escodro.alkaa.ui.task.list.TaskListFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.architecture.ext.viewModel

/**
 * Main application [AppCompatActivity].
 *
 * @author Igor Escodro on 1/2/18.
 */
class MainActivity : AppCompatActivity(), MainDelegate {

    private val viewModel: MainViewModel by viewModel()

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        initComponents()
        viewModel.delegate = this
        viewModel.loadCategories()
    }

    private fun initComponents() {
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_main_navigation) as? NavHostFragment? ?: return

        navController = host.navController
        setupActionBar()
        updateDrawer()

        navigationview_main_drawer.setNavigationItemSelectedListener { item -> navigateToItem(item) }
    }

    private fun setupActionBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar_main_toolbar)
        setSupportActionBar(toolbar)
        navController?.let {
            NavigationUI.setupActionBarWithNavController(this, it, layout_main_parent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val shouldHandle =
            navController?.let { NavigationUI.onNavDestinationSelected(item, it) } ?: false

        return shouldHandle || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp() =
        navController?.let { NavigationUI.navigateUp(layout_main_parent, it) } ?: false

    override fun onBackPressed() {
        if (layout_main_parent.isDrawerOpen(GravityCompat.START)) {
            layout_main_parent.closeDrawer(GravityCompat.START)
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
        val hostDestinationId = navController?.graph?.startDestination
        val navOptions = hostDestinationId?.let {
            NavOptions.Builder().setPopUpTo(it, true).setLaunchSingleTop(true).build()
        }
        val bundle = bundleOf(
            TaskListFragment.EXTRA_CATEGORY_ID to item.itemId,
            TaskListFragment.EXTRA_CATEGORY_NAME to item.title
        )
        navController?.navigate(R.id.taskListFragment, bundle, navOptions)
        layout_main_parent.closeDrawer(GravityCompat.START)

        return true
    }

    override fun updateList(list: MutableList<Category>) {
        val menu = navigationview_main_drawer.menu
        menu.clear()
        menu.add(Menu.NONE, 0, Menu.NONE, R.string.drawer_menu_all_tasks)
        list.forEach { menu.add(Menu.NONE, it.id.toInt(), Menu.NONE, it.name) }
    }

    /**
     * Updates the [android.support.v4.widget.DrawerLayout] to create a effect where the drawer
     * stands still and the main content slides out.
     */
    private fun updateDrawer() {
        layout_main_parent.setScrimColor(Color.TRANSPARENT)
        layout_main_parent.drawerElevation = 0F

        val toggle = object : ActionBarDrawerToggle(this, layout_main_parent, null, 0, 0) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)

                navigationview_main_drawer.translationX =
                    layout_main_drawercontainer.width * (1 - slideOffset)
                layout_main_content.translationX = layout_main_drawercontainer.width * slideOffset
            }

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)

                viewModel.loadCategories()
            }
        }
        layout_main_parent.addDrawerListener(toggle)
    }
}
