package com.honzikv.androidlauncher.ui.fragment.dock

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.observe
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.databinding.DockFragmentBinding
import com.honzikv.androidlauncher.model.DockItemModel
import com.honzikv.androidlauncher.ui.fragment.dock.adapter.DockAdapter
import com.honzikv.androidlauncher.utils.MAX_ITEMS_IN_DOCK
import com.honzikv.androidlauncher.utils.gestures.RecyclerTouchListener
import com.honzikv.androidlauncher.viewmodel.DockViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class DockFragment : Fragment() {

    private val dockViewModel: DockViewModel by sharedViewModel()

    private val settingsViewModel: SettingsViewModel by sharedViewModel()

    private lateinit var dockAdapter: DockAdapter

    private lateinit var onSwipeTouchListener: RecyclerTouchListener

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
        dockAdapter = DockAdapter(settingsViewModel.getShowDockLabels())
        navController = findNavController()

        settingsViewModel.currentTheme.observe(viewLifecycleOwner, {
            binding.cardView.setCardBackgroundColor(it.dockBackgroundColor)
            dockAdapter.setLabelColor(it.dockTextColor)
        })

        settingsViewModel.showDockLabels.observe(viewLifecycleOwner, {
            dockAdapter.setShowLabels(it)
        })

        dockViewModel.dockItems.observe(viewLifecycleOwner, { itemList ->
            if (itemList.isEmpty()) {
                dockAdapter.setDockItems(mutableListOf(
                    DockItemModel(
                        position = 0,
                        packageName = ""
                    ).apply {
                        icon = ColorDrawable(Color.TRANSPARENT)
                    }
                ))
            } else {
                dockAdapter.setDockItems(itemList.toMutableList().apply { sortBy { it.position } })
            }
            dockAdapter.notifyDataSetChanged()
        })

        binding.recyclerView.apply {
            layoutManager =
                GridLayoutManager(requireContext(), MAX_ITEMS_IN_DOCK)
            adapter = dockAdapter
            ItemTouchHelper(itemTouchSwipeUpCallback).attachToRecyclerView(this)
        }


        onSwipeTouchListener = object :
            RecyclerTouchListener(requireContext(), binding.recyclerView) {
            override fun onSwipeTop() {
                super.onSwipeTop()
                navigateToAppDrawer()
            }

            override fun onClick(view: View, position: Int) {
                super.onClick(view, position)
                val item = dockAdapter.getItem(position)

                //Pro prazdny predmet, ktery je pritomen aby se dok zobrazil
                if (item.packageName.isNotEmpty()) {
                    requireContext().startActivity(
                        requireContext().packageManager.getLaunchIntentForPackage(item.packageName)
                    )
                }
            }

            override fun onLongClick(view: View, position: Int) {
                super.onLongClick(view, position)
                EditDockItemsDialogFragment.newInstance()
                    .show(requireActivity().supportFragmentManager, "editDock")
            }
        }

        binding.recyclerView.addOnItemTouchListener(onSwipeTouchListener)

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
