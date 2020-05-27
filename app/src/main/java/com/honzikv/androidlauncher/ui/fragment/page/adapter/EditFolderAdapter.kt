package com.honzikv.androidlauncher.ui.fragment.page.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.databinding.EditHomescreenAppItemBinding
import com.honzikv.androidlauncher.model.FolderItemModel

/**
 * Adapter pro recycler view s ikonami aplikaci ve slozce
 */
class EditFolderAdapter(private val delete: (Long) -> Unit) :
    RecyclerView.Adapter<EditFolderAdapter.FolderItemViewHolder>() {

    private var itemList = mutableListOf<FolderItemModel>()

    private var textColor = Color.BLACK

    fun setItemList(itemList: MutableList<FolderItemModel>) {
        this.itemList = itemList
    }

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = FolderItemViewHolder(
        EditHomescreenAppItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: FolderItemViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun getItem(adapterPosition: Int) = itemList[adapterPosition]
    fun getItemList(): MutableList<FolderItemModel> = itemList

    inner class FolderItemViewHolder(val binding: EditHomescreenAppItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: FolderItemModel) {
            binding.appIcon.setImageDrawable(data.icon)
            binding.removeButton.apply {
                setColorFilter(textColor)
                setOnClickListener { delete(data.id!!) }
            }
            binding.textLeft.apply {
                text = data.label
                setTextColor(textColor)
            }
        }
    }
}