package com.honzikv.androidlauncher.utils

import android.content.SharedPreferences
import androidx.lifecycle.LiveData

//Prevzato z https://gist.github.com/er-abhishek-luthra/9c2962b4c6b3df6b76135393244324b3
/**
 * Umoznuje vytvorit LiveData, ktere sleduje SharedPreferences hodnoty
 */
abstract class SharedPreferenceLiveData<T>(
    val sharedPrefs: SharedPreferences,
    private val key: String,
    private val defValue: T
) : LiveData<T>() {

    init {
        value = this.getValueFromPreferences(key, defValue)
    }

    private val preferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == this.key) {
                value = getValueFromPreferences(key, defValue)
            }
        }

    abstract fun getValueFromPreferences(key: String, defValue: T): T

    override fun onActive() {
        super.onActive()
        value = getValueFromPreferences(key, defValue)
        sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onInactive() {
        sharedPrefs.unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
        super.onInactive()
    }
}


class SharedPreferenceBooleanLiveData(
    sharedPrefs: SharedPreferences,
    key: String,
    defValue: Boolean
) : SharedPreferenceLiveData<Boolean>(sharedPrefs, key, defValue) {
    override fun getValueFromPreferences(key: String, defValue: Boolean): Boolean =
        sharedPrefs.getBoolean(key, defValue)
}

fun SharedPreferences.booleanLiveData(key: String, defValue: Boolean) =
    SharedPreferenceBooleanLiveData(this, key, defValue)