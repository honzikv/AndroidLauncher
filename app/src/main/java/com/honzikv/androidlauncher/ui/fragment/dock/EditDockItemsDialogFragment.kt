package com.honzikv.androidlauncher.ui.fragment.dock

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.utils.MAX_ITEMS_IN_DOCK
import com.honzikv.androidlauncher.databinding.EditHomescreenContainerFragmentBinding
import com.honzikv.androidlauncher.ui.fragment.picker.AppPickerDialogFragment
import com.honzikv.androidlauncher.ui.fragment.dock.adapter.EditDockAdapter
import com.honzikv.androidlauncher.utils.SETTINGS_BACKGROUND_ALPHA
import com.honzikv.androidlauncher.utils.applyAlpha
import com.honzikv.androidlauncher.viewmodel.DockViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*

class EditDockItemsDialogFragment private constructor() : DialogFragment() {

    private val dockViewModel: DockViewModel by sharedViewModel()

    private val settingsViewModel: SettingsViewModel by sharedViewModel()

    private lateinit var itemAdapter: EditDockAdapter

    companion object {
        fun newInstance() =
            EditDockItemsDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.ThemeOverlay_Material_Dark)
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

    private val itemTouchDragToReorderCallback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        0
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            val toPosition = target.adapterPosition

            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(itemAdapter.getItemList(), i, i + 1)
                    val swap = itemAdapter.getItem(i).position
                    itemAdapter.getItem(i).position =
                        itemAdapter.getItem(i + 1).position
                    itemAdapter.getItem(i + 1).position = swap
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(itemAdapter.getItemList(), i, i - 1)
                    val swap = itemAdapter.getItem(i).position
                    itemAdapter.getItem(i).position =
                        itemAdapter.getItem(i - 1).position
                    itemAdapter.getItem(i - 1).position = swap
                }
            }
            itemAdapter.notifyItemMoved(fromPosition, toPosition)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            dockViewModel.updateItemList(itemAdapter.getItemList())
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initialize(binding: EditHomescreenContainerFragmentBinding) {
        itemAdapter =
            EditDockAdapter {
                dockViewModel.removeItem(it)
            }

        settingsViewModel.currentTheme.observe(viewLifecycleOwner, { theme ->
            val backgroundColor = theme.drawerSearchBackgroundColor
            val cardViewBackgroundColor = theme.drawerBackgroundColor
            val textFillColor = theme.drawerTextFillColor

            binding.apply {
                cardViewPageHeader.setCardBackgroundColor(cardViewBackgroundColor)
                cardViewRecyclerView.setCardBackgroundColor(cardViewBackgroundColor)
                constraintLayout.setBackgroundColor(
                    applyAlpha(backgroundColor,
                        SETTINGS_BACKGROUND_ALPHA
                    )
                )

                itemAdapter.setTextColor(textFillColor)
                itemAdapter.notifyDataSetChanged()

                addButton.setColorFilter(textFillColor)
                deleteButton.setColorFilter(textFillColor)
                okButton.setColorFilter(textFillColor)
                containerName.setTextColor(textFillColor)
                itemCountText.setTextColor(textFillColor)
            }
        })

        binding.deleteButton.visibility = View.GONE
        binding.containerName.text = "Dock"

        dockViewModel.dockItems.observe(viewLifecycleOwner, { dockItemList ->
            val itemCountText = "${dockItemList.size} / $MAX_ITEMS_IN_DOCK items"
            binding.itemCountText.text = itemCountText

            itemAdapter.setItemList(
                dockItemList.toMutableList().apply { sortBy { it.position } })
            itemAdapter.notifyDataSetChanged()
        })

        //Observe for error when new apps are getting uploaded
        dockViewModel.getDockPostError().observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandledOrReturnNull()?.let { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        })

        binding.itemListRecyclerView.apply {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchDragToReorderCallback).attachToRecyclerView(this)
        }

        binding.okButton.setOnClickListener { dismiss() }
        binding.addButton.setOnClickListener {
            AppPickerDialogFragment.newInstance(AppPickerDialogFragment.getDockFolderId())
                .show(requireActivity().supportFragmentManager, "editDockApps")
        }
    }
}