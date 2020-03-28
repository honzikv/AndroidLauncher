package com.honzikv.androidlauncher.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.honzikv.androidlauncher.data.database.dao.DockDao
import com.honzikv.androidlauncher.data.model.entity.DockModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DockDataRepository constructor(
    private val dockDao: DockDao
) {

    val dock = dockDao.getInstance()

}