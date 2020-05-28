package com.honzikv.androidlauncher.utils

import java.lang.Exception

/**
 * Vyjimka pri mazani posledni stranky
 */
class IntegrityException(message: String) : Exception(message)