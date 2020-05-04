package com.honzikv.androidlauncher.ui.fragment.homescreen.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.data.model.PageWithFolders
import com.honzikv.androidlauncher.databinding.FolderListBinding
import com.honzikv.androidlauncher.ui.gestures.OnSwipeTouchListener

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
        holder as PageViewHolder
        holder.bind(pages[position])
    }

    fun setPages(pages: List<PageWithFolders>) {
        this.pages = pages
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

    private fun longPressPopupMenu(view: View) {
        PopupMenu(context, view).apply {
            setOnMenuItemClickListener { item ->
                when (item!!.itemId) {
                    R.id.launcherSettings -> TODO("Launcher settings placeholder")
                    R.id.changeWallpaper -> TODO("Change Wallpaper")
                }

                true
            }
            inflate(R.menu.homescreen_long_click_popup_menu)
            show()
        }
    }


}