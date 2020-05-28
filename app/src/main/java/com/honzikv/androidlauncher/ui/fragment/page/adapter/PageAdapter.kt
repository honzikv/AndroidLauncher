package com.honzikv.androidlauncher.ui.fragment.page.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.databinding.FolderListBinding
import com.honzikv.androidlauncher.model.PageWithFolders

/**
 * Adapter pro stranky ve view pageru ve fragmentu HomescreenFragment
 */
class PageAdapter(
    /**
     * Aplikacni kontext pro vytvoreni layout manageru
     */
    val context: Context
) :
    RecyclerView.Adapter<PageAdapter.PageViewHolder>() {

    /**
     * Seznam stranek
     */
    private var pages: List<PageWithFolders> = mutableListOf()

    override fun getItemCount() = pages.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        return PageViewHolder(
            FolderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(pages[position])
    }

    /**
     * Setter pro nastaveni seznamu stranek, automaticky stranky i seradi
     */
    fun setPages(newPageList: List<PageWithFolders>) {
        val sorted = newPageList.sortedBy { it.page.pageNumber }
        this.pages = sorted
    }

    /**
     * ViewHolder pro jednotlive stranky
     */
    inner class PageViewHolder(val binding: FolderListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //Binding UI a dat stranky
        fun bind(page: PageWithFolders) {
            //Kazda stranka ma vlastni recycler view se slozkami, pro ktery se vytvori adapter
            val folderAdapter = FolderAdapter(context, binding.folderListRecyclerView)
            folderAdapter.setFolderList(
                page.folderList.toMutableList().apply { sortBy { it.folder.position } })

            binding.folderListRecyclerView.apply {
                adapter = folderAdapter
                //Slozky se budou radit od spoda, proto musime nastavit layout manager obracene
                layoutManager = LinearLayoutManager(context).apply {
                    reverseLayout = true
                }
                (adapter as FolderAdapter).notifyDataSetChanged()
            }
        }
    }


}