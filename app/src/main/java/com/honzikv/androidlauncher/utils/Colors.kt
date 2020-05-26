package com.honzikv.androidlauncher.utils

import android.graphics.Color
import androidx.annotation.ColorInt

fun toAlpha(@ColorInt color: Int, alpha: Int): Int =
    Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))