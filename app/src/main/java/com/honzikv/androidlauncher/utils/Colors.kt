package com.honzikv.androidlauncher.utils

import android.graphics.Color
import androidx.annotation.ColorInt

/**
 * Jednoducha funkce pro zmenu alpha kanalu barvy. Ignoruje predchozi nastaveni alpha kanalu -
 * tzn. z 50% alpha cervene pri nastaveni alpha = 255 vytvori nepruhlednou cervenou
 */
fun applyAlpha(@ColorInt color: Int, alpha: Int): Int =
    Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color))