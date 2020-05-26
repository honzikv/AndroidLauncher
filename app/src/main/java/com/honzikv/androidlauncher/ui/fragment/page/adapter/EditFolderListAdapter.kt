package com.honzikv.androidlauncher.ui.fragment.page.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.model.FolderWithItems
import com.honzikv.androidlauncher.databinding.EditHomescreenContainerItemBinding
import com.honzikv.androidlauncher.ui.fragment.page.EditFolderSettingsDialogFragment

class EditFolderListAdapter(
    val fragmentActivity: FragmentActivity,
    val delete: (Long) -> Unit
) :
    RecyclerView.Adapter<EditFolderListAdapter.FolderViewHolder>() {

    private var itemList: MutableList<FolderWithItems> = mutableListOf()

    private var textColor = Color.BLACK

    fun getItem(index: Int) = itemList[index]

    fun setItemList(itemList: MutableList<FolderWithItems>) {
        this.itemList = itemList
    }

    fun getItemList() = itemList

    fun setTextColor(labelColor: Int) {
        this.textColor = labelColor
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder =
        FolderViewHolder(
            EditHomescreenContainerItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) =
        holder.bind(itemList[position])

    inner class FolderViewHolder(val binding: EditHomescreenContainerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(folderWithItems: FolderWithItems) {
            val folder = folderWithItems.folder
            binding.textLeft.apply {
                text = folder.title
                setTextColor(textColor)
            }
            binding.appIcon.visibility = View.GONE

            binding.editButton.apply {
                setOnClickListener {
                    EditFolderSettingsDialogFragment.newInstance(folder).show(
                        fragmentActivity.supportFragmentManager,
                        "editFolderFragment"
                    )
                }
                setColorFilter(textColor)
            }

            binding.removeButton.apply {
                setOnClickListener { delete(folder.id!!) }
                setColorFilter(textColor)
            }
        }
    }

}