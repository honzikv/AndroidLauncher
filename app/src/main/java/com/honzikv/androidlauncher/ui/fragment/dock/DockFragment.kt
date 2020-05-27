package com.honzikv.androidlauncher.ui.fragment.dock

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.observe
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.databinding.DockFragmentBinding
import com.honzikv.androidlauncher.ui.fragment.dock.adapter.DockAdapter
import com.honzikv.androidlauncher.ui.gestures.OnSwipeTouchListener
import com.honzikv.androidlauncher.viewmodel.DockViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class DockFragment : Fragment() {

    private val dockViewModel: DockViewModel by sharedViewModel()

    private val settingsViewModel: SettingsViewModel by sharedViewModel()

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

    private val itemTouchSwipeUpCallback = object : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.UP or ItemTouchHelper.DOWN
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            if (direction == ItemTouchHelper.UP) {
                navController.navigate(R.id.action_homescreenPageFragment_to_appDrawerFragment)
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initialize(binding: DockFragmentBinding) {
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

        dockAdapter = DockAdapter(settingsViewModel.getShowDockLabels())

        settingsViewModel.currentTheme.observe(viewLifecycleOwner, {
            binding.cardView.setCardBackgroundColor(it.dockBackgroundColor)
            dockAdapter.setLabelColor(it.dockTextColor)
        })

        settingsViewModel.showDockLabels.observe(viewLifecycleOwner, {
            dockAdapter.setShowLabels(it)
        })

        dockViewModel.dockItems.observe(viewLifecycleOwner, { itemList ->
            dockAdapter.setDockItems(itemList.toMutableList().apply { sortBy { it.position } })
            dockAdapter.notifyDataSetChanged()
        })

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = dockAdapter
            ItemTouchHelper(itemTouchSwipeUpCallback).attachToRecyclerView(this)
        }

        binding.recyclerView.setOnTouchListener(onSwipeTouchListener)
        binding.recyclerView.setOnLongClickListener {
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
