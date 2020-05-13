package com.honzikv.androidlauncher.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.honzikv.androidlauncher.database.dao.*
import com.honzikv.androidlauncher.model.*

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

    abstract fun homescreenPageDao() : PageDao

    abstract fun themeProfileDao(): ThemeProfileDao
}