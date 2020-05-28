package com.honzikv.androidlauncher.ui.fragment.dock.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.databinding.EditHomescreenAppItemBinding
import com.honzikv.androidlauncher.model.DockItemModel

/**
 * Adapter pro predmety, ktere se zobrazi v recycler view pri uprave doku
 */
class EditDockAdapter(
    /**
     * Funkce pro smazani prvku z databaze, parametr je id v databazi
     */
    val delete: (Long) -> Unit
) :
    RecyclerView.Adapter<EditDockAdapter.DockItemViewHolder>() {

    /**
     * Seznam s danymi predmety v doku
     */
    private var itemList: MutableList<DockItemModel> = mutableListOf()

    /**
     * Barva popisku
     */
    private var textColor = Color.BLACK

    /**
     * Setter pro [itemList]
     */
    fun setItemList(itemList: MutableList<DockItemModel>) {
        this.itemList = itemList
    }

    /**
     * Setter pro [textColor]
     */
    fun setTextColor(textColor: Int) {
        this.textColor = textColor
    }

    fun getItem(i: Int) = itemList[i]

    fun getItemList(): MutableList<DockItemModel> = itemList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DockItemViewHolder(
        EditHomescreenAppItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: DockItemViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    inner class DockItemViewHolder(
        /**
         * Binding layoutu ikony v doku
         */
        val binding: EditHomescreenAppItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Nastaveni modelovych dat na View
         */
        fun bind(data: DockItemModel) {
            binding.appIcon.setImageDrawable(data.icon)
            binding.textLeft.apply {
                text = data.label
                setTextColor(textColor)
            }

            binding.removeButton.apply {
                setOnClickListener {
                    delete(data.id!!)
                }
                setColorFilter(textColor)
            }

        }
    }

}