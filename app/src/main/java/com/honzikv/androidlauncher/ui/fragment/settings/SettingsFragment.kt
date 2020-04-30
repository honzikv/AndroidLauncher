package com.honzikv.androidlauncher.ui.fragment.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager

import com.honzikv.androidlauncher.databinding.SettingsFragmentBinding
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.*
import com.honzikv.androidlauncher.ui.fragment.settings.menu.LookAndFeelMenu
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import com.multilevelview.models.RecyclerViewItem
import org.koin.android.ext.android.inject

class SettingsFragment : Fragment() {

    private val viewModel: SettingsViewModel by inject()

    private lateinit var multiLevelAdapter: SettingsMenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = SettingsFragmentBinding.inflate(inflater)
        setupMenu(binding)
        return binding.root
    }

    private fun setupMenu(binding: SettingsFragmentBinding) {
        val itemList = mutableListOf<RecyclerViewItem>()
        itemList.add(Header("Settings", 0))

        val lookAndFeelMenu = LookAndFeelMenu(
            viewModel,
            requireContext()
        ).apply {
            itemList.add(getRoot())
            position = itemList.size
        }

        binding.multiLevelRecyclerView.layoutManager = LinearLayoutManager(context)
        multiLevelAdapter =
            SettingsMenuAdapter(
                itemList
            )
        binding.multiLevelRecyclerView.adapter = multiLevelAdapter
        binding.multiLevelRecyclerView.openTill(0, 1, 2, 3)
        binding.multiLevelRecyclerView.setAccordion(true)

        viewModel.currentTheme.observe(viewLifecycleOwner, {
            binding.multiLevelRecyclerView.setBackgroundColor(it.drawerBackgroundColor)
            multiLevelAdapter.apply {
                changeTheme(it)
                notifyDataSetChanged()
            }
        })

        viewModel.allThemes.observe(viewLifecycleOwner, {
            val selectTheme = lookAndFeelMenu.selectTheme
            val newItems = mutableListOf<Displayable>(object : Displayable {
                override fun toString(): String {
                    return "Choose a Theme"
                }
            })
            newItems.addAll(it)
            selectTheme.items = newItems
            selectTheme.adapter.clear()
            selectTheme.adapter.addAll(selectTheme.items)
            selectTheme.adapter.notifyDataSetChanged()
        })

        viewModel.currentTheme.observe(viewLifecycleOwner, {
            val currentTheme = lookAndFeelMenu.currentTheme
            currentTheme.textRight = it.name
            multiLevelAdapter.notifyItemChanged(lookAndFeelMenu.position)
        })
    }


}
