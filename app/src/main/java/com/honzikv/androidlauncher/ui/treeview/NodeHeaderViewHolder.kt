package com.honzikv.androidlauncher.ui.treeview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.honzikv.androidlauncher.databinding.TreeViewHeaderTextBinding
import com.unnamed.b.atv.model.TreeNode

class NodeHeaderViewHolder(context: Context) : TreeNode.BaseNodeViewHolder<String>(context) {
    override fun createNodeView(node: TreeNode, headerText: String): View {
        val binding = TreeViewHeaderTextBinding.inflate(LayoutInflater.from(context))
        binding.headerText.text = headerText

        return binding.root
    }

}