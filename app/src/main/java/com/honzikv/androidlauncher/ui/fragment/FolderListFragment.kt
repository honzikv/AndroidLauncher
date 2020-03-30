package com.honzikv.androidlauncher.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.data.model.entity.FolderModel
import com.honzikv.androidlauncher.ui.viewmodel.FolderListViewModel
import com.honzikv.androidlauncher.ui.viewmodel.HomescreenViewModel
import org.koin.android.ext.android.inject

class FolderListFragment : Fragment() {

    companion object {
        fun newInstance() = FolderListFragment()
    }

    private var folderViewModel = inject<FolderListViewModel>()

    private var homescreenViewModel = inject<HomescreenViewModel>()

    private lateinit var folders: List<FolderModel>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.folder_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

}
