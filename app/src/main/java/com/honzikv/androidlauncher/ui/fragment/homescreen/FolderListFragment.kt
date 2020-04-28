package com.honzikv.androidlauncher.ui.fragment.homescreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView

import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.data.model.entity.FolderWithItems
import com.honzikv.androidlauncher.ui.fragment.homescreen.adapter.FolderListAdapter
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import org.koin.android.ext.android.inject

class FolderListFragment : Fragment() {

    private val homescreenViewModel: HomescreenViewModel by inject()

    private lateinit var folders: LiveData<List<FolderWithItems>>

    private lateinit var recyclerView: RecyclerView

    private lateinit var recyclerAdapter: FolderListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initialize()
        return inflater.inflate(R.layout.folder_list_fragment, container, false)
    }

    private fun initialize() {
        folders = homescreenViewModel.folderList

    }




}

