package com.honzikv.androidlauncher.ui.fragment.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.data.model.PageWithFolders
import com.honzikv.androidlauncher.databinding.EditHomescreenContainerFragmentBinding
import com.honzikv.androidlauncher.ui.constants.MAX_FOLDERS_PER_PAGE
import com.honzikv.androidlauncher.ui.fragment.dialog.adapter.EditPageAdapter

import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.dock_fragment.*
import org.koin.android.ext.android.inject
import java.util.*

class EditPageDialogFragment : DialogFragment() {

    private val homescreenViewModel: HomescreenViewModel by inject()

    private val settingsViewModel: SettingsViewModel by inject()

    private lateinit var page: LiveData<PageWithFolders>

    private lateinit var folderAdapter: EditPageAdapter

    companion object {
        private const val PAGE_ID = "pageId"
        fun newInstance(pageId: Long) = EditPageDialogFragment().apply {
            arguments = Bundle().apply { putLong(PAGE_ID, pageId) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pageId = requireArguments()[PAGE_ID] as Long
        setStyle(STYLE_NO_FRAME, android.R.style.ThemeOverlay_Material_Dark);
        page = homescreenViewModel.getPageWithFolders(pageId)
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
        ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.START or ItemTouchHelper.END, 0
    ) {

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            val folder1 = folderAdapter.getItem(viewHolder.adapterPosition).folder
            val folder2 = folderAdapter.getItem(target.adapterPosition).folder
            val swap = folder1.position
            folder1.position = folder2.position
            folder2.position = swap
            homescreenViewModel.updateFolders(folder1, folder2)

            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }
    }

    private fun initialize(binding: EditHomescreenContainerFragmentBinding) {
        folderAdapter = EditPageAdapter(requireActivity()) {
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

        val itemTouchHelper = ItemTouchHelper(itemTouchDragToReorderCallback)
        itemTouchHelper.attachToRecyclerView(binding.itemListRecyclerView)
        binding.itemListRecyclerView.adapter = folderAdapter
        binding.itemListRecyclerView.layoutManager = LinearLayoutManager(context)

        page.observe(viewLifecycleOwner, {
            binding.addButton.visibility = View.VISIBLE
            binding.deleteButton.visibility = View.VISIBLE

            val pageName = "Page ${it.page.pageNumber + 1}"
            binding.containerName.text = pageName

            val itemCount = "${it.folderList.size} / $MAX_FOLDERS_PER_PAGE folders"
            binding.itemCountText.text = itemCount

            folderAdapter.setItemList(it.folderList)
            folderAdapter.notifyDataSetChanged()
        })

        binding.deleteButton.setOnClickListener {
            homescreenViewModel.deletePageWithId(requireArguments()[PAGE_ID] as Long)
            dismiss()
        }

        binding.addButton.setOnClickListener {
            if (page.value!!.folderList.size >= MAX_FOLDERS_PER_PAGE) {
                Toast.makeText(context, "Page is full", Toast.LENGTH_LONG)
                    .show()
            } else {
                val createFolderDialogFragment =
                    CreateFolderDialogFragment.newInstance(page.value!!.page)
                createFolderDialogFragment.show(
                    requireActivity().supportFragmentManager,
                    "createFolder"
                )
            }
        }

        binding.okButton.setOnClickListener { dismiss() }
    }

}
