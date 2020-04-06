package com.honzikv.androidlauncher.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.data.model.entity.FolderWithItems
import com.honzikv.androidlauncher.databinding.FolderBinding
import kotlinx.android.synthetic.main.folder_header.view.*

class FolderListAdapter(val folderList: List<FolderWithItems>) : RecyclerView.Adapter<FolderListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderListViewHolder =
        FolderListViewHolder(
            FolderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: FolderListViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

}

class FolderListViewHolder(val binding: FolderBinding) : RecyclerView.ViewHolder(binding.root) {


}