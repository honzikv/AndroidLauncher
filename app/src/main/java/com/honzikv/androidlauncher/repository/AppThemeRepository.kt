package com.honzikv.androidlauncher.repository

import android.content.SharedPreferences
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.honzikv.androidlauncher.database.dao.ThemeProfileDao
import com.honzikv.androidlauncher.model.ThemeProfileModel
import com.honzikv.androidlauncher.utils.applyAlpha

/**
 * Trida, slouzici jako repository pro tema aplikace - obsahuje funkce pro zmenu a pridani novych
 * profilu
 */
class AppThemeRepository(
    private val preferences: SharedPreferences,
    private val themeProfileDao: ThemeProfileDao
) {

    companion object {

        /**
         * Vychozi tema v aplikaci
         */
        val lightTheme = ThemeProfileModel(
            name = "Light Theme",
            drawerBackgroundColor = Color.parseColor("#ecf0f1"),
            drawerTextFillColor = Color.parseColor("#2c3e50"),
            drawerSearchBackgroundColor = Color.parseColor("#70a1ff"),
            drawerSearchTextColor = Color.parseColor("#34495e"),
            dockTextColor = Color.parseColor("#2c3e50"),
            dockBackgroundColor = applyAlpha(Color.parseColor("#7f8c8d"), 200),
            switchBackgroundColor = Color.parseColor("#34495e"),
            switchThumbColorOn = Color.parseColor("#1abc9c"),
            switchThumbColorOff = Color.parseColor("#ecf0f1")
        )

        /**
         * Field v UserSettings
         */
        const val APP_THEME_FIELD = "appTheme"
    }

    /**
     * Aktualni tema v aplikaci
     */
    private val currentTheme = MutableLiveData(loadTheme())

    /**
     * LiveData se vsemi profily
     */
    val allThemes = themeProfileDao.getAllProfilesLiveData()

    /**
     * LiveData s aktualnim profilem
     */
    fun getCurrentTheme(): LiveData<ThemeProfileModel> = currentTheme

    /**
     * Nacte zvolene tema, pokud neexistuje vytvori [lightTheme]
     */
    private fun loadTheme(): ThemeProfileModel {
        return Gson().fromJson(
            preferences.getString(APP_THEME_FIELD, Gson().toJson(lightTheme)),
            ThemeProfileModel::class.java
        )
    }

    /**
     * Funkce pro zmenu tema - nastavi field [APP_THEME_FIELD] na hodnotu [theme]
     */
    fun changeTheme(theme: ThemeProfileModel) {
        preferences.edit().apply {
            putString(APP_THEME_FIELD, Gson().toJson(theme))
            apply()
        }
        currentTheme.postValue(theme)
    }

    /**
     * Prida profil do databaze. Funkci je nutno volat pomoci coroutine, protoze manipuluje s db
     */
    suspend fun addProfile(vararg profile: ThemeProfileModel) = profile.forEach {
        themeProfileDao.addProfile(it)
    }
}