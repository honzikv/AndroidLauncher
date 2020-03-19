package com.honzikv.androidlauncher

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.honzikv.androidlauncher.activity.MainActivity
import com.honzikv.androidlauncher.adapters.AppInfoAdapter
import com.honzikv.androidlauncher.data.AppInfo
import com.honzikv.androidlauncher.databinding.FragmentHomescreenBinding
import kotlinx.android.synthetic.main.fragment_homescreen.view.*

class HomescreenFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentHomescreenBinding>(
            inflater, R.layout.fragment_homescreen, container, false
        )

        val view = binding.root
        view.AppGrid.adapter =
            AppInfoAdapter(view.context, (activity as MainActivity).launcherData.appList)

        return view
    }

}
