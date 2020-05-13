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
import com.honzikv.androidlauncher.model.PageWithFolders
import com.honzikv.androidlauncher.databinding.EditHomescreenContainerFragmentBinding
import com.honzikv.androidlauncher.util.MAX_FOLDERS_PER_PAGE
import com.honzikv.androidlauncher.ui.fragment.page.adapter.EditPageAdapter

import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class EditPageItemsDialogFragment private constructor(): DialogFragment() {

    private val homescreenViewModel: HomescreenViewModel by viewModel()

    private val settingsViewModel: SettingsViewModel by viewModel()

    private lateinit var pageWithFolders: LiveData<PageWithFolders>

    private lateinit var folderAdapter: EditPageAdapter

    companion object {
        private const val PAGE_ID = "pageId"
        fun newInstance(pageId: Long) = EditPageItemsDialogFragment()
            .apply {
            arguments = Bundle().apply { putLong(PAGE_ID, pageId) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pageId = requireArguments()[PAGE_ID] as Long
        setStyle(STYLE_NO_FRAME, android.R.style.ThemeOverlay_Material_Dark);
        pageWithFolders = homescreenViewModel.getPageWithFolders(pageId)
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
        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END,
        0
    ) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val folder1 = folderAdapter.getItem(viewHolder.adapterPosition).folder
            val folder2 = folderAdapter.getItem(target.adapterPosition).folder
            homescreenViewModel.swapFolderPositions(folder1, folder2)
            Timber.d("folder position was updated")
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }
    }

    private fun initialize(binding: EditHomescreenContainerFragmentBinding) {
        folderAdapter =
            EditPageAdapter(
                requireActivity()
            ) {
                homescreenViewModel.deleteFolderWithId(it)
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

        binding.addButton.visibility = View.GONE
        binding.deleteButton.visibility = View.GONE

        ItemTouchHelper(itemTouchDragToReorderCallback).attachToRecyclerView(binding.itemListRecyclerView)
        binding.itemListRecyclerView.adapter = folderAdapter
        binding.itemListRecyclerView.layoutManager = LinearLayoutManager(context)

        pageWithFolders.observe(viewLifecycleOwner, { pageWithFolders ->
            binding.addButton.visibility = View.VISIBLE
            binding.deleteButton.visibility = View.VISIBLE

            val pageName = "Page ${pageWithFolders.page.pageNumber + 1}"
            binding.containerName.text = pageName

            val itemCount = "${pageWithFolders.folderList.size} / $MAX_FOLDERS_PER_PAGE folders"
            binding.itemCountText.text = itemCount

            folderAdapter.setItemList(pageWithFolders.folderList.sortedBy { it.folder.position })
            folderAdapter.notifyDataSetChanged()
        })

        binding.deleteButton.setOnClickListener {
            homescreenViewModel.deletePageWithId(requireArguments()[PAGE_ID] as Long)
            dismiss()
        }

        binding.addButton.setOnClickListener {
            if (pageWithFolders.value!!.folderList.size >= MAX_FOLDERS_PER_PAGE) {
                Toast.makeText(context, "Page is full", Toast.LENGTH_LONG)
                    .show()
            } else {
                CreateFolderDialogFragment.newInstance(
                    pageWithFolders.value!!.page
                )
                    .show(requireActivity().supportFragmentManager, "createFolder")
            }
        }

        binding.okButton.setOnClickListener { dismiss() }
    }

}
