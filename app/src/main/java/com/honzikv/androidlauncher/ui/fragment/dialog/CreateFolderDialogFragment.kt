package com.honzikv.androidlauncher.ui.fragment.dialog

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.databinding.CreateFolderDialogFragmentBinding

class CreateFolderDialogFragment : DialogFragment() {

    companion object {
        fun newInstance() = CreateFolderDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    var backgroundColor = Color.WHITE
    var backgroundText = Color.DKGRAY
    var name = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = CreateFolderDialogFragmentBinding.inflate(inflater)
        initialize(binding)
        return binding.root
    }

    fun initialize(binding: CreateFolderDialogFragmentBinding) {

    }

}
