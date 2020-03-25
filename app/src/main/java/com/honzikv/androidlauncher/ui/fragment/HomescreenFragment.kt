package com.honzikv.androidlauncher.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.ui.activity.LauncherActivity
import com.honzikv.androidlauncher.adapters.AppInfoAdapter
import com.honzikv.androidlauncher.data.AppInfo
import com.honzikv.androidlauncher.databinding.FragmentHomescreenBinding
import com.honzikv.androidlauncher.ui.viewmodel.SystemDataViewModel
import com.honzikv.androidlauncher.utils.InjectorUtils
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
            inflater,
            R.layout.fragment_homescreen, container, false
        )

        val view = binding.root
        view.AppGrid.adapter = AppInfoAdapter(view.context, getAppList(view.context))
        return view
    }

    private fun getAppList(context: Context): List<AppInfo> = ViewModelProvider(
        this, InjectorUtils.getSystemDataViewModelFactory()
    ).get(SystemDataViewModel::class.java).getAppList(context)


}
