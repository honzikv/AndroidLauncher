package com.honzikv.androidlauncher.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.honzikv.androidlauncher.data.database.dao.*
import com.honzikv.androidlauncher.data.model.entity.*

/**
 * Room database to store user edited data about homescreen
 */
@Database(
    entities = [FolderModel::class, FolderItemModel::class, DockItemModel::class,
        PageModel::class, ThemeProfileModel::class],
    version = 1
)
abstract class LauncherDatabase : RoomDatabase() {

    abstract fun folderDao(): FolderDao

    abstract fun dockDao(): DockDao

    abstract fun userAppDao(): UserAppDao

    abstract fun homescreenPageDao() : PageDao

    abstract fun themeProfileDao(): ThemeProfileDao
}