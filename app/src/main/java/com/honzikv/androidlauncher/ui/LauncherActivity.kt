package com.honzikv.androidlauncher.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.data.first.launch.FirstLaunchInitializer
import com.honzikv.androidlauncher.user.settings.UserSettings
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.context.KoinContextHandler.get

/**
 * This application uses single activity architecture
 */
class LauncherActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
    }

}
