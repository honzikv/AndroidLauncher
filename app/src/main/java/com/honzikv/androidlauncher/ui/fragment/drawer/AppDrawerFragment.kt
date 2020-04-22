package com.honzikv.androidlauncher.ui.fragment.drawer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.R

import com.honzikv.androidlauncher.databinding.AppDrawerFragmentBinding
import com.honzikv.androidlauncher.ui.adapter.AppDrawerAdapter
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
            AppDrawerFragmentBinding.inflate(inflater)
        initialize(binding)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initialize(binding: AppDrawerFragmentBinding) {
        appDrawerAdapter = AppDrawerAdapter()
        binding.appDrawerRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.appDrawerRecyclerView.adapter = appDrawerAdapter

        appDrawerViewModel.getAppList().observe(viewLifecycleOwner, {
            appDrawerAdapter.updateData(it)
        })

        navController = findNavController()

        binding.constraintLayout.setOnTouchListener(object :
            OnSwipeTouchListener(binding.root.context) {
            override fun onSwipeBottom() {
                super.onSwipeBottom()
                returnToHomePageFragment()
            }
        })
    }

    private fun returnToHomePageFragment() {
        navController.navigate(R.id.action_appDrawerFragment_to_homescreenPageFragment)
        Timber.d("From drawer to homepage")
    }
}
