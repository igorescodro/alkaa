package com.escodro.alkaa.ui

import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.escodro.alkaa.R
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Main application [AppCompatActivity].
 *
 * @author Igor Escodro on 1/2/18.
 */
class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        initComponents()
    }

    private fun initComponents() {
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment_main_navigation) as? NavHostFragment? ?: return

        navController = host.navController
        setupActionBar()
        updateDrawer()
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
     * Updates the [android.support.v4.widget.DrawerLayout] to create a effect where the drawer
     * stands still and the main content slides out.
     */
    private fun updateDrawer() {
        layout_main_parent.setScrimColor(Color.TRANSPARENT)
        layout_main_parent.drawerElevation = 0F

        val toggle = object : ActionBarDrawerToggle(this, layout_main_parent, null, 0, 0) {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)

                layout_main_drawercontent.translationX = layout_main_drawercontainer.width * (1 -
                    slideOffset)
                layout_main_content.translationX = layout_main_drawercontainer.width * slideOffset
            }
        }
        layout_main_parent.addDrawerListener(toggle)
    }
}
