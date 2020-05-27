package com.honzikv.androidlauncher.ui.fragment.picker.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.honzikv.androidlauncher.model.DrawerApp
import com.honzikv.androidlauncher.databinding.IconWithTitleRightCheckboxBinding

class AppPickerAdapter : RecyclerView.Adapter<AppPickerAdapter.AppItemViewHolder>() {

    private var items = listOf<DrawerApp>()

    private var textColor = Color.WHITE

    private var itemsSelected = MutableLiveData(0)

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
    }

    fun setItems(items: List<DrawerApp>) {
        this.items = items
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

        fun bind(data: DrawerApp) {
            binding.icon.setImageDrawable(data.icon)
            binding.label.apply {
                text = data.label
                setTextColor(textColor)
            }

            binding.checkBox.apply {
                //Nastaveni checkboxu, jinak po scrollovani muze holder zaskrtnout nezaskrtla data
                isChecked = items[adapterPosition].isChecked
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