package com.honzikv.androidlauncher.ui.fragment.homescreen

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.observe
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager

import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.databinding.DockFragmentBinding
import com.honzikv.androidlauncher.ui.fragment.dialog.EditDockItemsDialogFragment
import com.honzikv.androidlauncher.ui.fragment.homescreen.adapter.DockAdapter
import com.honzikv.androidlauncher.ui.gestures.OnSwipeTouchListener
import com.honzikv.androidlauncher.viewmodel.DockViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.ext.android.inject
import timber.log.Timber

class DockFragment : Fragment() {

    private val dockViewModel: DockViewModel by inject()

    private val settingsViewModel: SettingsViewModel by inject()

    private lateinit var dockAdapter: DockAdapter

    private lateinit var onSwipeTouchListener: OnSwipeTouchListener

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DockFragmentBinding.inflate(inflater)
        initialize(binding)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initialize(binding: DockFragmentBinding) {
        dockAdapter = DockAdapter(settingsViewModel.getShowDockLabels())
        settingsViewModel.currentTheme.observe(viewLifecycleOwner, {
            //todo opacity
            binding.cardView.apply {
                setCardBackgroundColor(it.dockBackgroundColor)
            }
            dockAdapter.setLabelColor(it.dockTextColor)
        })

        settingsViewModel.showDockLabels.observe(viewLifecycleOwner, {
            dockAdapter.setShowLabels(it)
        })

        dockViewModel.dockItems.observe(viewLifecycleOwner, {
            dockAdapter.setDockItems(it)
            dockAdapter.notifyDataSetChanged()
        })

        onSwipeTouchListener = object :
            OnSwipeTouchListener(requireContext()) {
            override fun onSwipeTop() {
                super.onSwipeTop()
                Timber.d("Swiping top")
                navigateToAppDrawer()
            }

            override fun onSwipeRight() {
                super.onSwipeBottom()
                navigateToSettings()
            }
        }

        binding.recyclerView.apply {
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = dockAdapter
        }

        binding.recyclerView.setOnTouchListener(onSwipeTouchListener)
        binding.recyclerView.setOnLongClickListener {
            Timber.d("EditDockItemListDialog is starting")
            EditDockItemsDialogFragment.newInstance()
                .show(requireActivity().supportFragmentManager, "editDock")

            return@setOnLongClickListener true
        }

        navController = findNavController()

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
