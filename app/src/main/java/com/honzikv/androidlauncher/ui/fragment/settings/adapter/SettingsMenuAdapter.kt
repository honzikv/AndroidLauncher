package com.honzikv.androidlauncher.ui.fragment.settings.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginBottom
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.data.model.entity.ThemeProfileModel
import com.honzikv.androidlauncher.databinding.SettingsHeaderItemBinding
import com.honzikv.androidlauncher.databinding.SettingsSpinnerItemBinding
import com.honzikv.androidlauncher.databinding.SettingsSwitchItemBinding
import com.honzikv.androidlauncher.databinding.SettingsTextLeftRightItemBinding
import com.multilevelview.MultiLevelAdapter
import com.multilevelview.models.RecyclerViewItem

class SettingsMenuAdapter(
    private var items: MutableList<RecyclerViewItem>
) :
    MultiLevelAdapter(items) {

    fun changeTheme(theme: ThemeProfileModel) {
        headerBackgroundColor = theme.drawerBackgroundColor
        headerTextFillColor = theme.drawerTextFillColor
        childBackgroundColor = theme.drawerSearchBackgroundColor
        childTextFillColor = theme.drawerSearchTextColor
    }

    private var headerBackgroundColor: Int = Color.BLACK

    private var headerTextFillColor: Int = Color.WHITE

    private var childBackgroundColor: Int = Color.BLACK

    private var childTextFillColor: Int = Color.WHITE


    companion object {
        private const val materialMinMargin = 80
        fun getConstraintLayoutMargin(
            level: Int,
            layoutParams: ViewGroup.LayoutParams
        ): ViewGroup.MarginLayoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
            topMargin = 0
            leftMargin = level * materialMinMargin
            bottomMargin = 0
            rightMargin = 0
        }
    }


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
            binding.constraintLayout.setBackgroundColor(headerBackgroundColor)
            binding.constraintLayout.layoutParams =
                getConstraintLayoutMargin(data.level, binding.constraintLayout.layoutParams)
            binding.headerText.text = data.headerText
            binding.subText.text = data.headerSubText
        }
    }

    inner class SwitchItemViewHolder(val binding: SettingsSwitchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SwitchItem) {
            binding.constraintLayout.setBackgroundColor(childBackgroundColor)
            binding.constraintLayout.layoutParams =
                getConstraintLayoutMargin(data.level, binding.constraintLayout.layoutParams)

            binding.textLeft.apply {
                text = data.textLeft
                setTextColor(childTextFillColor)
            }
            binding.radioButton.apply {
                isChecked = data.isChecked
                setOnCheckedChangeListener { _, isChecked ->
                    data.functionOnClick(isChecked)
                }
                //Todo color
            }
        }
    }

    inner class SpinnerViewHolder(val binding: SettingsSpinnerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SpinnerItem) {
            binding.constraintLayout.apply {
                layoutParams =
                    getConstraintLayoutMargin(data.level, binding.constraintLayout.layoutParams)
                setBackgroundColor(childBackgroundColor)
            }
            binding.textLeft.apply {
                text = data.textLeft
                setTextColor(childTextFillColor)
            }
            binding.spinner.apply {
                adapter = data.adapter
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                setBackgroundColor(childBackgroundColor)

            }
        }

    }

    inner class TextLeftRightViewHolder(val binding: SettingsTextLeftRightItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: TextLeftRightItem) {
            binding.constraintLayout.apply {
                layoutParams =
                    getConstraintLayoutMargin(data.level, binding.constraintLayout.layoutParams)
                setBackgroundColor(childBackgroundColor)
            }
            binding.textLeft.apply {
                text = data.textLeft
                setTextColor(childTextFillColor)
            }
            binding.textRight.apply {
                text = data.textRight
                setTextColor(childTextFillColor)
            }
            binding.root.setOnClickListener { data.functionOnClick() }
        }


    }

}