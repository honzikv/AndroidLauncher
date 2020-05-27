package com.honzikv.androidlauncher.ui.fragment.drawer

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.model.ThemeProfileModel

import com.honzikv.androidlauncher.databinding.AppDrawerFragmentBinding
import com.honzikv.androidlauncher.ui.anim.runAnimationOnRecyclerView
import com.honzikv.androidlauncher.utils.RADIUS_CARD_VIEW
import com.honzikv.androidlauncher.ui.fragment.drawer.adapter.AppDrawerAdapter
import com.honzikv.androidlauncher.utils.gestures.OnSwipeTouchListener
import com.honzikv.androidlauncher.utils.DRAWER_GRID_COLUMNS
import com.honzikv.androidlauncher.utils.applyAlpha
import com.honzikv.androidlauncher.viewmodel.DrawerViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber


class DrawerFragment : Fragment() {

    private val drawerViewModel: DrawerViewModel by sharedViewModel()

    private val settingsViewModel: SettingsViewModel by sharedViewModel()

    private lateinit var appDrawerAdapter: AppDrawerAdapter

    private lateinit var navController: NavController

    private lateinit var binding: AppDrawerFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.d("creating view")
        navController = findNavController()
        binding =
            AppDrawerFragmentBinding.inflate(inflater, container, false)
        initialize()
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initialize() {
        appDrawerAdapter =
            AppDrawerAdapter()

        binding.appDrawerRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = appDrawerAdapter
        }

        binding.searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                appDrawerAdapter.filter.filter(newText)
                return false
            }
        })

        binding.constraintLayout.setOnTouchListener(object :
            OnSwipeTouchListener(binding.root.context) {
            override fun onSwipeBottom() {
                super.onSwipeBottom()
                returnToHomePageFragment()
            }
        })

        binding.settingsIcon.setOnClickListener {
            navigateToSettings()
        }

        settingsViewModel.useRoundCorners.observe(viewLifecycleOwner, { use ->
            useRoundCardView(binding, use)
        })

        settingsViewModel.currentTheme.observe(viewLifecycleOwner, { theme ->
            updateTheme(binding, theme)
        })

        settingsViewModel.showDrawerAsGrid.observe(viewLifecycleOwner, { useGrid ->
            useDrawerAsGrid(binding, useGrid)
            appDrawerAdapter.useGrid = useGrid
            appDrawerAdapter.notifyDataSetChanged()
        })

        drawerViewModel.getDrawerApps().observe(viewLifecycleOwner, {
            appDrawerAdapter.updateData(it)
            Timber.d("Running recycler view animation")
            runAnimationOnRecyclerView(
                binding.appDrawerRecyclerView,
                R.anim.drawer_layout_animation_fall_down
            )
            appDrawerAdapter.notifyDataSetChanged()
        })

        settingsViewModel.showSearchBar.observe(viewLifecycleOwner, { show ->
            if (show) {
                binding.searchView.visibility = View.VISIBLE
                binding.searchCardView.visibility = View.VISIBLE
            } else {
                binding.searchCardView.visibility = View.GONE
                binding.searchView.visibility = View.GONE
            }
        })
    }

    private fun useDrawerAsGrid(binding: AppDrawerFragmentBinding, use: Boolean) {
        binding.appDrawerRecyclerView.layoutManager = if (use) {
            GridLayoutManager(context, DRAWER_GRID_COLUMNS)
        } else {
            LinearLayoutManager(context)
        }
    }

    private fun updateTheme(binding: AppDrawerFragmentBinding, theme: ThemeProfileModel) {
        appDrawerAdapter.setLabelColor(theme.drawerTextFillColor)
        binding.constraintLayout.setBackgroundColor(applyAlpha(theme.drawerTextFillColor, 120))
        binding.appDrawerCardView.setCardBackgroundColor(theme.drawerBackgroundColor)
        binding.searchCardView.setCardBackgroundColor(theme.drawerSearchBackgroundColor)
        binding.searchView.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        binding.settingsIcon.setColorFilter(theme.drawerBackgroundColor)
        binding.allApps.setTextColor(theme.drawerBackgroundColor)
    }

    private fun useRoundCardView(binding: AppDrawerFragmentBinding, use: Boolean) {
        if (use) {
            binding.appDrawerCardView.radius =
                RADIUS_CARD_VIEW
            binding.searchCardView.radius =
                RADIUS_CARD_VIEW
        } else {
            binding.appDrawerCardView.radius = 0f
            binding.searchCardView.radius = 0f
        }
    }

    private fun returnToHomePageFragment() {
        navController.navigate(R.id.action_appDrawerFragment_to_homescreenPageFragment)
        Timber.d("From drawer to homepage")
    }

    private fun navigateToSettings() {
        navController.navigate(R.id.action_appDrawerFragment_to_settingsFragment)
        Timber.d("Navigating from drawer to settings")
    }

}
