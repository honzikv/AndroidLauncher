package com.honzikv.androidlauncher.ui.adapter

import android.content.Context
import android.content.pm.PackageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.data.model.DrawerApp
import com.honzikv.androidlauncher.databinding.AppDrawerIconWithTitleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinApplication
import org.koin.core.KoinComponent
import org.koin.core.get

class AppDrawerAdapter() :
    RecyclerView.Adapter<AppDrawerAdapter.ItemViewHolder>(), KoinComponent {

    private var drawerItems: List<DrawerApp> = mutableListOf()

    private val onClickListener = View.OnClickListener { view ->
        val viewHolder = view?.tag as ItemViewHolder
        val item = drawerItems[viewHolder.adapterPosition]
        val context: Context = get()
        context.startActivity(context.packageManager.getLaunchIntentForPackage(item.packageName))
    }

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

        init {
            itemView.tag = this
            itemView.setOnClickListener(onClickListener)
        }

        fun bind(drawerApp: DrawerApp) {
            itemBinding.icon.setImageDrawable(drawerApp.icon)
            itemBinding.label.text = drawerApp.label
        }
    }


}
