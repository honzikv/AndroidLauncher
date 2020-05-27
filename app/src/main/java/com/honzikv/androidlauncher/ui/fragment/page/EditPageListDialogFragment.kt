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
import com.honzikv.androidlauncher.ui.fragment.page.adapter.EditPageListAdapter
import com.honzikv.androidlauncher.utils.MAX_PAGES
import com.honzikv.androidlauncher.utils.SETTINGS_BACKGROUND_ALPHA
import com.honzikv.androidlauncher.utils.applyAlpha
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import com.honzikv.androidlauncher.viewmodel.SettingsViewModel
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*

class EditPageListDialogFragment private constructor() : DialogFragment() {

    companion object {
        fun newInstance() = EditPageListDialogFragment()
    }

    private val homescreenViewModel: HomescreenViewModel by sharedViewModel()

    private val settingsViewModel: SettingsViewModel by sharedViewModel()

    private lateinit var pagesWithFolders: LiveData<List<PageWithFolders>>

    private lateinit var pageAdapter: EditPageListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, android.R.style.ThemeOverlay_Material_Dark)
        pagesWithFolders = homescreenViewModel.allPagesWithFolders
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
                    Collections.swap(pageAdapter.getItemList(), i, i + 1)
                    val swap = pageAdapter.getItem(i).page.pageNumber
                    pageAdapter.getItem(i).page.pageNumber =
                        pageAdapter.getItem(i + 1).page.pageNumber
                    pageAdapter.getItem(i + 1).page.pageNumber = swap
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(pageAdapter.getItemList(), i, i - 1)
                    val swap = pageAdapter.getItem(i).page.pageNumber
                    pageAdapter.getItem(i).page.pageNumber =
                        pageAdapter.getItem(i - 1).page.pageNumber
                    pageAdapter.getItem(i - 1).page.pageNumber = swap
                }
            }
            pageAdapter.notifyItemMoved(fromPosition, toPosition)

            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            homescreenViewModel.updatePageList(pageAdapter.getItemList().map { it.page })
        }
    }

    private fun initialize(binding: EditHomescreenContainerFragmentBinding) {
        pageAdapter = EditPageListAdapter(requireActivity()) {
            homescreenViewModel.deletePage(it)
        }

        settingsViewModel.currentTheme.observe(viewLifecycleOwner) { theme ->
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

                pageAdapter.setTextColor(textFillColor)
                pageAdapter.notifyDataSetChanged()

                addButton.setColorFilter(textFillColor)
                deleteButton.setColorFilter(textFillColor)
                okButton.setColorFilter(textFillColor)
                containerName.setTextColor(textFillColor)
                itemCountText.setTextColor(textFillColor)
            }
        }

        binding.deleteButton.visibility = View.GONE

        binding.containerName.text = "Edit Pages"

        binding.addButton.visibility = View.GONE
        pagesWithFolders.observe(viewLifecycleOwner) { pages ->
            binding.addButton.visibility = View.VISIBLE
            pageAdapter.setItemList(pages.toMutableList().apply {
                sortBy { it.page.pageNumber }
            })
            pageAdapter.notifyDataSetChanged()
            binding.itemListRecyclerView.scheduleLayoutAnimation()

            val itemCount = "${pages.size} / $MAX_PAGES pages"
            binding.itemCountText.text = itemCount
        }

        binding.itemListRecyclerView.apply {
            adapter = pageAdapter
            layoutManager = LinearLayoutManager(context)
            ItemTouchHelper(itemTouchDragToReorderCallback).attachToRecyclerView(this)
        }

        binding.okButton.setOnClickListener { dismiss() }

        binding.addButton.setOnClickListener {
            if (pagesWithFolders.value!!.size >= MAX_PAGES) {
                Toast.makeText(context, "Page limit exceeded", Toast.LENGTH_LONG)
                    .show()
            } else {
                CreatePageDialogFragment.newInstance()
                    .show(requireActivity().supportFragmentManager, "createPage")
            }
        }

        homescreenViewModel.getPageDeleteError().observe(viewLifecycleOwner, { event ->
            event.getContentIfNotHandledOrReturnNull()?.let { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        })
    }
}