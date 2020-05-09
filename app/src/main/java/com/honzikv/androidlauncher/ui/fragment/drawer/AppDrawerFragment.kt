package com.honzikv.androidlauncher.ui.fragment.drawer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.data.model.ThemeProfileModel

import com.honzikv.androidlauncher.databinding.AppDrawerFragmentBinding
import com.honzikv.androidlauncher.ui.anim.runAnimationOnRecyclerView
import com.honzikv.androidlauncher.ui.constants.RADIUS_CARD_VIEW
import com.honzikv.androidlauncher.ui.fragment.drawer.adapter.AppDrawerAdapter
import com.honzikv.androidlauncher.ui.gestures.OnSwipeTouchListener
import com.honzikv.androidlauncher.viewmodel.AppDrawerViewModel
import org.koin.android.ext.android.inject
import timber.log.Timber


class AppDrawerFragment : Fragment() {

    private val appDrawerViewModel: AppDrawerViewModel by inject()

    private lateinit var appDrawerAdapter: AppDrawerAdapter

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        navController = findNavController()
        val binding =
            AppDrawerFragmentBinding.inflate(inflater, container, false)
        initialize(binding)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initialize(binding: AppDrawerFragmentBinding) {
        appDrawerAdapter =
            AppDrawerAdapter()

        binding.appDrawerRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = appDrawerAdapter
            registerForContextMenu(this)
            setOnLongClickListener {
                true
            }
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

        appDrawerViewModel.useRoundCorners.observe(viewLifecycleOwner, { use ->
            useRoundCardView(binding, use)
        })

        appDrawerViewModel.currentTheme.observe(viewLifecycleOwner, { theme ->
            updateTheme(binding, theme)
        })

        appDrawerViewModel.showDrawerAsGrid.observe(viewLifecycleOwner, { useGrid ->
            useDrawerAsGrid(binding, useGrid)
            appDrawerAdapter.useGrid = useGrid
            appDrawerAdapter.notifyDataSetChanged()
        })

        appDrawerViewModel.getDrawerApps().observe(viewLifecycleOwner, {
            appDrawerAdapter.updateData(it)
            Timber.d("Running recycler view animation")
            runAnimationOnRecyclerView(
                binding.appDrawerRecyclerView,
                R.anim.drawer_layout_animation_fall_down
            )
            appDrawerAdapter.notifyDataSetChanged()
        })

        appDrawerViewModel.showSearchBar.observe(viewLifecycleOwner, { show ->
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
            GridLayoutManager(context, 5)
        } else {
            LinearLayoutManager(context)
        }
    }

    private fun updateTheme(binding: AppDrawerFragmentBinding, theme: ThemeProfileModel) {
        appDrawerAdapter.setLabelColor(theme.drawerTextFillColor)
        binding.appDrawerCardView.setCardBackgroundColor(theme.drawerBackgroundColor)
        binding.searchCardView.setCardBackgroundColor(theme.drawerSearchBackgroundColor)
    }

    private fun useRoundCardView(binding: AppDrawerFragmentBinding, use: Boolean) {
        if (use) {
            binding.appDrawerCardView.radius = RADIUS_CARD_VIEW
            binding.searchCardView.radius = RADIUS_CARD_VIEW
        } else {
            binding.appDrawerCardView.radius = 0f
            binding.searchCardView.radius = 0f
        }
    }

    private fun returnToHomePageFragment() {
        navController.navigate(R.id.action_appDrawerFragment_to_homescreenPageFragment)
        Timber.d("From drawer to homepage")
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        view: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, view, menuInfo)

        when (view.id) {
            R.id.appDrawerRecyclerView -> createRecyclerViewPopupMenu(menu, view)
        }
    }

    private fun createRecyclerViewPopupMenu(menu: ContextMenu, view: View) {
        view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        activity?.menuInflater?.inflate(R.menu.app_drawer_on_long_click_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return super.onContextItemSelected(item)
    }
}
