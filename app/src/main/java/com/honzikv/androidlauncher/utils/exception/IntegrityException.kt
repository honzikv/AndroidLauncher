package com.honzikv.androidlauncher.utils.exception

import java.lang.Exception

/**
 * Vyjimka pri mazani posledni stranky
 */
class IntegrityException(message: String) : Exception(message)