package com.honzikv.androidlauncher.ui.fragment.homescreen.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.data.model.FolderItemModel
import com.honzikv.androidlauncher.databinding.IconWithTitleBelowBinding
import org.koin.core.KoinComponent
import org.koin.core.get

class FolderAdapter(
    private val folderItems: List<FolderItemModel>
) : RecyclerView.Adapter<FolderAdapter.ItemViewHolder>(), KoinComponent {

    private var labelColor = Color.BLACK

    fun setLabelColor(color: Int) {
        this.labelColor = color
    }

    private val onClickListener = View.OnClickListener { view ->
        val viewHolder = view?.tag as RecyclerView.ViewHolder
        val item = folderItems[viewHolder.adapterPosition]
        val context: Context = get()
        context.startActivity(context.packageManager.getLaunchIntentForPackage(item.systemAppPackageName))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            IconWithTitleBelowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = folderItems.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(folderItems[position])

    inner class ItemViewHolder(private val itemBinding: IconWithTitleBelowBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        init {
            itemView.tag = this
            itemView.setOnClickListener(onClickListener)
        }

        fun bind(folderItem: FolderItemModel) {
            itemBinding.icon.setImageDrawable(folderItem.drawable)
            itemBinding.label.text = folderItem.label
            itemBinding.label.setTextColor(labelColor)
        }
    }
}
