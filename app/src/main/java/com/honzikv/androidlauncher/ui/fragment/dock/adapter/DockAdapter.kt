package com.honzikv.androidlauncher.ui.fragment.dock.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.databinding.IconWithTitleBelowBinding
import com.honzikv.androidlauncher.model.DockItemModel
import org.koin.core.KoinComponent
import org.koin.core.get

/**
 * Adapter pro recycler view v DockFragment tride. Trida navic dedi od KoinKomponent tridy
 */
class DockAdapter(
    /**
     * Zobrazeni popisku v doku
     */
    private var showLabels: Boolean
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), KoinComponent {

    /**
     * Barva popisku ikon
     */
    private var labelColor = Color.BLACK
    fun setLabelColor(color: Int) {
        labelColor = color
    }

    /**
     * Aplikacni kontext pro spusteni jednotlivych aplikaci v onClickListeneru
     */
    private val context: Context = get()

    /**
     * OnClickListener zajistujici spusteni aplikaci
     */
    private val onClickListener = View.OnClickListener { view ->
        val viewHolder = view?.tag as DockItemViewHolder
        val item = dockItems[viewHolder.adapterPosition]

        //Pro prazdny predmet, ktery je pritomen aby se dok zobrazil
        if (item.packageName == "") {
            return@OnClickListener
        }
        context.startActivity(context.packageManager.getLaunchIntentForPackage(item.packageName))
    }

    private var dockItems = listOf<DockItemModel>()

    /**
     * Nastavi predmety v doku, melo by byt serazeno podle pozice pro spravnou funkcionalitu
     */
    fun setDockItems(dockItems: List<DockItemModel>) {
        this.dockItems = dockItems
    }

    fun setShowLabels(show: Boolean) {
        this.showLabels = show
    }

    fun getItem(position: Int) = dockItems[position]

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

    /**
     * ViewHolder pro ikonu v doku
     */
    inner class DockItemViewHolder(val binding: IconWithTitleBelowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Nastaveni onClickListeneru a onTouchListeneru pri vytvoreni objektu
         */
        init {
            itemView.tag = this
            itemView.setOnClickListener(onClickListener)
        }

        /**
         * Binding dat na view
         */
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