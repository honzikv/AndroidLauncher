package com.honzikv.androidlauncher.ui.fragment.homescreen.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.data.model.FolderItemModel
import com.honzikv.androidlauncher.data.model.FolderWithItems
import com.honzikv.androidlauncher.databinding.FolderDetailBinding
import com.honzikv.androidlauncher.databinding.FolderHeaderBinding
import com.honzikv.androidlauncher.ui.fragment.dialog.EditFolderSettingsDialogFragment

class FolderListAdapter(context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * List of all folders displayed on current page
     */
    private var folderList: List<FolderWithItems> = mutableListOf()

    fun setFolderList(folderList: List<FolderWithItems>) {
        this.folderList = folderList
    }

    private val onClickListener = View.OnClickListener { view ->
        val viewHolder = view?.tag as RecyclerView.ViewHolder
        val item = folderList[viewHolder.adapterPosition]
        item.showItems = !item.showItems
        notifyItemChanged(viewHolder.adapterPosition)
    }

    private val onLongClickListener = View.OnLongClickListener { view ->
        val viewHolder = view?.tag as RecyclerView.ViewHolder
        val item = folderList[viewHolder.adapterPosition]
        val fragmentActivity = context as FragmentActivity

        EditFolderSettingsDialogFragment.newInstance(item.folder).apply {
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

    override fun getItemViewType(position: Int) =
        if (folderList[position].showItems) {
            R.layout.folder_detail
        } else {
            R.layout.folder_header
        }

    inner class FolderDetailViewHolder(private val binding: FolderDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.tag = this
            itemView.setOnClickListener(onClickListener)
            itemView.setOnLongClickListener(onLongClickListener)
        }

        fun bind(data: FolderWithItems) {
            binding.recyclerView.apply {
                layoutManager = GridLayoutManager(context, 4) //TODO
                adapter = FolderAdapter(data.itemList)
                (adapter as FolderAdapter).setLabelColor(data.folder.itemColor)
            }
            binding.folderName.apply {
                setTextColor(data.folder.itemColor)
                text = data.folder.title
            }
            DrawableCompat.wrap(binding.minimizeIcon.drawable).setTint(data.folder.itemColor)
            binding.folderCardView.setCardBackgroundColor(data.folder.backgroundColor)
        }
    }

    inner class FolderHeaderViewHolder(private val binding: FolderHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.tag = this
            itemView.setOnClickListener(onClickListener)
            itemView.setOnLongClickListener(onLongClickListener)
        }

        fun bind(data: FolderWithItems) {
            binding.folderName.apply {
                text = data.folder.title
                setTextColor(data.folder.itemColor)
            }
            binding.subText.apply {
                text = data.itemList.map(FolderItemModel::label).joinToString(", ")
                setTextColor(data.folder.itemColor)
            }
            DrawableCompat.wrap(binding.circleIcon.drawable).setTint(data.folder.itemColor)
            binding.folderCardView.setCardBackgroundColor(data.folder.backgroundColor)
        }
    }

}





