package com.honzikv.androidlauncher.data.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ThemeProfileDto(
    @PrimaryKey
    val id: Long,

    val drawerBackgroundcolor: Int,

    val drawerTextFillColor: Int,

    val drawerSearchColor: Int,

    val drawerSearchTextColor: Int,

    val dockBackgroundColor: Int,

    val isSelected: Boolean = false,

    val isUserProfile: Boolean = true
)