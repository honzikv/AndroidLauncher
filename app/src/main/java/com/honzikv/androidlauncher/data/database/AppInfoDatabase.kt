package com.honzikv.androidlauncher.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.honzikv.androidlauncher.data.database.dao.DockDao
import com.honzikv.androidlauncher.data.database.dao.FolderDao
import com.honzikv.androidlauncher.data.database.dao.UserAppDao
import com.honzikv.androidlauncher.data.model.entity.DockEntity

/**
 * Room database to store user edited data about homescreen
 */
@Database(
    entities = [SystemAppModel::class, FolderEntity::class, DockEntity::class, FolderEntity::class],
    version = 1
)
abstract class AppInfoDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var instance: AppInfoDatabase? = null

        fun getInstance(context: Context): AppInfoDatabase {
            synchronized(this) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppInfoDatabase::class.java,
                        "app_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                }
                return instance as AppInfoDatabase
            }
        }
    }

    abstract fun appDao(): AppDao

    abstract fun folderDao(): FolderDao

    abstract fun dockDao(): DockDao

    abstract fun userAppDao(): UserAppDao
}