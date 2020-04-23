package com.honzikv.androidlauncher.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.data.model.DrawerApp
import com.honzikv.androidlauncher.databinding.AppDrawerIconWithTitleBinding
import org.koin.core.KoinComponent
import org.koin.core.get
import java.util.*


class AppDrawerAdapter() :
    RecyclerView.Adapter<AppDrawerAdapter.ItemViewHolder>(), KoinComponent, Filterable {

    private var drawerItemsAll: List<DrawerApp> = mutableListOf()

    private var drawerItemsDisplay: MutableList<DrawerApp> = mutableListOf()

    private val searchFilter = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<DrawerApp>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(drawerItemsAll)
            } else {
                val searchConstraint = constraint.toString().toLowerCase(Locale.ROOT)
                filteredList.addAll(drawerItemsAll.filter {
                    it.label.toLowerCase(Locale.ROOT) == searchConstraint
                }
                    .toList())
            }

            return FilterResults().apply { values = filteredList }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            drawerItemsDisplay.clear()
            drawerItemsDisplay.addAll(results?.values as Collection<DrawerApp>)
            notifyDataSetChanged()
        }
    }

    private val onClickListener = View.OnClickListener { view ->
        val viewHolder = view?.tag as ItemViewHolder
        val item = drawerItemsAll[viewHolder.adapterPosition]
        val context: Context = get()
        context.startActivity(context.packageManager.getLaunchIntentForPackage(item.packageName))
    }

    fun updateData(drawerItems: List<DrawerApp>) {
        this.drawerItemsAll = drawerItems
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

    override fun getItemCount() = drawerItemsAll.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(drawerItemsAll[position])

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

    override fun getFilter(): Filter {
        return searchFilter
    }


}
