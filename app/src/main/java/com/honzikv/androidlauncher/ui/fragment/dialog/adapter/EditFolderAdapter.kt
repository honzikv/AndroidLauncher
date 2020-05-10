package com.honzikv.androidlauncher.ui.fragment.dialog.adapter

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.data.model.FolderItemModel
import com.honzikv.androidlauncher.databinding.EditHomescreenContainerItemBinding

class EditFolderAdapter(val fragmentActivity: FragmentActivity, val delete: (Long) -> Unit) :
    RecyclerView.Adapter<EditFolderAdapter.FolderItemViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderItemViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: FolderItemViewHolder, position: Int) {
        TODO("Not yet implemented")
    }


    inner class FolderItemViewHolder(val binding: EditHomescreenContainerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: FolderItemModel) {
        }
    }
}