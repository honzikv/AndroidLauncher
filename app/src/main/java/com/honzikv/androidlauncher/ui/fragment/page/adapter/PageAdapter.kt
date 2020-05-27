package com.honzikv.androidlauncher.ui.fragment.page.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.model.PageWithFolders
import com.honzikv.androidlauncher.databinding.FolderListBinding
import com.honzikv.androidlauncher.utils.gestures.OnSwipeTouchListener

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
        val sorted = newPageList.sortedBy { it.page.pageNumber }
        this.pages = sorted
    }

    inner class PageViewHolder(val binding: FolderListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnTouchListener(onSwipeTouchListener)
        }

        @SuppressLint("ClickableViewAccessibility")
        fun bind(page: PageWithFolders) {
            val folderAdapter = FolderAdapter(context, binding.folderListRecyclerView)
            folderAdapter.setFolderList(
                page.folderList.toMutableList().apply { sortBy { it.folder.position } })
            binding.folderListRecyclerView.apply {
                adapter = folderAdapter
                setOnTouchListener(onSwipeTouchListener)
                layoutManager = LinearLayoutManager(context).apply {
                    reverseLayout = true
                }
                (adapter as FolderAdapter).notifyDataSetChanged()
            }
        }
    }


}