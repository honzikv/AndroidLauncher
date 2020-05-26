package com.honzikv.androidlauncher.ui.fragment.page.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.databinding.EditHomescreenContainerItemBinding
import com.honzikv.androidlauncher.databinding.EditHomescreenPageItemBinding
import com.honzikv.androidlauncher.model.PageModel
import com.honzikv.androidlauncher.model.PageWithFolders
import com.honzikv.androidlauncher.ui.fragment.page.EditPageItemsDialogFragment

class EditPageListAdapter(
    val fragmentActivity: FragmentActivity,
    val delete: (Long) -> Unit
) : RecyclerView.Adapter<EditPageListAdapter.PageViewHolder>() {

    private var itemList: MutableList<PageWithFolders> = mutableListOf()

    private var textColor = Color.BLACK

    fun getItem(index: Int) = itemList[index]

    fun setItemList(itemList: MutableList<PageWithFolders>) {
        this.itemList = itemList
    }

    fun getItemList() = itemList

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditPageListAdapter.PageViewHolder = PageViewHolder(
        EditHomescreenPageItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: EditPageListAdapter.PageViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class PageViewHolder(val binding: EditHomescreenPageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(pageWithFolders: PageWithFolders) {
            binding.textLeft.apply {
                text = if (pageWithFolders.folderList.isEmpty()) {
                    "Page has no folders"
                } else {
                    pageWithFolders.folderList
                        .toMutableList()
                        .sortedBy { it.folder.position }
                        .reversed()
                        .joinToString(separator = ", ") { it.folder.title }
                }
                setTextColor(textColor)
            }

            binding.editButton.apply {
                setOnClickListener {
                    EditPageItemsDialogFragment.newInstance(pageWithFolders.page.id!!).show(
                        fragmentActivity.supportFragmentManager,
                        "editPageFoldersFragment"
                    )
                }
                setColorFilter(textColor)
            }

            binding.removeButton.apply {
                setOnClickListener { delete(pageWithFolders.page.id!!) }
                setColorFilter(textColor)
            }
        }
    }
}