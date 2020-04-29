package com.honzikv.androidlauncher.ui.fragment.settings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.databinding.SettingsHeaderItemBinding
import com.honzikv.androidlauncher.databinding.SettingsSpinnerItemBinding
import com.honzikv.androidlauncher.databinding.SettingsSwitchItemBinding
import com.honzikv.androidlauncher.databinding.SettingsTextLeftRightItemBinding
import com.multilevelview.MultiLevelAdapter
import com.multilevelview.models.RecyclerViewItem

class SettingsMenuAdapter(private var items: MutableList<RecyclerViewItem>) :
    MultiLevelAdapter(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.settings_header_item -> HeaderViewHolder(
                SettingsHeaderItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            R.layout.settings_switch_item -> SwitchItemViewHolder(
                SettingsSwitchItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            R.layout.settings_spinner_item -> SpinnerViewHolder(
                SettingsSpinnerItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
//R.layout.settings_text_left_right_item
            else -> TextLeftRightViewHolder(
                SettingsTextLeftRightItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
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

            R.layout.settings_switch_item -> (holder as SwitchItemViewHolder)
                .bind(item as SwitchItem)

            R.layout.settings_spinner_item -> (holder as SpinnerViewHolder)
                .bind(item as SpinnerItem)

            R.layout.settings_text_left_right_item -> (holder as TextLeftRightViewHolder)
                .bind(item as TextLeftRightItem)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is HeaderItem -> R.layout.settings_header_item
            is SwitchItem -> R.layout.settings_switch_item
            is TextLeftRightItem -> R.layout.settings_text_left_right_item
            is SpinnerItem -> R.layout.settings_spinner_item
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

    inner class SwitchItemViewHolder(val binding: SettingsSwitchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SwitchItem) {
            binding.radioButton.isChecked = data.isChecked
            binding.textLeft.text = data.textLeft
            binding.radioButton.setOnCheckedChangeListener { _, isChecked ->
                data.functionOnClick(isChecked)
            }
        }
    }

    inner class SpinnerViewHolder(val binding: SettingsSpinnerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SpinnerItem) {
            binding.textLeft.text = data.textLeft
            binding.spinner.adapter = data.adapter
            binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (position != 0) {
                        data.functionOnClick(binding.spinner.adapter.getItem(position) as Displayable)
                    }
                }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }
                }
            }
        }

        inner class TextLeftRightViewHolder(val binding: SettingsTextLeftRightItemBinding) :
            RecyclerView.ViewHolder(binding.root) {

            fun bind(data: TextLeftRightItem) {
                binding.textLeft.text = data.textLeft
                binding.textRight.text = data.textRight
                binding.root.setOnClickListener { data.functionOnClick() }
            }
        }

    }