package com.honzikv.androidlauncher.ui.fragment.settings.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.databinding.*
import com.honzikv.androidlauncher.model.ThemeProfileModel
import com.honzikv.androidlauncher.utils.MATERIAL_MIN_LENGTH
import com.honzikv.androidlauncher.utils.RADIUS_CARD_VIEW
import com.multilevelview.MultiLevelAdapter
import com.multilevelview.MultiLevelRecyclerView
import com.multilevelview.models.RecyclerViewItem
import kotlinx.android.synthetic.main.settings_switch_item.view.*
import timber.log.Timber


class SettingsMenuAdapter(
    private var items: MutableList<RecyclerViewItem>,
    private val recyclerView: MultiLevelRecyclerView
) :
    MultiLevelAdapter(items) {


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

        private val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        )
    }

    fun setTheme(theme: ThemeProfileModel) {
        cardViewTextColor = theme.drawerTextFillColor
        cardViewBackgroundColor = theme.drawerSearchBackgroundColor
        childTextFillColor = theme.drawerTextFillColor

        switchTrackColors[0] = theme.switchBackgroundColor
        switchTrackColors[1] = theme.switchBackgroundColor
        switchThumbColors[1] = theme.switchThumbColorOn
        switchThumbColors[0] = theme.switchThumbColorOff
    }

    private var cardViewTextColor: Int = Color.WHITE

    private var cardViewBackgroundColor: Int = Color.BLACK

    private var childTextFillColor: Int = Color.WHITE

    private val switchThumbColors = intArrayOf(
        Color.WHITE,
        Color.WHITE
    )

    private val switchTrackColors = intArrayOf(
        Color.BLACK,
        Color.BLACK
    )

    fun setOnClickExpand(view: View, holder: RecyclerView.ViewHolder) {
        Timber.d("Expanding viewholder $holder")
        view.setOnClickListener {
            recyclerView.toggleItemsGroup(holder.adapterPosition)
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

            R.layout.settings_text_left_right_item -> TextLeftRightViewHolder(
                SettingsTextLeftRightItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            R.layout.settings_text_left_item -> TextLeftViewHolder(
                SettingsTextLeftItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
//R.layout.settings_sub_header_icon_right_item
            else -> SubHeaderIconRightViewHolder(
                SettingsSubHeaderIconRightItemBinding.inflate(
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

            R.layout.settings_text_left_item -> (holder as TextLeftViewHolder)
                .bind(item as TextLeftItem)

            R.layout.settings_sub_header_icon_right_item -> (holder as SubHeaderIconRightViewHolder)
                .bind(item as SubHeaderItem)

        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is Header -> R.layout.settings_title
            is HeaderItem -> R.layout.settings_header_item
            is SwitchItem -> R.layout.settings_switch_item
            is TextLeftRightItem -> R.layout.settings_text_left_right_item
            is SpinnerItem -> R.layout.settings_spinner_item
            is TextLeftItem -> R.layout.settings_text_left_item
            is SubHeaderItem -> R.layout.settings_sub_header_icon_right_item
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
            setOnClickExpand(binding.root, this)

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

            binding.switchItem.apply {
                setOnCheckedChangeListener(null)
                isChecked = data.isChecked
                setOnCheckedChangeListener { _, isChecked ->
                    Timber.d("Clicked $switchItem")
                    data.performClick(isChecked)
                }

                DrawableCompat.setTintList(
                    DrawableCompat.wrap(thumbDrawable),
                    ColorStateList(states, switchThumbColors)
                )

                DrawableCompat.setTintList(
                    DrawableCompat.wrap(trackDrawable),
                    ColorStateList(states, switchTrackColors)
                )
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

    inner class TextLeftViewHolder(val binding: SettingsTextLeftItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: TextLeftItem) {
            binding.constraintLayout.layoutParams =
                getConstraintLayoutMargin(data.level, binding.constraintLayout.layoutParams)

            binding.textLeft.apply {
                text = data.textLeft
                setTextColor(childTextFillColor)
            }

            binding.root.setOnClickListener { data.functionOnClick() }
        }
    }

    inner class SubHeaderIconRightViewHolder(val binding: SettingsSubHeaderIconRightItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        //Rotace ikony pro detail menu
        private fun getRotationAngle() =
            if (items[adapterPosition].isExpanded) {
                180f
            } else {
                0f
            }


        fun bind(data: SubHeaderItem) {
            binding.cardView.setOnClickListener {
                recyclerView.toggleItemsGroup(adapterPosition)
                binding.expandIcon.animate().rotation(
                    getRotationAngle()
                )
            }

            binding.cardView.apply {
                setCardBackgroundColor(cardViewBackgroundColor)
                radius = RADIUS_CARD_VIEW
                layoutParams = getCardViewMargin(layoutParams)
            }

            binding.expandIcon.apply {
                setColorFilter(childTextFillColor)
                rotation = getRotationAngle()
            }
            binding.constraintLayout.layoutParams =
                getConstraintLayoutMargin(data.level, binding.constraintLayout.layoutParams)

            binding.textLeft.apply {
                text = data.textLeft
                setTextColor(childTextFillColor)
            }
        }
    }

}
