package com.honzikv.androidlauncher.ui.fragment.dialog.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.data.model.DockItemModel
import com.honzikv.androidlauncher.data.model.FolderItemModel
import com.honzikv.androidlauncher.databinding.EditHomescreenContainerItemBinding

class EditFolderAdapter(private val delete: (Long) -> Unit) :
    RecyclerView.Adapter<EditFolderAdapter.FolderItemViewHolder>() {

    private var itemList = listOf<FolderItemModel>()

    private var textColor = Color.BLACK

    fun setItemList(itemList: List<FolderItemModel>) {
        this.itemList = itemList
    }

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FolderItemViewHolder(
        EditHomescreenContainerItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: FolderItemViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun getItem(adapterPosition: Int) = itemList[adapterPosition]

    inner class FolderItemViewHolder(val binding: EditHomescreenContainerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: FolderItemModel) {
            binding.textLeft.apply {
                text = data.label
                setTextColor(textColor)
            }
            binding.editButton.visibility = View.GONE
            binding.appIcon.setImageDrawable(data.icon)
            binding.removeButton.apply {
                setColorFilter(textColor)
                setOnClickListener { delete(data.id!!) }
            }
        }
    }
}