package com.honzikv.androidlauncher.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.data.model.DrawerApp
import com.honzikv.androidlauncher.databinding.AppDrawerIconWithTitleBinding

class AppDrawerAdapter(private var drawerItems: List<DrawerApp>) :
    RecyclerView.Adapter<AppDrawerAdapter.ItemViewHolder>() {

    fun updateData(drawerItems: List<DrawerApp>) {
        this.drawerItems = drawerItems
        notifyDataSetChanged()
    }

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

        fun bind(drawerApp: DrawerApp) {
            itemBinding.icon.setImageDrawable(drawerApp.icon)
            itemBinding.label.text = drawerApp.label
        }
    }
}