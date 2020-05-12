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
        val sorted = newPageList.sortedBy { it.page.pageNumber }
        Timber.d("size = ${sorted.size}")
        this.pages = sorted
    }

    inner class PageViewHolder(val binding: FolderListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.tag = this
        }

        @SuppressLint("ClickableViewAccessibility")
        fun bind(page: PageWithFolders) {
            val folderAdapter = FolderListAdapter(context)
            folderAdapter.setFolderList(page.folderList.sortedBy { it.folder.position })
            binding.folderListRecyclerView.apply {
                adapter = folderAdapter
                setOnTouchListener(onSwipeTouchListener)
                layoutManager = LinearLayoutManager(context)
                (adapter as FolderListAdapter).notifyDataSetChanged()
            }
        }
    }


}