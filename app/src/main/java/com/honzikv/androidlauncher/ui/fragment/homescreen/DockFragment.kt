package com.honzikv.androidlauncher.ui.fragment.homescreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.observe
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.databinding.DockFragmentBinding
import com.honzikv.androidlauncher.ui.fragment.homescreen.adapter.DockAdapter
import com.honzikv.androidlauncher.viewmodel.DockViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.ext.android.inject

class DockFragment : Fragment() {

    private val dockViewModel: DockViewModel by inject()

    private val settingsViewModel: SettingsViewModel by inject()

    private lateinit var dockAdapter: DockAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DockFragmentBinding.inflate(inflater)
        initialize(binding)
        return binding.root
    }

    private fun initialize(binding: DockFragmentBinding) {
        dockAdapter = DockAdapter(settingsViewModel.getShowDockLabels())
        settingsViewModel.currentTheme.observe(viewLifecycleOwner, {
            binding.recyclerView.setBackgroundColor(it.dockBackgroundColor)
            dockAdapter.setLabelColor(it.dockTextColor)
        })

        settingsViewModel.showDockLabels.observe(viewLifecycleOwner, {
            dockAdapter.setShowLabels(it)
        })

        dockViewModel.dockItems.observe(viewLifecycleOwner, {
            dockAdapter.setDockItems(it)
            dockAdapter.notifyDataSetChanged()
        })

        binding.recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerView.adapter = dockAdapter
    }

}
