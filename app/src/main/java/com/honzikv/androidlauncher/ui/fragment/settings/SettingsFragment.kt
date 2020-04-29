package com.honzikv.androidlauncher.ui.fragment.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.data.model.entity.ThemeProfileModel

import com.honzikv.androidlauncher.databinding.SettingsFragmentBinding
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.HeaderItem
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.SwitchItem
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.SettingsMenuAdapter
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.SpinnerItem
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import com.multilevelview.MultiLevelAdapter
import com.multilevelview.MultiLevelRecyclerView
import com.multilevelview.models.RecyclerViewItem
import org.koin.android.ext.android.inject
import timber.log.Timber

class SettingsFragment : Fragment() {

    companion object {
        const val LOOK_AND_FEEL = "Look and Feel"
        const val LOOK_AND_FEEL_SUB = "Customize theme of your launcher"
        const val SELECT_THEME = "Select Theme"
        const val SWIPE_DOWN_NOTIFICATIONS = "Swipe Down for Notification Panel"
        const val USE_ANIMATIONS = "Use Animations"
        const val SHOW_DOCK = "Show Dock"
        const val ONE_HANDED_MODE = "Use One Handed Mode"
    }

    private val viewModel: SettingsViewModel by inject()

    private lateinit var multiLevelAdapter: MultiLevelAdapter

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
        itemList.add(createLookAndFeelSubmenu())

        binding.multiLevelRecyclerView.layoutManager = LinearLayoutManager(context)
        multiLevelAdapter =
            SettingsMenuAdapter(
                itemList
            )
        binding.multiLevelRecyclerView.adapter = multiLevelAdapter
        binding.multiLevelRecyclerView.openTill(0, 1, 2, 3)
        binding.multiLevelRecyclerView.setAccordion(true)
    }

    private fun createLookAndFeelSubmenu(): RecyclerViewItem {
        val lookAndFeel =
            HeaderItem(
                LOOK_AND_FEEL,
                LOOK_AND_FEEL_SUB,
                0
            )
        lookAndFeel.showChildren = true

        val selectTheme = SpinnerItem(
            SELECT_THEME,
            viewModel.allThemes.value ?: mutableListOf(),
            { viewModel.changeTheme(it as ThemeProfileModel) },
            requireContext(),
            1
        )
        viewModel.allThemes.observe(viewLifecycleOwner, {
            selectTheme.items = it
            selectTheme.adapter.clear()
            selectTheme.adapter.addAll(selectTheme.items)
            selectTheme.adapter.notifyDataSetChanged()
        })

        Timber.d("size = ${viewModel.allThemes.value?.size}")

        val swipeDownToOpenNotificationsRadio = SwitchItem(
            SWIPE_DOWN_NOTIFICATIONS,
            viewModel.getSwipeDownForNotifications(),
            { viewModel.setSwipeDownForNotifications(it) },
            1
        )

        val showDock = SwitchItem(
            SHOW_DOCK,
            viewModel.getShowDock(),
            { viewModel.setShowDock(it) },
            1
        )

        val oneHandedMode = SwitchItem(
            ONE_HANDED_MODE,
            viewModel.getUseOneHandedMode(),
            { viewModel.setUseOneHandedMode(it) },
            1
        )

        lookAndFeel.addChildren(listOf(selectTheme, swipeDownToOpenNotificationsRadio, showDock))

        return lookAndFeel
    }


}
