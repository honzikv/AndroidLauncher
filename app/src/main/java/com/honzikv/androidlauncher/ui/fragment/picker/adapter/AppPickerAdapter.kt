package com.honzikv.androidlauncher.ui.fragment.picker.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.databinding.IconWithTitleRightCheckboxBinding
import com.honzikv.androidlauncher.model.DrawerApp
import com.honzikv.androidlauncher.model.ThemeProfileModel

/**
 * Adapter pro recycler view v app picker fragmentu
 */
class AppPickerAdapter : RecyclerView.Adapter<AppPickerAdapter.AppItemViewHolder>() {

    companion object {
        /**
         * Pro nastaveni barev checkboxu
         */
        private val states = arrayOf(
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_checked)
        )
    }

    /**
     * Vsechny aplikace
     */
    private var items = listOf<DrawerApp>()

    /**
     * Barva popisku ikon
     */
    private var textColor = Color.WHITE

    /**
     * Pocet vybranych predmetu
     */
    private var itemsSelected = MutableLiveData(0)

    /**
     * Barvy checkboxu
     */
    private val checkboxThumbColors = intArrayOf(
        Color.WHITE,
        Color.BLACK
    )

    /**
     * Setter pro [items]
     */
    fun setItems(items: List<DrawerApp>) {
        this.items = items
    }

    /**
     * Setter pro nastaveni tematu - volano kdykoliv pri zmene LiveDat
     */
    fun setTheme(theme: ThemeProfileModel) {
        this.textColor = theme.drawerTextFillColor
        checkboxThumbColors[0] = theme.switchBackgroundColor
        checkboxThumbColors[1] = theme.switchThumbColorOn
    }

    fun getSelectedItems() = items.filter { it.isChecked }.toMutableList()

    fun getSelectedItemsCount() = itemsSelected as LiveData<Int>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = AppItemViewHolder(
        IconWithTitleRightCheckboxBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: AppItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class AppItemViewHolder(val binding: IconWithTitleRightCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Bind dat a UI
         */
        fun bind(data: DrawerApp) {
            binding.icon.setImageDrawable(data.icon)
            binding.label.apply {
                text = data.label
                setTextColor(textColor)
            }

            binding.checkBox.apply {
                //Nastaveni checkboxu, jinak po scrollovani muze holder zaskrtnout nezaskrtla data
                isChecked = items[adapterPosition].isChecked
                buttonTintList = ColorStateList(states, checkboxThumbColors)
                setOnClickListener {

                    if (items[adapterPosition].isChecked) {
                        binding.checkBox.isChecked = false
                        items[adapterPosition].isChecked = false
                        itemsSelected.apply {
                            var count = value!!
                            count -= 1
                            postValue(count)
                        }
                    } else {
                        binding.checkBox.isChecked = true
                        items[adapterPosition].isChecked = true
                        itemsSelected.apply {
                            var count = value!!
                            count += 1
                            postValue(count)
                        }
                    }
                }
            }
        }


    }
}