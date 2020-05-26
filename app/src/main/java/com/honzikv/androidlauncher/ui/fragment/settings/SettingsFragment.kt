package com.honzikv.androidlauncher.ui.fragment.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.honzikv.androidlauncher.model.PageWithFolders

import com.honzikv.androidlauncher.databinding.SettingsFragmentBinding
import com.honzikv.androidlauncher.ui.fragment.page.EditPageItemsDialogFragment
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.*
import com.honzikv.androidlauncher.ui.fragment.settings.menu.DrawerMenu
import com.honzikv.androidlauncher.ui.fragment.settings.menu.HomescreenMenu
import com.honzikv.androidlauncher.ui.fragment.settings.menu.LookAndFeelMenu
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import com.multilevelview.models.RecyclerViewItem
import org.koin.android.viewmodel.ext.android.sharedViewModel
import timber.log.Timber

class SettingsFragment : Fragment() {

    private val settingsViewModel: SettingsViewModel by sharedViewModel()

    private val homescreenViewModel: HomescreenViewModel by sharedViewModel()

    private lateinit var multiLevelAdapter: SettingsMenuAdapter

    private lateinit var binding: SettingsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SettingsFragmentBinding.inflate(inflater)
        setupMenu(binding)
        return binding.root
    }

    private fun setupMenu(binding: SettingsFragmentBinding) {
        val itemList = mutableListOf<RecyclerViewItem>()
        itemList.add(Header("Settings", 0))

        //Create a look and feel settings submenu
        val lookAndFeelMenu = LookAndFeelMenu(settingsViewModel, requireContext()).apply {
            position = itemList.size
            itemList.add(getRoot())
        }

        //Create a drawer settings submenu
        val drawerMenu = DrawerMenu(settingsViewModel).apply {
            position = itemList.size
            itemList.add(getRoot())
        }

        //Create a homescreen settings submenu
        val homescreenMenu = HomescreenMenu(settingsViewModel, requireActivity()).apply {
            position = itemList.size
            itemList.add(getRoot())
        }

        multiLevelAdapter = SettingsMenuAdapter(itemList, binding.multiLevelRecyclerView)

        binding.multiLevelRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = multiLevelAdapter
            //Odstrani onClickListener u kazdeho predmetu, aby slo kliknout na jednotlive casti View
            removeItemClickListeners()
        }

        settingsViewModel.currentTheme.observe(viewLifecycleOwner, {
            binding.multiLevelRecyclerView.setBackgroundColor(it.drawerBackgroundColor)
            lookAndFeelMenu.currentTheme.textRight = it.name
            multiLevelAdapter.apply {
                changeTheme(it)
                notifyDataSetChanged()
            }
        })

        settingsViewModel.allThemes.observe(viewLifecycleOwner, {
            val selectTheme = lookAndFeelMenu.selectTheme

            val newItems = mutableListOf<Displayable>(object : Displayable {
                override fun toString(): String {
                    return "Choose a Theme"
                }
            })
            newItems.addAll(it)
            selectTheme.adapter.apply {
                clear()
                addAll(newItems)
                notifyDataSetChanged()
            }
        })

        settingsViewModel.currentTheme.observe(viewLifecycleOwner, {
            val currentTheme = lookAndFeelMenu.currentTheme
            currentTheme.textRight = it.name
            multiLevelAdapter.notifyDataSetChanged()
        })

    }



}

