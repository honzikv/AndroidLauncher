package com.honzikv.androidlauncher.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.data.SystemDataRepository
import com.honzikv.androidlauncher.ui.viewmodel.SystemDataViewModel
import com.honzikv.androidlauncher.utils.InjectorUtils
import timber.log.Timber

class LauncherActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
    }
}
