package com.honzikv.androidlauncher.ui.fragment.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.honzikv.androidlauncher.databinding.SettingsFragmentBinding
import com.honzikv.androidlauncher.ui.adapter.multilevel.HeaderItem
import com.honzikv.androidlauncher.ui.adapter.multilevel.RadioButtonItem
import com.honzikv.androidlauncher.ui.adapter.multilevel.SettingsMenuAdapter
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import com.multilevelview.MultiLevelAdapter
import com.multilevelview.models.RecyclerViewItem
import org.koin.android.ext.android.inject

const val LOOK_AND_FEEL = "Look and Feel"
const val LOOK_AND_FEEL_SUB = "Customize theme of your launcher"

class SettingsFragment : Fragment() {

    private val settingsViewModel: SettingsViewModel by inject()

    private lateinit var multiLevelAdapter: MultiLevelAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = SettingsFragmentBinding.inflate(inflater)
        setupMenu(binding)
        return binding.root

    }

    private fun setupMenu(binding: SettingsFragmentBinding){
        val itemList = mutableListOf<RecyclerViewItem>()
        itemList.add(createLookAndFeelSubmenu())

        binding.multiLevelRecyclerView.layoutManager = LinearLayoutManager(context)
        multiLevelAdapter = SettingsMenuAdapter(itemList)
        binding.multiLevelRecyclerView.adapter = multiLevelAdapter
        binding.multiLevelRecyclerView.openTill(0,1,2,3)
        binding.multiLevelRecyclerView.setAccordion(true)
    }

    private fun createLookAndFeelSubmenu(): RecyclerViewItem {
        val headerItem = HeaderItem(LOOK_AND_FEEL, LOOK_AND_FEEL_SUB, 0)
        headerItem.showChildren = true

        val radioButtonItem1 =
            RadioButtonItem("sample", false, { println("sample") },  1)

        val radioButtonItem2 =
            RadioButtonItem("sample2", false, { println("sample") },  1)

        headerItem.addChildren(listOf(radioButtonItem1, radioButtonItem2))

        return headerItem
    }


}
