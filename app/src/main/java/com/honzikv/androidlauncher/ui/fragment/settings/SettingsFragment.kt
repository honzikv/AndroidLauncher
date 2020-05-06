package com.honzikv.androidlauncher.ui.fragment.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.honzikv.androidlauncher.data.model.FolderWithItems
import com.honzikv.androidlauncher.data.model.PageWithFolders

import com.honzikv.androidlauncher.databinding.SettingsFragmentBinding
import com.honzikv.androidlauncher.ui.fragment.dialog.AppPickerFragmentDialog
import com.honzikv.androidlauncher.ui.fragment.dialog.CreateFolderDialogFragment
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.*
import com.honzikv.androidlauncher.ui.fragment.settings.menu.DrawerMenu
import com.honzikv.androidlauncher.ui.fragment.settings.menu.HomescreenMenu
import com.honzikv.androidlauncher.ui.fragment.settings.menu.LookAndFeelMenu
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import com.multilevelview.models.RecyclerViewItem
import org.koin.android.ext.android.inject

class SettingsFragment : Fragment() {

    private val settingsViewModel: SettingsViewModel by inject()

    private val homescreenViewModel: HomescreenViewModel by inject()

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

        multiLevelAdapter = SettingsMenuAdapter(itemList)

        binding.multiLevelRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = multiLevelAdapter
        }

        settingsViewModel.currentTheme.observe(viewLifecycleOwner, {
            binding.multiLevelRecyclerView.setBackgroundColor(it.drawerBackgroundColor)
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

        homescreenViewModel.allPages.observe(viewLifecycleOwner, { pagesWithFolders ->
            updateHomescreenItems(homescreenMenu, pagesWithFolders)
        })
    }

    fun updateHomescreenItems(
        homescreenMenu: HomescreenMenu,
        pagesWithFolders: List<PageWithFolders>
    ) {

        val pages = mutableListOf<SettingsPageItem>()
        pagesWithFolders.forEach {
            pages.add(
                createPageSubMenu(
                    it,
                    homescreenMenu.managePages.level
                )
            )
        }
        if (homescreenMenu.managePages.hasChildren()) {
            homescreenMenu.managePages.children.clear()
        }
        homescreenMenu.managePages.addChildren(pages as List<RecyclerViewItem>?)
        multiLevelAdapter.notifyItemChanged(homescreenMenu.position)
    }

    private fun createPageSubMenu(pageWithFolders: PageWithFolders, level: Int): SettingsPageItem {
        //Todo might need list
        val page = SettingsPageItem(
            "Page ${pageWithFolders.page.pageNumber + 1}",
            { homescreenViewModel.deletePage(pageWithFolders.page) },
            {
                val fragment = CreateFolderDialogFragment.newInstance()
                fragment.show(requireActivity().supportFragmentManager, "createFolder")
            },
            level + 1
        )

        val folders = mutableListOf<SettingsFolderItem>()
        pageWithFolders.folderList.forEach { folders.add(createFolderSubMenu(it, page.level)) }
        return page.apply { addChildren(folders as List<RecyclerViewItem>?) }
    }

    private fun createFolderSubMenu(
        folderWithItems: FolderWithItems,
        level: Int
    ): SettingsFolderItem {
        val page = SettingsFolderItem(
            folderWithItems.folder.title,
            { homescreenViewModel.deleteFolder(folderWithItems.folder) },
            {
                val fragment = AppPickerFragmentDialog.newInstance()
                fragment.show(requireActivity().supportFragmentManager, "addItem")
            },
            level + 1
        )

        val items = mutableListOf<SettingsFolderItem>()
        return page
    }
}

