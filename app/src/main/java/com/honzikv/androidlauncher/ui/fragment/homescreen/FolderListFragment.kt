package com.honzikv.androidlauncher.ui.fragment.homescreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.honzikv.androidlauncher.data.model.FolderWithItems
import com.honzikv.androidlauncher.databinding.FolderListFragmentBinding
import com.honzikv.androidlauncher.ui.fragment.homescreen.adapter.FolderListAdapter

class FolderListFragment(private var folders: List<FolderWithItems>) : Fragment() {

    private lateinit var folderListAdapter: FolderListAdapter

    fun setFolders(folders: List<FolderWithItems>) {
        this.folders = folders
        folderListAdapter.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FolderListFragmentBinding.inflate(inflater, container, false)
        initialize(binding)
        return binding.root
    }

    private fun initialize(binding: FolderListFragmentBinding) {
        folderListAdapter = FolderListAdapter()
        binding.folderListRecyclerView.apply {
            val layoutManager = LinearLayoutManager(context)
            //To display items from bottom
            layoutManager.stackFromEnd = true
            adapter = folderListAdapter
            this.layoutManager = layoutManager
        }
        folderListAdapter.setFolderList(folders)
        folderListAdapter.notifyDataSetChanged()
    }
}

