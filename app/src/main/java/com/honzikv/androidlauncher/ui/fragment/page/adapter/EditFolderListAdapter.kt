package com.honzikv.androidlauncher.ui.fragment.page.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.model.FolderWithItems
import com.honzikv.androidlauncher.databinding.EditHomescreenContainerItemBinding
import com.honzikv.androidlauncher.ui.fragment.page.FolderSettingsDialogFragment

/**
 * Adapter pro zobrazeni jednotlivych slozek v recycler view ve fragmentu EditPageItemsDialogFragment
 */
class EditFolderListAdapter(
    val fragmentActivity: FragmentActivity,
    /**
     * Funkce pro smazani slozky
     */
    val delete: (Long) -> Unit
) :
    RecyclerView.Adapter<EditFolderListAdapter.FolderViewHolder>() {

    /**
     * Seznam vsech slozek na strance
     */
    private var itemList: MutableList<FolderWithItems> = mutableListOf()

    /**
     * Barva popisku
     */
    private var textColor = Color.BLACK

    fun getItem(index: Int) = itemList[index]

    /**
     * Setter pro [itemList]
     */
    fun setItemList(itemList: MutableList<FolderWithItems>) {
        this.itemList = itemList
    }

    fun getItemList() = itemList

    /**
     * Setter pro [labelColor]
     */
    fun setTextColor(labelColor: Int) {
        this.textColor = labelColor
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder =
        FolderViewHolder(
            EditHomescreenContainerItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) =
        holder.bind(itemList[position])

    /**
     * ViewHolder pro radek se slozkou
     */
    inner class FolderViewHolder(val binding: EditHomescreenContainerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //Binding UI a dat
        fun bind(folderWithItems: FolderWithItems) {
            val folder = folderWithItems.folder
            binding.textLeft.apply {
                text = folder.title
                setTextColor(textColor)
            }

            binding.editButton.apply {
                setOnClickListener {
                    //Spusteni dialogu pro nastaveni slozky
                    FolderSettingsDialogFragment.newInstance(folder).show(
                        fragmentActivity.supportFragmentManager, "editFolderFragment"
                    )
                }
                setColorFilter(textColor)
            }

            binding.removeButton.apply {
                setOnClickListener { delete(folder.id!!) }
                setColorFilter(textColor)
            }
        }
    }

}