package com.honzikv.androidlauncher.ui.fragment.page.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.databinding.FolderDetailBinding
import com.honzikv.androidlauncher.databinding.FolderHeaderBinding
import com.honzikv.androidlauncher.model.FolderItemModel
import com.honzikv.androidlauncher.model.FolderWithItems
import com.honzikv.androidlauncher.ui.fragment.page.FolderSettingsDialogFragment
import com.honzikv.androidlauncher.utils.COLUMNS_IN_FOLDER

/**
 * Adapter pro zobrazeni slozek na strance
 */
class FolderAdapter(context: Context, val recyclerView: RecyclerView) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * Seznam vsech slozek na strance
     */
    private var folderList: List<FolderWithItems> = mutableListOf()

    /**
     * Setter pro [folderList]
     */
    fun setFolderList(folderList: List<FolderWithItems>) {
        this.folderList = folderList
    }

    /**
     * OnClickListener pro zobrazeni detailu po kliknuti
     */
    private val onClickListener = View.OnClickListener { view ->
        val viewHolder = view?.tag as RecyclerView.ViewHolder
        val item = folderList[viewHolder.adapterPosition]
        item.showItems = !item.showItems
        notifyDataSetChanged()
    }

    /**
     * OnLongClickListener pro spusteni dialogu s upravou slozky po podrzeni
     */
    private val onLongClickListener = View.OnLongClickListener { view ->
        val viewHolder = view?.tag as RecyclerView.ViewHolder
        val item = folderList[viewHolder.adapterPosition]
        val fragmentActivity = context as FragmentActivity

        FolderSettingsDialogFragment.newInstance(item.folder).apply {
            show(fragmentActivity.supportFragmentManager, "folder_edit_settings")
        }
        return@OnLongClickListener true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.folder_header -> FolderHeaderViewHolder(
                FolderHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> FolderDetailViewHolder(
                FolderDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun getItemCount(): Int = folderList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = folderList[position]
        when (getItemViewType(position)) {
            R.layout.folder_detail -> (holder as FolderDetailViewHolder).bind(item)
            R.layout.folder_header -> (holder as FolderHeaderViewHolder).bind(item)
        }
    }

    /**
     * ItemViewType je dvojiho typu - podle layoutu. Protoze id layoutu jsou unikatni lze je pouzit
     * rovnou jako view type
     */
    override fun getItemViewType(position: Int) =
        if (folderList[position].showItems) {
            R.layout.folder_detail
        } else {
            R.layout.folder_header
        }

    /**
     * ViewHolder pro zobrazeni detailu - tzn. i se seznamem aplikaci
     */
    inner class FolderDetailViewHolder(private val binding: FolderDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //Nastaveni listeneru
        init {
            itemView.tag = this
            itemView.setOnClickListener(onClickListener)
            itemView.setOnLongClickListener(onLongClickListener)
        }

        //Binding dat a UI
        fun bind(data: FolderWithItems) {
            binding.recyclerView.apply {
                layoutManager = GridLayoutManager(context, COLUMNS_IN_FOLDER)
                //Serazeni aplikaci podle pozice a predani je adapteru
                adapter =
                    FolderItemAdapter(
                        data.itemList.toMutableList().apply { sortBy { it.position } })
                (adapter as FolderItemAdapter).setLabelColor(data.folder.itemColor)
            }
            binding.folderName.apply {
                setTextColor(data.folder.itemColor)
                text = data.folder.title
            }

            binding.minimizeIcon.setColorFilter(data.folder.itemColor)
            binding.folderCardView.setCardBackgroundColor(data.folder.backgroundColor)
        }
    }

    /**
     * ViewHolder pro zobrazeni bez detailu
     */
    inner class FolderHeaderViewHolder(private val binding: FolderHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //Nastaveni listeneru
        init {
            itemView.tag = this
            itemView.setOnClickListener(onClickListener)
            itemView.setOnLongClickListener(onLongClickListener)
        }

        //Binding dat a UI
        fun bind(data: FolderWithItems) {
            binding.folderName.apply {
                text = data.folder.title
                setTextColor(data.folder.itemColor)
            }
            binding.subText.apply {
                text = data.itemList.toMutableList().apply { sortBy { it.position } }
                    .map(FolderItemModel::label).joinToString(", ")
                setTextColor(data.folder.itemColor)
            }
            binding.circleIcon.setColorFilter(data.folder.itemColor)
            binding.folderCardView.setCardBackgroundColor(data.folder.backgroundColor)
        }
    }

}





