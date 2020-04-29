package com.honzikv.androidlauncher.data.repository

import android.content.SharedPreferences
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.honzikv.androidlauncher.data.database.dao.ThemeProfileDao
import com.honzikv.androidlauncher.data.model.entity.ThemeProfileModel
import timber.log.Timber

val DEFAULT_THEME = ThemeProfileModel(
    id = null,
    drawerBackgroundColor = Color.parseColor("#dfe6e9"),
    drawerTextFillColor = Color.parseColor("#2d3436"),
    drawerSearchBackgroundColor = Color.parseColor("#0984e3"),
    drawerSearchTextColor = Color.parseColor("#636e72"),
    dockBackgroundColor = Color.parseColor("#dfe6e9"),
    dockTextColor = Color.parseColor("#2d3436"),
    isUserProfile = false,
    name = "Light Theme"
)

const val APP_THEME_FIELD = "appTheme"

class AppThemeRepository(
    private val preferences: SharedPreferences,
    private val themeProfileDao: ThemeProfileDao
) {

    private val currentTheme = MutableLiveData(loadTheme())

    val allThemes = themeProfileDao.getAllProfiles()

    private fun loadTheme(): ThemeProfileModel {
        return Gson().fromJson(
            preferences.getString(APP_THEME_FIELD, Gson().toJson(DEFAULT_THEME)),
            ThemeProfileModel::class.java
        )
    }

    fun getCurrentTheme(): LiveData<ThemeProfileModel> = currentTheme

    fun changeTheme(theme: ThemeProfileModel) {
        Timber.d("Attempting to change theme")
        preferences.edit().apply{
            putString(APP_THEME_FIELD, Gson().toJson(theme))
            apply()
            Timber.d("Theme changed to ${theme.name}")
        }
        currentTheme.postValue(theme)
    }

    suspend fun addProfile(vararg profile: ThemeProfileModel) = profile.forEach {
        val profileId = themeProfileDao.addProfile(it)
        Timber.d("New profile added, id=$profileId")
    }
}