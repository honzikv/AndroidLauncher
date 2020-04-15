package com.honzikv.androidlauncher.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import org.koin.android.ext.android.inject

class HomescreenFragment : Fragment() {

    private val homescreenViewModel: HomescreenViewModel by inject()

    private lateinit var pager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return view
    }


}
