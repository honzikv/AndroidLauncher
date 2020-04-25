package com.honzikv.androidlauncher.ui.treeview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.honzikv.androidlauncher.databinding.TreeViewTextLeftBinding
import com.unnamed.b.atv.model.TreeNode
import timber.log.Timber

class NodeTextLeftViewHolder(
    private val onClickListener: View.OnClickListener,
    context: Context
) : TreeNode.BaseNodeViewHolder<NodeTextLeftViewHolder.Details>(context) {

    override fun createNodeView(
        node: TreeNode,
        nodeTextLeftViewHolderDetails: NodeTextLeftViewHolder.Details
    ): View {
        val binding = TreeViewTextLeftBinding.inflate(LayoutInflater.from(context))
        binding.textLeft.text = nodeTextLeftViewHolderDetails.textLeft
        Timber.d(binding.textLeft.text as String)
        binding.root.setOnClickListener(onClickListener)

        return binding.root
    }

    data class Details(val textLeft: String, val textColor: Int)
}