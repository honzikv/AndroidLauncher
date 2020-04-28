package com.honzikv.androidlauncher.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class ThemeProfileModel(

    @PrimaryKey(autoGenerate = true)
    val id: Long? = null,

    val drawerBackgroundColor: Int,

    val drawerTextFillColor: Int,

    val drawerSearchBackgroundColor: Int,

    val drawerSearchTextColor: Int,

    val dockBackgroundColor: Int,

    val dockTextColor: Int,

    var name: String,

    val isUserProfile: Boolean = true
)