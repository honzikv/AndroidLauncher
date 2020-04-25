package com.honzikv.androidlauncher.ui.treeview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.honzikv.androidlauncher.databinding.TreeViewRadioButtonBinding
import com.unnamed.b.atv.model.TreeNode


class NodeRadioButtonViewHolder(
    private val onClickListener: View.OnClickListener,
    context: Context
) :
    TreeNode.BaseNodeViewHolder<NodeRadioButtonViewHolder.Details>(context) {

    override fun createNodeView(node: TreeNode, details: Details): View {
        val binding = TreeViewRadioButtonBinding.inflate(LayoutInflater.from(context))
        binding.radioButton.setOnClickListener(onClickListener)
        binding.radioButton.isChecked = details.isRadioButtonChecked
        binding.textLeft.text = details.title
        binding.textLeft.setTextColor(details.textColor)

        return binding.root
    }

    data class Details(
        val title: String,
        val textColor: Int,
        val isRadioButtonChecked: Boolean
    )
}
