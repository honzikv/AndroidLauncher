package com.honzikv.androidlauncher.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.data.model.entity.FolderItemModel
import com.honzikv.androidlauncher.databinding.IconWithTitleBinding

class FolderAdapter(
    private val folderItems: List<FolderItemModel>
) : RecyclerView.Adapter<FolderAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            IconWithTitleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = folderItems.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(folderItems[position])


    inner class ItemViewHolder(private val itemBinding: IconWithTitleBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(folderItem: FolderItemModel) {
            itemBinding.icon.setImageDrawable(folderItem.drawable)
            itemBinding.label.text = folderItem.label
        }
    }
}
