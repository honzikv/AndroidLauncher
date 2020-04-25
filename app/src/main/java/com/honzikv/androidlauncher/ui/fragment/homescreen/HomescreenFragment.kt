package com.honzikv.androidlauncher.ui.fragment.homescreen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.databinding.HomescreenFragmentBinding
import com.honzikv.androidlauncher.ui.gestures.OnSwipeTouchListener
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import org.koin.android.ext.android.inject
import timber.log.Timber

class HomescreenFragment : Fragment() {

    private lateinit var navController: NavController

    private lateinit var binding: HomescreenFragmentBinding

    private val homescreenViewModel: HomescreenViewModel by inject()

    private lateinit var onSwipeTouchListener: OnSwipeTouchListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = HomescreenFragmentBinding.inflate(inflater)
        initialize(binding)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initialize(binding: HomescreenFragmentBinding) {
        binding.constraintLayout.setOnClickListener {
            longPressPopupMenu(binding.constraintLayout)
        }
        binding.constraintLayout.setOnTouchListener(object :
            OnSwipeTouchListener(binding.root.context) {
            override fun onSwipeTop() {
                super.onSwipeTop()
                swipeUpAppMenu()
            }

            override fun onSwipeBottom() {
                super.onSwipeBottom()
                settings()
            }
        })
    }

    private fun settings() {
        navController.navigate(R.id.action_homescreenPageFragment_to_settingsFragment)
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
        navController.navigate(R.id.action_homescreenPageFragment_to_appDrawerFragment)
        Timber.d("swipe up")
    }

}
