package com.honzikv.androidlauncher.ui.fragment.page

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.tabs.TabLayoutMediator
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.databinding.HomescreenFragmentBinding
import com.honzikv.androidlauncher.ui.fragment.page.adapter.PageAdapter
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


class HomescreenFragment : Fragment() {

    private lateinit var navController: NavController

    private val homescreenViewModel: HomescreenViewModel by sharedViewModel()

    private val settingsViewModel: SettingsViewModel by sharedViewModel()

    private lateinit var viewPagerAdapter: PageAdapter

    private lateinit var swipeDownForNotification: MediatorLiveData<Boolean>

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

        viewPagerAdapter = PageAdapter(requireActivity())
        binding.viewPager.adapter = viewPagerAdapter

        binding.menuButton.setOnClickListener { navigateToAppDrawer() }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ ->
        }.attach()

        //Nastaveni barvy ikony
        settingsViewModel.currentTheme.observe(viewLifecycleOwner) {
            binding.menuButton.setColorFilter(it.dockBackgroundColor)
        }

        settingsViewModel.showPageDots.observe(viewLifecycleOwner) {
            if (!it) {
                binding.tabLayout.visibility = View.INVISIBLE
            } else {
                binding.tabLayout.visibility = View.VISIBLE
            }
        }

        settingsViewModel.showDock.observe(viewLifecycleOwner) {
            if (it) {
                binding.dockFragment.visibility = View.VISIBLE
            } else {
                binding.dockFragment.visibility = View.GONE
            }
        }

        swipeDownForNotification = MediatorLiveData<Boolean>().apply {
            value = settingsViewModel.getSwipeDownForNotifications()
            addSource(settingsViewModel.swipeDownForNotifications) { value = it }
        }

        homescreenViewModel.allPagesWithFolders.observe(viewLifecycleOwner, {
            viewPagerAdapter.setPages(it)
            viewPagerAdapter.notifyDataSetChanged()
        })
    }

    private fun navigateToAppDrawer() {
        Timber.d("Navigating from homescreen fragment to app drawer fragment")
        navController.navigate(R.id.action_homescreenPageFragment_to_appDrawerFragment)
    }


}
