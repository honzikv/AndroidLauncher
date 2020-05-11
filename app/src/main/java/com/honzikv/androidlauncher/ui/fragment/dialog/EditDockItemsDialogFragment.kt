package com.honzikv.androidlauncher.ui.fragment.dialog

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.honzikv.androidlauncher.MAX_ITEMS_IN_DOCK
import com.honzikv.androidlauncher.databinding.EditHomescreenContainerFragmentBinding
import com.honzikv.androidlauncher.ui.fragment.dialog.adapter.EditDockAdapter
import com.honzikv.androidlauncher.viewmodel.DockViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.ext.android.inject

class EditDockItemsDialogFragment private constructor() : DialogFragment() {

    private val dockViewModel: DockViewModel by inject()

    private val settingsViewModel: SettingsViewModel by inject()

    private lateinit var dockItemAdapter: EditDockAdapter

    companion object {
        fun newInstance() = EditDockItemsDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.ThemeOverlay_Material_Dark);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = EditHomescreenContainerFragmentBinding.inflate(inflater)
        initialize(binding)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initialize(binding: EditHomescreenContainerFragmentBinding) {
        dockItemAdapter = EditDockAdapter { dockViewModel.removeItem(it) }

        settingsViewModel.currentTheme.observe(viewLifecycleOwner, { theme ->
            val backgroundColor = theme.drawerSearchBackgroundColor
            val cardViewBackgroundColor = theme.drawerBackgroundColor
            val textFillColor = theme.drawerTextFillColor

            binding.apply {
                cardViewPageHeader.setCardBackgroundColor(cardViewBackgroundColor)
                cardViewRecyclerView.setCardBackgroundColor(cardViewBackgroundColor)
                constraintLayout.setBackgroundColor(backgroundColor)

                dockItemAdapter.setTextColor(textFillColor)
                dockItemAdapter.notifyDataSetChanged()

                addButton.setColorFilter(textFillColor)
                deleteButton.setColorFilter(textFillColor)
                okButton.setColorFilter(textFillColor)
                containerName.setTextColor(textFillColor)
                itemCountText.setTextColor(textFillColor)
            }
        })

        binding.deleteButton.visibility = View.GONE
        binding.containerName.text = "Dock"

        dockViewModel.dockItems.observe(viewLifecycleOwner, {
            val itemCountText = "${it.size} / $MAX_ITEMS_IN_DOCK items"
            binding.itemCountText.text = itemCountText

            dockItemAdapter.setItemList(it)
            dockItemAdapter.notifyDataSetChanged()
        })

        binding.itemListRecyclerView.apply {
            adapter = dockItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}