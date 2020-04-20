package com.honzikv.androidlauncher.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.viewmodel.AppDrawerViewModel
import org.koin.android.ext.android.inject

/**
 * A simple [Fragment] subclass.
 */
class AppDrawerFragment : Fragment() {

    private val appDrawerViewModel: AppDrawerViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        return inflater.inflate(R.layout.app_drawer_fragment, container, false)
    }

}
