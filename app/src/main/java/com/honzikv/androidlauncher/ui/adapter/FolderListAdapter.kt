package com.honzikv.androidlauncher.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.data.model.entity.FolderWithItems
import com.honzikv.androidlauncher.databinding.FolderDetailBinding
import com.honzikv.androidlauncher.databinding.FolderHeaderBinding

class FolderListAdapter :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val folderList: MutableList<FolderWithItems> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val binding =
                FolderHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)

            return FolderHeaderViewHolder(binding)
        } else {
            FolderDetailViewHolder(
                FolderDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }


    override fun getItemCount(): Int = folderList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = folderList[position]

    }
}

class FolderHeaderViewHolder(private val binding: FolderHeaderBinding) :
    RecyclerView.ViewHolder(binding.root) {

}

class FolderDetailViewHolder(private val binding: FolderDetailBinding) :
    RecyclerView.ViewHolder(binding.root) {

}

interface RecyclerViewOnClickListener {
    fun onClick(view: View?, position: Int)

    fun onLongClick(view: View?, position: Int)
}

