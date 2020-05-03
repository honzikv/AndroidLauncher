package com.honzikv.androidlauncher.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.honzikv.androidlauncher.ui.fragment.settings.adapter.Displayable

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
) : Displayable {

    override fun toString() = name
}