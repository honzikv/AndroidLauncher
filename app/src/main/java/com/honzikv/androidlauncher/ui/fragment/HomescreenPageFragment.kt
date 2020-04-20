package com.honzikv.androidlauncher.ui.fragment

import android.os.Bundle
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.GestureDetectorCompat

import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.databinding.HomescreenPageFragmentBinding
import com.honzikv.androidlauncher.ui.gestures.OnSwipeTouchListener
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import org.koin.android.ext.android.inject
import timber.log.Timber

class HomescreenPageFragment : Fragment() {

    private lateinit var binding: HomescreenPageFragmentBinding

    private val homescreenViewModel: HomescreenViewModel by inject()

    private lateinit var onSwipeTouchListener: OnSwipeTouchListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HomescreenPageFragmentBinding.inflate(inflater)
        initialize(binding)
        return binding.root

    }

    private fun initialize(binding: HomescreenPageFragmentBinding) {
        binding.constraintLayout.setOnClickListener {
            longPressPopupMenu(binding.constraintLayout)
        }
        onSwipeTouchListener = object : OnSwipeTouchListener(binding.root.context) {
            override fun onSwipeTop() {
                super.onSwipeTop()
                swipeUpAppMenu()
            }
        }
    }

    private fun longPressPopupMenu(view: View) {
        PopupMenu(activity, view).apply {
            setOnMenuItemClickListener { item ->
                when (item!!.itemId) {
                    R.id.launcherSettings -> TODO("Launcher settings placeholder")
                    R.id.changeWallpaper -> TODO("Change Wallpaper")
                }

                true
            }
            inflate(R.menu.homescreen_long_click_popup_menu)
            show()
        }
    }

    private fun swipeUpAppMenu() {
        Timber.d("swipe up")
    }

}
