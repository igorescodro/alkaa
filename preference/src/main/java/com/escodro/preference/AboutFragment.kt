package com.escodro.preference

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.escodro.core.extension.openUrl
import kotlinx.android.synthetic.main.fragment_about.*
import timber.log.Timber

/**
 * [Fragment] responsible to show information about the application.
 */
class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("onCreateView()")

        return inflater.inflate(R.layout.fragment_about, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startDrawableAnimation()
        initComponents()
    }

    private fun startDrawableAnimation() {
        val animationDrawable = view_about_background.background as? AnimationDrawable
        animationDrawable?.apply {
            setEnterFadeDuration(FADE_ANIMATION_DURATION)
            setExitFadeDuration(FADE_ANIMATION_DURATION)
            start()
        }
    }

    private fun initComponents() {
        button_about_project.setOnClickListener { context?.openUrl(PROJECT_URL) }
    }

    companion object {

        private const val FADE_ANIMATION_DURATION = 4_000

        private const val PROJECT_URL = "https://github.com/igorescodro/alkaa"
    }
}
