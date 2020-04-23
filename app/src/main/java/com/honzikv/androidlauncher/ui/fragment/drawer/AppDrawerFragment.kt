package com.honzikv.androidlauncher.ui.fragment.drawer

import android.annotation.SuppressLint
import android.graphics.Color
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
import com.honzikv.androidlauncher.user.theme.Themer
import com.honzikv.androidlauncher.viewmodel.AppDrawerViewModel
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.app_drawer_fragment.*
import org.koin.android.ext.android.inject
import timber.log.Timber

class AppDrawerFragment : Fragment() {

    private val appDrawerViewModel: AppDrawerViewModel by inject()

    private val themer: Themer by inject()

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

        Blurry.with(context)
            .radius(25)
            .sampling(2)
            .color(Color.argb(66, 0, 255, 255))
            .capture(binding.imageView)
            .into(binding.imageView)
    }

    private fun returnToHomePageFragment() {
        navController.navigate(R.id.action_appDrawerFragment_to_homescreenPageFragment)
        Timber.d("From drawer to homepage")
    }
}
