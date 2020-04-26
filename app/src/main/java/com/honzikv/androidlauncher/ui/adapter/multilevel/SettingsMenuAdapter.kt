package com.honzikv.androidlauncher.ui.adapter.multilevel

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.databinding.SettingsHeaderItemBinding
import com.honzikv.androidlauncher.databinding.SettingsRadioButtonItemBinding
import com.multilevelview.MultiLevelAdapter
import com.multilevelview.models.RecyclerViewItem

class SettingsMenuAdapter(private var items: MutableList<RecyclerViewItem>) :
    MultiLevelAdapter(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.settings_header_item -> HeaderViewHolder(
                SettingsHeaderItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> RadioButtonViewHolder(
                SettingsRadioButtonItemBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        println("binding position $position")
        val item = items[position]
        when (getItemViewType(position)) {
            R.layout.settings_header_item -> (holder as HeaderViewHolder)
                .bind(item as HeaderItem)

            R.layout.settings_radio_button_item -> (holder as RadioButtonViewHolder)
                .bind(item as RadioButtonItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is HeaderItem -> R.layout.settings_header_item
            is RadioButtonItem -> R.layout.settings_radio_button_item
            is TextLeftRightItem -> R.layout.settings_text_left_right_item
            else -> -1
        }
    }

    inner class HeaderViewHolder(val binding: SettingsHeaderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: HeaderItem) {
            binding.headerText.text = data.headerText
            binding.subText.text = data.headerSubText
        }
    }

    inner class RadioButtonViewHolder(val binding: SettingsRadioButtonItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: RadioButtonItem) {
            binding.radioButton.isChecked = data.isChecked
            binding.textLeft.text = data.textLeft
        }
    }
}