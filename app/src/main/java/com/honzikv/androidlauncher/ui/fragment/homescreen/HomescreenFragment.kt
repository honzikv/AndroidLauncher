package com.honzikv.androidlauncher.ui.fragment.homescreen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.databinding.HomescreenFragmentBinding
import com.honzikv.androidlauncher.ui.fragment.homescreen.adapter.PageAdapter
import com.honzikv.androidlauncher.ui.gestures.OnSwipeTouchListener
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import org.koin.android.ext.android.inject
import timber.log.Timber

class HomescreenFragment : Fragment() {

    private lateinit var navController: NavController

    private val homescreenViewModel: HomescreenViewModel by inject()

    private lateinit var viewPagerAdapter: PageAdapter

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
        onSwipeTouchListener = object :
            OnSwipeTouchListener(requireContext()) {
            override fun onSwipeTop() {
                super.onSwipeTop()
                navigateToAppDrawer()
            }

            override fun onSwipeBottom() {
                super.onSwipeBottom()
                navigateToSettings()
            }
        }

        binding.constraintLayout.setOnTouchListener(onSwipeTouchListener)
        viewPagerAdapter =
            PageAdapter(requireActivity(), onSwipeTouchListener)
        binding.viewPager.adapter = viewPagerAdapter

        homescreenViewModel.allPages.observe(viewLifecycleOwner, {
            viewPagerAdapter.setPages(it)
            viewPagerAdapter.notifyDataSetChanged()
        })

        binding.constraintLayout.setOnLongClickListener { view ->
            longPressPopupMenu(view)
            return@setOnLongClickListener true
        }
    }

    private fun longPressPopupMenu(view: View) {
        Timber.d("Creating Popup Menu")
        PopupMenu(context, view).apply {
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

    private fun navigateToSettings() {
        Timber.d("Navigating from homescreen fragment to settings fragment")
        navController.navigate(R.id.action_homescreenPageFragment_to_settingsFragment)
    }


    private fun navigateToAppDrawer() {
        Timber.d("Navigating from homescreen fragment to app drawer fragment")
        navController.navigate(R.id.action_homescreenPageFragment_to_appDrawerFragment)
    }


}
