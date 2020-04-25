package com.honzikv.androidlauncher.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.data.model.entity.DockItemDto
import com.honzikv.androidlauncher.databinding.IconWithTitleBinding
import org.koin.core.KoinComponent
import org.koin.core.get

class DockAdapter : RecyclerView.Adapter<DockAdapter.ItemViewHolder>(), KoinComponent {

    private var items: List<DockItemDto> = mutableListOf()

    private var showLabels = true

    private var labelColor: Int = Color.BLACK

    private val onClickListener = View.OnClickListener { view ->
        val viewHolder = view?.tag as ItemViewHolder
        val item = items[viewHolder.adapterPosition]
        val context: Context = get()
        context.startActivity(context.packageManager.getLaunchIntentForPackage(item.packageName))
    }

    fun updateData(items: List<DockItemDto>) {
        this.items = items
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            IconWithTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ItemViewHolder(private val itemBinding: IconWithTitleBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(dockItemDto: DockItemDto) {
            itemBinding.icon.setImageDrawable(dockItemDto.drawable)
        }

        init {
            itemView.tag = this
            itemView.setOnClickListener(onClickListener)
        }
    }

}