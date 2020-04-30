package com.honzikv.androidlauncher.ui.fragment.drawer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.data.model.entity.ThemeProfileModel

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
        val binding =
            AppDrawerFragmentBinding.inflate(inflater, container, false)
        initialize(binding)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initialize(binding: AppDrawerFragmentBinding) {
        appDrawerAdapter =
            AppDrawerAdapter()

        appDrawerViewModel.useRoundCorners.observe(viewLifecycleOwner, {
            useRoundCardView(binding, it)
        })

        appDrawerViewModel.currentTheme.observe(viewLifecycleOwner, {
            updateTheme(binding, it)
        })

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

        appDrawerViewModel.getDrawerApps().observe(viewLifecycleOwner, {
            appDrawerAdapter.updateData(it)
            runAnimationOnRecyclerView(
                binding.appDrawerRecyclerView,
                R.anim.drawer_layout_animation_fall_down
            )
        })

        navController = findNavController()

        binding.constraintLayout.setOnTouchListener(object :
            OnSwipeTouchListener(binding.root.context) {
            override fun onSwipeBottom() {
                super.onSwipeBottom()
                returnToHomePageFragment()
            }
        })

        binding.appDrawerCardView.radius = RADIUS_CARD_VIEW
        binding.searchCardView.radius = RADIUS_CARD_VIEW
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
        }
        else {
            binding.appDrawerCardView.radius = 0f
            binding.searchCardView.radius = 0f
        }
    }

    private fun returnToHomePageFragment() {
        navController.navigate(R.id.action_appDrawerFragment_to_homescreenPageFragment)
        Timber.d("From drawer to homepage")
    }

}
