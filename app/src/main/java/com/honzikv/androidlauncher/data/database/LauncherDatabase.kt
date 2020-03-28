package com.honzikv.androidlauncher.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.honzikv.androidlauncher.data.database.dao.DockDao
import com.honzikv.androidlauncher.data.database.dao.FolderDao
import com.honzikv.androidlauncher.data.database.dao.HomescreenPageDao
import com.honzikv.androidlauncher.data.database.dao.UserAppDao
import com.honzikv.androidlauncher.data.model.entity.*

/**
 * Room database to store user edited data about homescreen
 */
@Database(
    entities = [FolderModel::class, DockModel::class, FolderItemModel::class, DockItemModel::class,
        HomescreenPageModel::class],
    version = 1
)
abstract class LauncherDatabase : RoomDatabase() {

    abstract fun folderDao(): FolderDao

    abstract fun dockDao(): DockDao

    abstract fun userAppDao(): UserAppDao

    abstract fun homescreenPageDao() : HomescreenPageDao
}