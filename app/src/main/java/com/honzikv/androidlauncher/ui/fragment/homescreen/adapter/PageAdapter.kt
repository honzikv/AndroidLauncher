package com.honzikv.androidlauncher.ui.fragment.homescreen.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.data.model.FolderWithItems
import com.honzikv.androidlauncher.data.model.PageWithFolders
import com.honzikv.androidlauncher.databinding.FolderListBinding
import com.honzikv.androidlauncher.ui.gestures.OnSwipeTouchListener
import timber.log.Timber
import kotlin.math.min

/**
 * [context] must be of type FragmentActivity
 */
class PageAdapter(val context: Context, val onSwipeTouchListener: OnSwipeTouchListener) :
    RecyclerView.Adapter<PageAdapter.PageViewHolder>() {

    private var pages: List<PageWithFolders> = mutableListOf()

    override fun getItemCount() = pages.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        return PageViewHolder(
            FolderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    fun setPages(newPageList: List<PageWithFolders>) {
        if (pages.isNotEmpty()) {
            restorePreviousState(newPageList)
        }
        val sorted = newPageList.sortedBy { it.page.pageNumber }
        Timber.d("size = ${sorted.size}")
        this.pages = sorted
    }

    private fun restorePreviousState(newPageList: List<PageWithFolders>) {
        val maxIndex = min(pages.size, newPageList.size)
        for (i in 0 until maxIndex) {
            if (pages[i].page == newPageList[i].page) {
                restorePageState(pages[i].folderList, newPageList[i].folderList)
            }
        }
    }

    private fun restorePageState(
        oldFolderList: List<FolderWithItems>,
        newFolderList: List<FolderWithItems>
    ) {
        val maxIndex = min(oldFolderList.size, newFolderList.size)
        for (i in 0 until maxIndex) {
            if (oldFolderList[i].folder == newFolderList[i].folder) {
                Timber.d("restoring state")
                newFolderList[i].restoreState(oldFolderList[i])
            }
        }
    }

    inner class PageViewHolder(val binding: FolderListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.tag = this
        }

        @SuppressLint("ClickableViewAccessibility")
        fun bind(page: PageWithFolders) {
            val folderAdapter = FolderListAdapter(context)
            folderAdapter.setFolderList(page.folderList)
            binding.folderListRecyclerView.apply {
                adapter = folderAdapter
                setOnTouchListener(onSwipeTouchListener)
                layoutManager = LinearLayoutManager(context)
                (adapter as FolderListAdapter).notifyDataSetChanged()
            }
        }
    }


}