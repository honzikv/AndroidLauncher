package com.honzikv.androidlauncher.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.data.first.launch.FirstLaunchInitializer
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.core.context.KoinContextHandler.get

class LauncherActivity : AppCompatActivity() {

    private val initializer: FirstLaunchInitializer by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO probably launch somewhere else
        lifecycleScope.launch {
            if (!initializer.isAppInitialized()) {
                initializer.initialize()
            }
        }

        setContentView(R.layout.activity_launcher)
    }

}
