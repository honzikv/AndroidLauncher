package com.honzikv.androidlauncher.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.data.model.entity.FolderItemDto
import com.honzikv.androidlauncher.data.model.entity.FolderWithItems
import com.honzikv.androidlauncher.databinding.FolderDetailBinding
import com.honzikv.androidlauncher.databinding.FolderHeaderBinding
import com.honzikv.androidlauncher.data.repository.UserSettingsRepository
import org.koin.core.KoinComponent
import org.koin.core.inject

class FolderListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(), KoinComponent {

    private val folderList: MutableList<FolderWithItems> = mutableListOf()

    private val userSettingsRepository: UserSettingsRepository by inject()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.folder_header -> FolderHeaderViewHolder(
                FolderHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> FolderDetailViewHolder(
                FolderDetailBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
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
        if (folderList[position].showItems) R.layout.folder_detail else R.layout.folder_header

    inner class FolderDetailViewHolder(private val binding: FolderDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: FolderWithItems) {
            binding.recyclerView.apply {
                layoutManager = GridLayoutManager(context, userSettingsRepository.getFolderColsCount())
                adapter = FolderAdapter(data.itemList)
            }
        }
    }

    inner class FolderHeaderViewHolder(private val binding: FolderHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: FolderWithItems) {
            binding.folderName.text = data.folder.title
            binding.subText.text = data.itemList.map(FolderItemDto::label).joinToString(", ")
            TODO("Bind color")
        }
    }

}





