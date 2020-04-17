package com.honzikv.androidlauncher.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu

import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.databinding.HomescreenPageFragmentBinding
import com.honzikv.androidlauncher.viewmodel.HomescreenViewModel
import org.koin.android.ext.android.inject

class HomescreenPageFragment : Fragment() {

    private lateinit var binding: HomescreenPageFragmentBinding

    private val homescreenViewModel: HomescreenViewModel by inject()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = HomescreenPageFragmentBinding.inflate(inflater)
        binding.constraintLayout.setOnClickListener {
            longPressPopupMenu(binding.root)
        }
        return binding.root
    }


    private fun longPressPopupMenu(view: View) {
        PopupMenu(activity, view).apply {
            setOnMenuItemClickListener { item ->
                when (item!!.itemId) {
                    R.id.launcherSettings -> TODO("Launcher settings placeholder")
                    R.id.changeWallpaper -> TODO("Change Wallpaper")
                }

                true
            }
            inflate(R.menu.homescreen_long_click_popup_menu)
            show()
        }
    }

}
