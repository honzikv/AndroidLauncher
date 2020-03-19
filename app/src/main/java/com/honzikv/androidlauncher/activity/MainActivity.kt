package com.honzikv.androidlauncher.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.data.LauncherData
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    lateinit var launcherData : LauncherData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launcherData = LauncherData(this)
        setContentView(R.layout.activity_main)
        Timber.d("contentView set")
        Timber.d("launcherData set")
    }
}
