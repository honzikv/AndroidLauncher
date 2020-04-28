package com.honzikv.androidlauncher.ui.fragment.drawer

import android.annotation.SuppressLint
import android.content.ClipData
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.data.model.entity.ThemeProfileModel

import com.honzikv.androidlauncher.databinding.AppDrawerFragmentBinding
import com.honzikv.androidlauncher.ui.adapter.AppDrawerAdapter
import com.honzikv.androidlauncher.ui.anim.runAnimationOnRecyclerView
import com.honzikv.androidlauncher.ui.gestures.OnSwipeTouchListener
import com.honzikv.androidlauncher.viewmodel.AppDrawerViewModel
import org.koin.android.ext.android.inject
import timber.log.Timber

val darkTheme = ThemeProfileModel(
    id = null,
    drawerBackgroundColor = Color.parseColor("#2d3436"),
    drawerTextFillColor = Color.parseColor("#dfe6e9"),
    drawerSearchBackgroundColor = Color.parseColor("#0984e3"),
    drawerSearchTextColor = Color.parseColor("#2d3436"),
    dockBackgroundColor = Color.parseColor("#636e72"),
    dockTextColor = Color.parseColor("#b2bec3"),
    name = "Dark Theme",
    isSelected = false,
    isUserProfile = false
)

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
        appDrawerAdapter = AppDrawerAdapter()

        appDrawerViewModel.selectedProfile.observe(viewLifecycleOwner, {
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

        updateTheme(binding, appDrawerViewModel.currentProfile)
    }

    private fun updateTheme(binding: AppDrawerFragmentBinding, theme: ThemeProfileModel) {
        appDrawerAdapter.setLabelColor(theme.drawerTextFillColor)
        binding.appDrawerRecyclerView.setBackgroundColor(theme.drawerBackgroundColor)
        binding.searchView.apply {
            setBackgroundColor(theme.drawerSearchBackgroundColor)
            val searchText =
                context.resources.getIdentifier("android:id/search_src_text", null, null)
//            findViewById<TextView>(searchText).apply {
//                val textColor = theme.drawerSearchTextColor
//                setTextColor(textColor)
//                setHintTextColor(textColor)
//            }
        }
    }

    private fun returnToHomePageFragment() {
        navController.navigate(R.id.action_appDrawerFragment_to_homescreenPageFragment)
        Timber.d("From drawer to homepage")
    }

}
