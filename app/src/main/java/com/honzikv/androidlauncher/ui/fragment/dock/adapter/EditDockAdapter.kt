package com.honzikv.androidlauncher.ui.fragment.dock.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.databinding.EditHomescreenContainerAppItemBinding
import com.honzikv.androidlauncher.model.DockItemModel
import com.honzikv.androidlauncher.databinding.EditHomescreenContainerItemBinding

class EditDockAdapter(val delete: (Long) -> Unit) :
    RecyclerView.Adapter<EditDockAdapter.DockItemViewHolder>() {

    private var itemList: MutableList<DockItemModel> = mutableListOf()

    private var textColor = Color.BLACK

    fun setItemList(itemList: MutableList<DockItemModel>) {
        this.itemList = itemList
    }

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DockItemViewHolder(
        EditHomescreenContainerAppItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: DockItemViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun getItem(i: Int) = itemList[i]
    fun getItemList(): MutableList<DockItemModel> = itemList

    inner class DockItemViewHolder(val binding: EditHomescreenContainerAppItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DockItemModel) {
            binding.appIcon.setImageDrawable(data.icon)
            binding.textLeft.apply {
                text = data.label
                setTextColor(textColor)
            }

            binding.removeButton.apply {
                setOnClickListener {
                    delete(data.id!!)
                }
                setColorFilter(textColor)
            }

        }
    }

}