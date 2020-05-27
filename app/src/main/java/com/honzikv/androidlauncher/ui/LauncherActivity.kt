package com.honzikv.androidlauncher.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.honzikv.androidlauncher.R
import com.honzikv.androidlauncher.repository.DrawerRepository
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * Aplikace pouziva pouze jednu aktivitu a vymenuje fragmenty. Aktivita bezi dokud se aplikace neukonci
 */
class LauncherActivity : AppCompatActivity() {

    /**
     * DrawerRepository pro nacteni dat
     */
    private val drawerRepository: DrawerRepository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Nacteni vsech aplikaci v pozadi
        lifecycleScope.launch { drawerRepository.reloadAppList() }
        //Nastavi layout s NavHostFragmentem, ktery zajisti HomescreenFragment a prechody
        setContentView(R.layout.activity_launcher)
    }

}
