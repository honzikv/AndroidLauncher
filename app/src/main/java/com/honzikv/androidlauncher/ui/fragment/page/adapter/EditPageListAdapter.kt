package com.honzikv.androidlauncher.ui.fragment.page.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.databinding.EditHomescreenPageItemBinding
import com.honzikv.androidlauncher.model.PageWithFolders
import com.honzikv.androidlauncher.ui.fragment.page.EditPageItemsDialogFragment

/**
 * Adapter pro recycler view v dialogovem okne EditPageListDialogFragment
 */
class EditPageListAdapter(
    val fragmentActivity: FragmentActivity,
    /**
     * Funkce pro smazani stranky
     */
    val delete: (Long) -> Unit
) : RecyclerView.Adapter<EditPageListAdapter.PageViewHolder>() {

    /**
     * Seznam vsech stranek
     */
    private var itemList: MutableList<PageWithFolders> = mutableListOf()

    /**
     * Barva textu popisku
     */
    private var textColor = Color.BLACK

    fun getItem(index: Int) = itemList[index]

    /**
     * Setter pro [itemList]
     */
    fun setItemList(itemList: MutableList<PageWithFolders>) {
        this.itemList = itemList
    }

    fun getItemList() = itemList

    /**
     * Setter pro [textColor]
     */
    fun setTextColor(textColor: Int) {
        this.textColor = textColor
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditPageListAdapter.PageViewHolder = PageViewHolder(
        EditHomescreenPageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun getItemCount() = itemList.size

    override fun onBindViewHolder(holder: EditPageListAdapter.PageViewHolder, position: Int) =
        holder.bind(itemList[position])

    /**
     * ViewHolder pro radek s informacemi o strance
     */
    inner class PageViewHolder(val binding: EditHomescreenPageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //Binding dat a UI
        @SuppressLint("SetTextI18n")
        fun bind(pageWithFolders: PageWithFolders) {
            binding.textLeft.apply {
                //Nastaveni popisku - stranky nemaji "nazev", takze je nejlepsi napsat jejich slozky
                text = if (pageWithFolders.folderList.isEmpty()) {
                    "Page has no folders"
                } else {
                    "Folders: " + pageWithFolders.folderList
                        .toMutableList()
                        .sortedBy { it.folder.position }
                        .reversed()
                        .joinToString(separator = ", ") { it.folder.title }
                }
                setTextColor(textColor)
            }

            binding.editButton.apply {
                setOnClickListener {
                    //Spusteni dialogu pro upravu slozek stranky
                    EditPageItemsDialogFragment.newInstance(pageWithFolders.page.id!!).show(
                        fragmentActivity.supportFragmentManager, "editPageFoldersFragment"
                    )
                }
                setColorFilter(textColor)
            }

            binding.removeButton.apply {
                setOnClickListener { delete(pageWithFolders.page.id!!) }
                setColorFilter(textColor)
            }
        }
    }
}