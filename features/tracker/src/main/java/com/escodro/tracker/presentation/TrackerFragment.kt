package com.escodro.tracker.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.escodro.tracker.R
import com.escodro.tracker.di.injectDynamicFeature
import com.google.android.play.core.splitcompat.SplitCompat

/**
 * Temporary fragment for the new dynamic module.
 */
class TrackerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        injectDynamicFeature()
        return inflater.inflate(R.layout.fragment_tracker, container, false)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        SplitCompat.install(context)
    }
}
