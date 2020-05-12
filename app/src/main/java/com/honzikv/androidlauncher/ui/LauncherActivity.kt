package com.honzikv.androidlauncher.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.data.repository.AppDrawerRepository
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * This application uses single activity architecture
 */
class LauncherActivity : AppCompatActivity() {

    private val appDrawerRepository: AppDrawerRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Load apps for drawer so it doesnt stutter on first launch of the drawer
        lifecycleScope.launch { appDrawerRepository.reloadAppList() }
        setContentView(R.layout.activity_launcher)
    }

}
