package com.honzikv.androidlauncher.ui.fragment.page.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.databinding.IconWithTitleBelowBinding
import com.honzikv.androidlauncher.model.FolderItemModel
import org.koin.core.KoinComponent
import org.koin.core.get

/**
 * Adapter pro jednotlive predmety ve slozce ve fragmentu HomescreenFragment
 */
class FolderItemAdapter(
    /**
     * Seznam predmetu
     */
    private val items: List<FolderItemModel>
) : RecyclerView.Adapter<FolderItemAdapter.ItemViewHolder>(), KoinComponent {

    /**
     * Barva textu popisku
     */
    private var labelColor = Color.BLACK

    /**
     * Injekce kontextu pro ziskani package manageru pro spusteni aplikace
     */
    val context: Context = get()

    /**
     * Setter pro [labelColor]
     */
    fun setLabelColor(color: Int) {
        this.labelColor = color
    }

    /**
     * OnClickListener zajistujici spusteni aplikace po kliknuti na ikonu
     */
    private val onClickListener = View.OnClickListener { view ->
        val viewHolder = view?.tag as RecyclerView.ViewHolder
        val item = items[viewHolder.adapterPosition]
        context.startActivity(context.packageManager.getLaunchIntentForPackage(item.packageName))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            IconWithTitleBelowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) =
        holder.bind(items[position])

    /**
     * ViewHolder pro nastaveni vzhledu jednotlivych ikon
     */
    inner class ItemViewHolder(private val itemBinding: IconWithTitleBelowBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        //Nastaveni OnClickListeneru
        init {
            itemView.tag = this
            itemView.setOnClickListener(onClickListener)
        }

        //Binding UI a dat
        fun bind(folderItem: FolderItemModel) {
            itemBinding.icon.setImageDrawable(folderItem.icon)
            itemBinding.label.text = folderItem.label
            itemBinding.label.setTextColor(labelColor)
        }
    }
}
