package com.escodro.alkaa.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.escodro.alkaa.R

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
            .findFragmentById(R.id.navigation_host) as? NavHostFragment? ?: return

        navController = host.navController
        setupActionBar()
    }

    private fun setupActionBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        navController?.let { NavigationUI.setupActionBarWithNavController(this, it) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val shouldHandle = navController?.let { NavigationUI.onNavDestinationSelected(item, it) }
            ?: false

        return shouldHandle || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp() =
        navController?.navigateUp() ?: false
}
