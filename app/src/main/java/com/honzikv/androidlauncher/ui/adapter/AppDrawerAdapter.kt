package com.honzikv.androidlauncher.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.data.model.DrawerAppModel
import com.honzikv.androidlauncher.databinding.AppDrawerIconWithTitleBinding

class AppDrawerAdapter(private val drawerItems: List<DrawerAppModel>) :
    RecyclerView.Adapter<AppDrawerAdapter.ItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            AppDrawerIconWithTitleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = drawerItems.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(drawerItems[position])


    inner class ItemViewHolder(private val itemBinding: AppDrawerIconWithTitleBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(drawerAppModel: DrawerAppModel) {
            itemBinding.icon.setImageDrawable(drawerAppModel.icon)
            itemBinding.label.text = drawerAppModel.label
        }
    }
}