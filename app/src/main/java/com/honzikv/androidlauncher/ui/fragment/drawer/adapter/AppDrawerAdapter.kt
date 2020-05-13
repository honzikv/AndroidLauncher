package com.honzikv.androidlauncher.ui.fragment.drawer.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.model.DrawerApp
import com.honzikv.androidlauncher.databinding.IconWithTitleBelowBinding
import com.honzikv.androidlauncher.databinding.IconWithTitleRightBinding
import org.koin.core.KoinComponent
import org.koin.core.get
import java.util.*


class AppDrawerAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), KoinComponent, Filterable {

    var useGrid = false

    private var drawerItemsAll: List<DrawerApp> = mutableListOf()

    private var drawerItemsFiltered: MutableList<DrawerApp> = mutableListOf()

    private var labelColor: Int = Color.BLACK

    private val searchFilter = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList = mutableListOf<DrawerApp>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(drawerItemsAll)
            } else {
                val searchConstraint = constraint.toString().toLowerCase(Locale.ROOT)
                drawerItemsAll.filterTo(filteredList) {
                    it.label.toLowerCase(Locale.ROOT).contains(searchConstraint)
                }
                drawerItemsFiltered = filteredList
            }

            return FilterResults().apply { values = filteredList }
        }

        @Suppress("UNCHECKED_CAST")
        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            drawerItemsFiltered = results?.values as MutableList<DrawerApp>
            notifyDataSetChanged()
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (useGrid) {
            R.layout.icon_with_title_below
        } else {
            R.layout.icon_with_title_right
        }
    }

    private val onClickListener = View.OnClickListener { view ->
        val viewHolder = view?.tag as RecyclerView.ViewHolder
        val item = drawerItemsFiltered[viewHolder.adapterPosition]
        val context: Context = get()
        context.startActivity(context.packageManager.getLaunchIntentForPackage(item.packageName))
    }

    private val onLongClickListener = View.OnLongClickListener { view ->
        val viewHolder = view?.tag as RecyclerView.ViewHolder
        val item = drawerItemsFiltered[viewHolder.adapterPosition]
        val context: Context = get()
        return@OnLongClickListener true
    }

    fun updateData(drawerItems: List<DrawerApp>) {
        this.drawerItemsAll = drawerItems
        this.drawerItemsFiltered.apply {
            clear()
            addAll(drawerItems)
        }
    }

    fun setLabelColor(color: Int) {
        this.labelColor = color
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.icon_with_title_right -> IconWithTitleRightViewHolder(
                IconWithTitleRightBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> IconWithTitleBelowViewHolder(
                IconWithTitleBelowBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
        }
    }

    override fun getItemCount() = drawerItemsFiltered.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is IconWithTitleBelowViewHolder) {
            holder.bind(drawerItemsFiltered[position])
        } else if (holder is IconWithTitleRightViewHolder) {

            holder.bind(drawerItemsFiltered[position])
        }
    }

    override fun getFilter(): Filter {
        return searchFilter
    }

    inner class IconWithTitleRightViewHolder(private val itemBinding: IconWithTitleRightBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemView.tag = this
            itemView.setOnClickListener(onClickListener)
        }

        fun bind(drawerApp: DrawerApp) {
            itemBinding.icon.setImageDrawable(drawerApp.icon)
            itemBinding.label.text = drawerApp.label
            itemBinding.label.setTextColor(labelColor)
        }
    }

    inner class IconWithTitleBelowViewHolder(private val itemBinding: IconWithTitleBelowBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        init {
            itemView.tag = this
            itemView.setOnClickListener(onClickListener)
        }

        fun bind(drawerApp: DrawerApp) {
            itemBinding.icon.setImageDrawable(drawerApp.icon)
            itemBinding.label.text = drawerApp.label
            itemBinding.label.setTextColor(labelColor)
        }
    }
}
