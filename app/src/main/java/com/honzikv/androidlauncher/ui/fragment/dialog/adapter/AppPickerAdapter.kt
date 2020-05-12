package com.honzikv.androidlauncher.ui.fragment.dialog.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.data.model.DrawerApp
import com.honzikv.androidlauncher.databinding.IconWithTitleRightCheckboxBinding

class AppPickerAdapter : RecyclerView.Adapter<AppPickerAdapter.AppItemViewHolder>() {

    private var items = listOf<DrawerApp>()

    private var selectedItems = hashSetOf<DrawerApp>()

    private var textColor = Color.WHITE

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
    }

    fun setItems(items: List<DrawerApp>) {
        this.items = items
        selectedItems.clear()
    }

    fun getSelectedItems() = selectedItems.toMutableList().apply { sortBy { it.label } }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AppItemViewHolder(
        IconWithTitleRightCheckboxBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: AppItemViewHolder, position: Int) {
        holder.bind(items[position], selectedItems)
    }

    inner class AppItemViewHolder(val binding: IconWithTitleRightCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DrawerApp, selectedItems: MutableSet<DrawerApp>) {
            binding.icon.setImageDrawable(data.icon)
            binding.label.apply {
                text = data.label
                setTextColor(textColor)
            }

            binding.checkBox.setOnClickListener {
                binding.checkBox.isSelected = !binding.checkBox.isChecked

                if (binding.checkBox.isChecked) {
                    selectedItems.add(data)
                } else if (!binding.checkBox.isChecked) {
                    selectedItems.remove(data)
                }
            }
        }


    }
}