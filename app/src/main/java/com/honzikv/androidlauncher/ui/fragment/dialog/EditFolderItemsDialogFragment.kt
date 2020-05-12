package com.honzikv.androidlauncher.ui.fragment.dialog

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
import com.honzikv.androidlauncher.MAX_ITEMS_IN_FOLDER
import com.honzikv.androidlauncher.data.model.FolderWithItems
import com.honzikv.androidlauncher.databinding.EditHomescreenContainerFragmentBinding
import com.honzikv.androidlauncher.ui.fragment.dialog.adapter.EditFolderAdapter
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.ext.android.bind
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class EditFolderItemsDialogFragment private constructor() : DialogFragment() {

    companion object {
        private const val FOLDER_ID = "folderId"
        fun newInstance(folderId: Long) = EditFolderItemsDialogFragment().apply {
            arguments = Bundle().apply { putLong(FOLDER_ID, folderId) }
        }
    }

    private val homescreenViewModel: HomescreenViewModel by viewModel()

    private val settingsViewModel: SettingsViewModel by viewModel()

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
            val item1 = itemAdapter.getItem(viewHolder.adapterPosition)
            val item2 = itemAdapter.getItem(target.adapterPosition)
            homescreenViewModel.swapFolderItemsPositions(item1, item2)
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
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
        itemAdapter = EditFolderAdapter { homescreenViewModel.deleteFolderItem(it) }
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
            }

            if (folderWithItems.itemList.size >= MAX_ITEMS_IN_FOLDER) {
                binding.addButton.visibility = View.GONE
            }

            binding.addButton.setOnClickListener {
                AppPickerDialogFragment.newInstance(folderWithItems.folder.id!!)
                    .show(requireActivity().supportFragmentManager, "itemPicker")
            }
            itemAdapter.setItemList(folderWithItems.itemList.apply { sortedBy { it.position } })
            itemAdapter.notifyDataSetChanged()
        }

        binding.okButton.setOnClickListener { dismiss() }
    }
}