package com.honzikv.androidlauncher.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.honzikv.androidlauncher.data.SystemDataRepository

class SystemDataViewModel(private val systemRepository: SystemDataRepository) : ViewModel() {

    fun getAppList(context: Context) = systemRepository.getAppList(context)

}