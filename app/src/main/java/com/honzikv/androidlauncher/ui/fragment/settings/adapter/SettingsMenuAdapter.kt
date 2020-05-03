package com.honzikv.androidlauncher.ui.fragment.settings.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.data.model.ThemeProfileModel
import com.honzikv.androidlauncher.databinding.*
import com.honzikv.androidlauncher.ui.constants.MATERIAL_MIN_LENGTH
import com.honzikv.androidlauncher.ui.constants.RADIUS_CARD_VIEW
import com.multilevelview.MultiLevelAdapter
import com.multilevelview.models.RecyclerViewItem

class SettingsMenuAdapter(
    private var items: MutableList<RecyclerViewItem>
) :
    MultiLevelAdapter(items) {

    fun changeTheme(theme: ThemeProfileModel) {
        cardViewTextColor = theme.drawerTextFillColor
        cardViewBackgroundColor = theme.drawerSearchBackgroundColor
        childTextFillColor = theme.drawerTextFillColor
    }

    private var cardViewTextColor: Int = Color.WHITE

    private var cardViewBackgroundColor: Int = Color.BLACK

    private var childTextFillColor: Int = Color.WHITE

    companion object {

        fun getConstraintLayoutMargin(
            level: Int,
            layoutParams: ViewGroup.LayoutParams
        ): ViewGroup.MarginLayoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
            topMargin = 0
            leftMargin = 4 * level * MATERIAL_MIN_LENGTH
            bottomMargin = 0
            rightMargin = 0
        }

        fun getCardViewMargin(layoutParams: ViewGroup.LayoutParams) =
            (layoutParams as ViewGroup.MarginLayoutParams).apply {
                marginStart = MATERIAL_MIN_LENGTH * 4
                marginEnd = MATERIAL_MIN_LENGTH * 4
            }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.settings_title -> SettingsTitleViewHolder(
                SettingsTitleBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

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

            R.layout.settings_title -> (holder as SettingsTitleViewHolder)
                .bind(item as Header)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Header -> R.layout.settings_title
            is HeaderItem -> R.layout.settings_header_item
            is SwitchItem -> R.layout.settings_switch_item
            is TextLeftRightItem -> R.layout.settings_text_left_right_item
            is SpinnerItem -> R.layout.settings_spinner_item
            else -> -1 //never happens
        }
    }

    inner class SettingsTitleViewHolder(val binding: SettingsTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Header) {
            binding.textView.apply {
                text = data.text
                setTextColor(cardViewTextColor)
            }
        }
    }

    inner class HeaderViewHolder(val binding: SettingsHeaderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: HeaderItem) {
            binding.cardView.apply {
                setCardBackgroundColor(cardViewBackgroundColor)
                radius = RADIUS_CARD_VIEW
                layoutParams = getCardViewMargin(layoutParams)
            }
            binding.constraintLayout.layoutParams =
                getConstraintLayoutMargin(data.level, binding.constraintLayout.layoutParams)
            binding.headerText.apply {
                text = data.headerText
                setTextColor(cardViewTextColor)
            }
            binding.subText.apply {
                text = data.headerSubText
                setTextColor(cardViewTextColor)
            }
        }
    }

    inner class SwitchItemViewHolder(val binding: SettingsSwitchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SwitchItem) {
            binding.constraintLayout.layoutParams =
                getConstraintLayoutMargin(data.level, binding.constraintLayout.layoutParams)

            binding.textLeft.apply {
                text = data.textLeft
                setTextColor(childTextFillColor)
            }
            binding.radioButton.apply {
                isChecked = data.isChecked
                setOnCheckedChangeListener { _, isChecked ->
                    data.performClick(isChecked)
                }
                //Todo color
            }
        }
    }

    inner class SpinnerViewHolder(val binding: SettingsSpinnerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: SpinnerItem) {
            binding.constraintLayout.layoutParams =
                getConstraintLayoutMargin(data.level, binding.constraintLayout.layoutParams)

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
                setBackgroundColor(cardViewBackgroundColor)

            }
        }
    }

    inner class TextLeftRightViewHolder(val binding: SettingsTextLeftRightItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: TextLeftRightItem) {
            binding.constraintLayout.layoutParams =
                getConstraintLayoutMargin(data.level, binding.constraintLayout.layoutParams)

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