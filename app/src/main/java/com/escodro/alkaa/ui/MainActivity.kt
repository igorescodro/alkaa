package com.escodro.alkaa.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.escodro.alkaa.ui.task.TaskFragment

/**
 * Main application [AppCompatActivity].
 *
 * @author Igor Escodro on 1/2/18.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, TaskFragment.newInstance()).commit()
    }
}
