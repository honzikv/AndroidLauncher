package com.honzikv.androidlauncher.ui.fragment.settings.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.adapters.SpinnerBindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.data.model.entity.ThemeProfileModel
import com.honzikv.androidlauncher.databinding.SettingsHeaderItemBinding
import com.honzikv.androidlauncher.databinding.SettingsSpinnerItemBinding
import com.honzikv.androidlauncher.databinding.SettingsSwitchItemBinding
import com.multilevelview.MultiLevelAdapter
import com.multilevelview.models.RecyclerViewItem

class SettingsMenuAdapter(private var items: MutableList<RecyclerViewItem>) :
    MultiLevelAdapter(items) {

    private val themes: MutableList<ThemeProfileModel> = mutableListOf()

    fun setThemes(themes: MutableList<ThemeProfileModel>) {
        this.themes.apply {
            clear()
            addAll(themes)
        }
    }

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
                SettingsSwitchItemBinding.inflate(
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

            R.layout.settings_switch_item -> (holder as RadioButtonViewHolder)
                .bind(item as SwitchItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is HeaderItem -> R.layout.settings_header_item
            is SwitchItem -> R.layout.settings_switch_item
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

    inner class RadioButtonViewHolder(val binding: SettingsSwitchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SwitchItem) {
            binding.radioButton.isChecked = data.isChecked
            binding.textLeft.text = data.textLeft
            binding.radioButton.setOnCheckedChangeListener { _, isChecked ->
                data.functionOnClick(isChecked)
            }
        }
    }

    inner class SpinnerViewHolder<T>(val binding: SettingsSpinnerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SpinnerItem<T>) {
            binding.textLeft.text = data.textLeft
            data.items.
        }
    }


}