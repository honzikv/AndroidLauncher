package com.honzikv.androidlauncher.ui.fragment.homescreen.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.data.model.DockItemModel
import com.honzikv.androidlauncher.databinding.IconWithTitleBelowBinding
import org.koin.core.KoinComponent
import org.koin.core.get

class DockAdapter(private var showLabels: Boolean) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), KoinComponent {

    private var labelColor = Color.BLACK

    private val onClickListener = View.OnClickListener { view ->
        val viewHolder = view?.tag as DockItemViewHolder
        val item = dockItems[viewHolder.adapterPosition]
        context.startActivity(context.packageManager.getLaunchIntentForPackage(item.packageName))
    }

    fun setLabelColor(color: Int) {
        labelColor = color
    }

    private var dockItems = listOf<DockItemModel>()

    private val context: Context = get()

    fun setDockItems(dockItems: List<DockItemModel>) {
        this.dockItems = dockItems
    }

    fun setShowLabels(show: Boolean) {
        this.showLabels = show
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DockItemViewHolder(
            IconWithTitleBelowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = dockItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as DockItemViewHolder
        holder.bind(dockItems[position])
    }

    inner class DockItemViewHolder(val binding: IconWithTitleBelowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.tag = this
            itemView.setOnClickListener(onClickListener)
        }

        fun bind(data: DockItemModel) {
            if (showLabels) {
                binding.label.visibility = View.VISIBLE
                binding.label.text = data.label
            } else {
                binding.label.visibility = View.GONE
            }
            binding.icon.setImageDrawable(data.icon)
        }
    }
}