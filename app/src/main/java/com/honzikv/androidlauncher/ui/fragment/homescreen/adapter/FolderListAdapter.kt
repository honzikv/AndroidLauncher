package com.honzikv.androidlauncher.ui.fragment.homescreen.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.data.model.FolderItemModel
import com.honzikv.androidlauncher.data.model.FolderWithItems
import com.honzikv.androidlauncher.databinding.FolderDetailBinding
import com.honzikv.androidlauncher.databinding.FolderHeaderBinding
import org.koin.core.KoinComponent

class FolderListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), KoinComponent {

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
        }

        fun bind(data: FolderWithItems) {
            binding.recyclerView.apply {
                layoutManager = GridLayoutManager(context, 4) //TODO
                adapter = FolderAdapter(data.itemList)
                (adapter as FolderAdapter).setLabelColor(data.folder.itemColor)
            }
            binding.folderName.text = data.folder.title
            //TODO item color
            binding.folderCardView.setBackgroundColor(data.folder.backgroundColor)
        }
    }

    inner class FolderHeaderViewHolder(private val binding: FolderHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.tag = this
            itemView.setOnClickListener(onClickListener)
        }

        fun bind(data: FolderWithItems) {
            binding.folderName.text = data.folder.title
            binding.subText.text =
                data.itemList.map(FolderItemModel::label).joinToString(", ")
            binding.folderCardView.setCardBackgroundColor(data.folder.backgroundColor)
        }
    }

}





