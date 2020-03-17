package com.honzikv.androidlauncher.data

import android.content.Intent
import android.graphics.drawable.Drawable

/**
 * Class that represents app in launcher - contains details of app, intent to run the app and so on
 */
open class LauncherApp {

    private lateinit var packageName: String

    /**
     * Text seen on label of icon of the app
     */
    private lateinit var iconLabel: String

    /**
     * Icon image
     */
    private lateinit var iconDrawable: Drawable

    private lateinit var intent: Intent

}