package com.honzikv.androidlauncher.ui.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.honzikv.androidlauncher.databinding.SettingsFragmentBinding
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.Displayable
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.Header
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.SettingsMenuAdapter
import com.honzikv.androidlauncher.ui.fragment.settings.menu.DrawerMenu
import com.honzikv.androidlauncher.ui.fragment.settings.menu.HomescreenMenu
import com.honzikv.androidlauncher.ui.fragment.settings.menu.LookAndFeelMenu
import com.honzikv.androidlauncher.utils.SETTINGS_BACKGROUND_ALPHA
import com.honzikv.androidlauncher.utils.applyAlpha
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import com.multilevelview.models.RecyclerViewItem
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * Fragment s nastavenim aplikace
 */
class SettingsFragment : Fragment() {

    private val settingsViewModel: SettingsViewModel by sharedViewModel()

    /**
     * Adapter pro multi level recycler view
     */
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

        //Vytvoreni look and feel menu
        val lookAndFeelMenu = LookAndFeelMenu(settingsViewModel, requireContext()).apply {
            position = itemList.size
            itemList.add(getRoot())
        }

        //Vytvoreni drawer menu
        val drawerMenu = DrawerMenu(settingsViewModel).apply {
            position = itemList.size
            itemList.add(getRoot())
        }

        //Vytvoreni homescreen settings submenu
        val homescreenMenu = HomescreenMenu(settingsViewModel, requireActivity()).apply {
            position = itemList.size
            itemList.add(getRoot())
        }

        multiLevelAdapter = SettingsMenuAdapter(itemList, binding.multiLevelRecyclerView)

        binding.multiLevelRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = multiLevelAdapter
            //Odstrani onClickListener u kazdeho predmetu, aby slo kliknout na jednotlive casti View
            //Jinak by neslo klikat na switche, dropdown menu apod.
            removeItemClickListeners()
        }

        //Nastaveni barev podle aktualniho tematu
        settingsViewModel.currentTheme.observe(viewLifecycleOwner, {
            binding.multiLevelRecyclerView.setBackgroundColor(
                applyAlpha(it.drawerBackgroundColor, SETTINGS_BACKGROUND_ALPHA)
            )
            lookAndFeelMenu.currentTheme.textRight = it.name
            multiLevelAdapter.apply {
                setTheme(it)
                notifyDataSetChanged()
            }
        })

        //Prida vsechny temata do select theme spinneru
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

        //Zmena barev pri zmene tema
        settingsViewModel.currentTheme.observe(viewLifecycleOwner, {
            val currentTheme = lookAndFeelMenu.currentTheme
            currentTheme.textRight = it.name
            multiLevelAdapter.notifyDataSetChanged()
        })

    }


}

