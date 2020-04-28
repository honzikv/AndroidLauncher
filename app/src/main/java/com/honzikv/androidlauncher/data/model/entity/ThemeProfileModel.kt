package com.honzikv.androidlauncher.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

data class ThemeProfileModel(

    val drawerBackgroundColor: Int,

    val drawerTextFillColor: Int,

    val drawerSearchBackgroundColor: Int,

    val drawerSearchTextColor: Int,

    val dockBackgroundColor: Int,

    val dockTextColor: Int,

    var isSelected: Boolean = false,

    var name: String,

    val isUserProfile: Boolean = true
)