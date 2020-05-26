package com.honzikv.androidlauncher.ui.fragment.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.utils.MAX_ITEMS_IN_FOLDER
import com.honzikv.androidlauncher.model.FolderWithItems
import com.honzikv.androidlauncher.databinding.EditHomescreenContainerFragmentBinding
import com.honzikv.androidlauncher.ui.fragment.picker.AppPickerDialogFragment
import com.honzikv.androidlauncher.ui.fragment.page.adapter.EditFolderAdapter
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*

class EditFolderItemsDialogFragment private constructor() : DialogFragment() {

    companion object {
        private const val FOLDER_ID = "folderId"
        fun newInstance(folderId: Long) = EditFolderItemsDialogFragment()
            .apply {
                arguments = Bundle().apply { putLong(FOLDER_ID, folderId) }
            }
    }

    private val homescreenViewModel: HomescreenViewModel by sharedViewModel()

    private val settingsViewModel: SettingsViewModel by sharedViewModel()

    private lateinit var folder: LiveData<FolderWithItems>

    private lateinit var itemAdapter: EditFolderAdapter

    private val itemTouchDragToReorderCallBack = object : ItemTouchHelper.SimpleCallback(

        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
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
            homescreenViewModel.updateFolderItemList(itemAdapter.getItemList())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val folderId = requireArguments()[FOLDER_ID] as Long
        setStyle(STYLE_NO_FRAME, android.R.style.ThemeOverlay_Material_Dark);
        folder = homescreenViewModel.getFolderWithItemsLiveData(folderId)
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

    private fun initialize(binding: EditHomescreenContainerFragmentBinding) {
        itemAdapter =
            EditFolderAdapter {
                homescreenViewModel.deleteFolderItem(it)
            }
        binding.itemListRecyclerView.apply {
            adapter = itemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchDragToReorderCallBack).attachToRecyclerView(this)
        }

        //Style page
        settingsViewModel.currentTheme.observe(viewLifecycleOwner, { theme ->
            val backgroundColor = theme.drawerSearchBackgroundColor
            val cardViewBackgroundColor = theme.drawerBackgroundColor
            val textFillColor = theme.drawerTextFillColor

            binding.apply {
                cardViewPageHeader.setCardBackgroundColor(cardViewBackgroundColor)
                cardViewRecyclerView.setCardBackgroundColor(cardViewBackgroundColor)
                constraintLayout.setBackgroundColor(backgroundColor)

                itemAdapter.setTextColor(textFillColor)
                itemAdapter.notifyDataSetChanged()

                addButton.setColorFilter(textFillColor)
                deleteButton.setColorFilter(textFillColor)
                okButton.setColorFilter(textFillColor)
                containerName.setTextColor(textFillColor)
                itemCountText.setTextColor(textFillColor)
            }
        })

        folder.observe(viewLifecycleOwner) { folderWithItems ->
            binding.containerName.text = folderWithItems.folder.title
            val itemCount = "${folderWithItems.itemList.size} / $MAX_ITEMS_IN_FOLDER items"
            binding.itemCountText.text = itemCount

            binding.deleteButton.setOnClickListener {
                homescreenViewModel.deleteFolderWithId(folderWithItems.folder.id!!)
                dismiss()
            }

            if (folderWithItems.itemList.size >= MAX_ITEMS_IN_FOLDER) {
                binding.addButton.visibility = View.GONE
            }

            binding.addButton.setOnClickListener {
                AppPickerDialogFragment.newInstance(folderWithItems.folder.id!!)
                    .show(requireActivity().supportFragmentManager, "itemPicker")
            }
            itemAdapter.setItemList(
                folderWithItems.itemList.toMutableList().apply { sortBy { it.position } })
            itemAdapter.notifyDataSetChanged()
        }

        binding.okButton.setOnClickListener { dismiss() }
    }
}