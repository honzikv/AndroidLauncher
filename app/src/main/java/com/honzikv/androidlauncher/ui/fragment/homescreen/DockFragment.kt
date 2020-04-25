package com.honzikv.androidlauncher.ui.fragment.homescreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.databinding.DockFragmentBinding
import com.honzikv.androidlauncher.viewmodel.DockViewModel
import org.koin.android.ext.android.inject

class DockFragment : Fragment() {

    private val dockViewModel: DockViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DockFragmentBinding.inflate(inflater)
        initialize(binding)
        return binding.root
    }

    private fun initialize(binding: DockFragmentBinding) {
    }

}
