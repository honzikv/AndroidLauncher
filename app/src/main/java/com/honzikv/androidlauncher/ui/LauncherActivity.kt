package com.honzikv.androidlauncher.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.repository.DrawerRepository
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * This application uses single activity architecture
 */
class LauncherActivity : AppCompatActivity() {

    private val drawerRepository: DrawerRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Load apps for drawer so it does not stutter on first launch of the drawer
        lifecycleScope.launch { drawerRepository.reloadAppList() }
        setContentView(R.layout.activity_launcher)
    }

}
