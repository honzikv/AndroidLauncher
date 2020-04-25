package com.honzikv.androidlauncher.ui.fragment.settings

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.honzikv.androidlauncher.databinding.SettingsFragmentBinding
import com.honzikv.androidlauncher.ui.treeview.NodeHeaderViewHolder
import com.honzikv.androidlauncher.ui.treeview.NodeTextLeftViewHolder
import com.unnamed.b.atv.model.TreeNode
import com.unnamed.b.atv.view.AndroidTreeView

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = SettingsFragmentBinding.inflate(inflater)

        val treeView = createTreeView()
        binding.constraintLayout.addView(treeView.view)
        return binding.root
    }

    private fun createTreeView(): AndroidTreeView {
        val context = requireContext()
        val root = TreeNode.root()
        val treeView = AndroidTreeView(context, root)

        val colorsHeader = TreeNode("Colors").setViewHolder(NodeHeaderViewHolder(context))
        val exampleChild =
            TreeNode(NodeTextLeftViewHolder.Details("Hello34243234", Color.BLACK)).setViewHolder(
                NodeTextLeftViewHolder(View.OnClickListener { println("hehe") }, context)
            )
        exampleChild.isExpanded = true
        val exampleChild2 =
            TreeNode(NodeTextLeftViewHolder.Details("Hello34243234", Color.BLACK)).setViewHolder(
                NodeTextLeftViewHolder(View.OnClickListener { println("hehe") }, context)
            )
        exampleChild.addChild(exampleChild2)
        colorsHeader.addChild(exampleChild)
        colorsHeader.isExpanded = true
        root.addChild(colorsHeader)

        return treeView
    }
}
