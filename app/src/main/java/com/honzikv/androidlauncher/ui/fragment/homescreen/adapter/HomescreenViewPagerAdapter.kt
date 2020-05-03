package com.honzikv.androidlauncher.ui.fragment.homescreen.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.honzikv.androidlauncher.data.model.PageWithFolders
import com.honzikv.androidlauncher.ui.fragment.homescreen.FolderListFragment

class HomescreenViewPagerAdapter(fragmentManager: FragmentManager, activity: FragmentActivity) :
    FragmentStateAdapter(
        fragmentManager, activity.lifecycle
    ) {

    private var fragments = mutableListOf<Fragment>()

    private var pages: List<PageWithFolders> = mutableListOf()
    fun setPages(pages: List<PageWithFolders>) {
       //todo
        this.pages = pages

        pages.forEach { page ->
            fragments.add(FolderListFragment(page.folderList))
        }
    }

    override fun getItemCount() = pages.size

    override fun createFragment(position: Int): Fragment {
        return FolderListFragment(pages[position].folderList)
    }

}