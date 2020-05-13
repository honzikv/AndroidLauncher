package com.honzikv.androidlauncher.ui.fragment.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.databinding.EditHomescreenContainerFragmentBinding
import com.honzikv.androidlauncher.model.PageWithFolders
import com.honzikv.androidlauncher.ui.fragment.page.adapter.EditPageAdapter
import com.honzikv.androidlauncher.util.MAX_FOLDERS_PER_PAGE
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*
import kotlin.properties.Delegates


class EditPageItemsDialogFragment private constructor() : DialogFragment() {

    private val homescreenViewModel: HomescreenViewModel by sharedViewModel()

    private val settingsViewModel: SettingsViewModel by sharedViewModel()

    private lateinit var pageWithFoldersLiveData: LiveData<PageWithFolders>

    private lateinit var folderAdapter: EditPageAdapter

    private var pageId by Delegates.notNull<Long>()

    companion object {
        private const val PAGE_ID = "pageId"
        fun newInstance(pageId: Long) = EditPageItemsDialogFragment()
            .apply {
                arguments = Bundle().apply { putLong(PAGE_ID, pageId) }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageId = requireArguments()[PAGE_ID] as Long
        setStyle(STYLE_NO_FRAME, android.R.style.ThemeOverlay_Material_Dark);
        pageWithFoldersLiveData = homescreenViewModel.getPageWithFolders(pageId)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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
                    Collections.swap(folderAdapter.getItemList(), i, i + 1)
                    val swap = folderAdapter.getItem(i).folder.position
                    folderAdapter.getItem(i).folder.position =
                        folderAdapter.getItem(i + 1).folder.position
                    folderAdapter.getItem(i + 1).folder.position = swap
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(folderAdapter.getItemList(), i, i - 1)
                    val swap = folderAdapter.getItem(i).folder.position
                    folderAdapter.getItem(i).folder.position =
                        folderAdapter.getItem(i - 1).folder.position
                    folderAdapter.getItem(i - 1).folder.position = swap
                }
            }
            folderAdapter.notifyItemMoved(fromPosition, toPosition)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            homescreenViewModel.updateFolderList(folderAdapter.getItemList().map { it.folder })
        }
    }

    private fun initialize(binding: EditHomescreenContainerFragmentBinding) {
        folderAdapter =
            EditPageAdapter(requireActivity()) {
                homescreenViewModel.deleteFolder(it)
            }

        settingsViewModel.currentTheme.observe(viewLifecycleOwner, { theme ->
            val backgroundColor = theme.drawerSearchBackgroundColor
            val cardViewBackgroundColor = theme.drawerBackgroundColor
            val textFillColor = theme.drawerTextFillColor

            binding.apply {
                cardViewPageHeader.setCardBackgroundColor(cardViewBackgroundColor)
                cardViewRecyclerView.setCardBackgroundColor(cardViewBackgroundColor)
                constraintLayout.setBackgroundColor(backgroundColor)

                folderAdapter.setTextColor(textFillColor)
                folderAdapter.notifyDataSetChanged()

                addButton.setColorFilter(textFillColor)
                deleteButton.setColorFilter(textFillColor)
                okButton.setColorFilter(textFillColor)
                containerName.setTextColor(textFillColor)
                itemCountText.setTextColor(textFillColor)
            }
        })


        binding.itemListRecyclerView.apply {
            adapter = folderAdapter
            layoutManager = LinearLayoutManager(context)
            ItemTouchHelper(itemTouchDragToReorderCallback).attachToRecyclerView(this)
        }

        binding.addButton.visibility = View.GONE
        binding.deleteButton.visibility = View.GONE
        pageWithFoldersLiveData.observe(viewLifecycleOwner, { pageWithFolders ->
            binding.addButton.visibility = View.VISIBLE
            binding.deleteButton.visibility = View.VISIBLE

            val pageName = "Page ${pageWithFolders.page.pageNumber + 1}"
            binding.containerName.text = pageName

            val itemCount = "${pageWithFolders.folderList.size} / $MAX_FOLDERS_PER_PAGE folders"
            binding.itemCountText.text = itemCount

            folderAdapter.setItemList(pageWithFolders.folderList.toMutableList().apply {
                sortBy { it.folder.position }
                reverse()
            })

            folderAdapter.notifyDataSetChanged()
        })

        binding.deleteButton.setOnClickListener {
            homescreenViewModel.deletePage(pageId)
            dismiss()
        }

        binding.addButton.setOnClickListener {
            if (pageWithFoldersLiveData.value!!.folderList.size >= MAX_FOLDERS_PER_PAGE) {
                Toast.makeText(context, "Page is full", Toast.LENGTH_LONG)
                    .show()
            } else {
                CreateFolderDialogFragment.newInstance(pageId)
                    .show(requireActivity().supportFragmentManager, "createFolder")
            }
        }

        binding.okButton.setOnClickListener { dismiss() }
    }

}
