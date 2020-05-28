package com.honzikv.androidlauncher.utils

/**
 * Jednoducha trida, ktera slouzi jako callback pri necekane udalosti v coroutine - napr. pri mazani
 * posledni stranky. Tato trida se musi zabalit do LiveData a pote ji lze pozorovat a cist.
 */
class Event<out T>(private val eventMessage: T) {
    /**
     * Zda-li byla udalost zpracovana
     */
    private var wasEventHandled = false

    /**
     * Vrati obsah, pokud ho jiz nekdo neprecetl
     */
    fun getContentIfNotHandled(): T? = if (wasEventHandled) {
        null
    } else {
        wasEventHandled = true
        eventMessage
    }

}