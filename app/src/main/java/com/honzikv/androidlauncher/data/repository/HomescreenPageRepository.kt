package com.honzikv.androidlauncher.data.repository

import androidx.lifecycle.MutableLiveData
import com.honzikv.androidlauncher.data.database.dao.HomescreenPageDao
import com.honzikv.androidlauncher.data.model.entity.HomescreenPageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class HomescreenPageRepository(
    private val homescreenPageDao: HomescreenPageDao
) {
    val homescreenPageList = homescreenPageDao.getAllPages()
}