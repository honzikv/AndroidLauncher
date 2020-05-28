package com.honzikv.androidlauncher.utils

/**
 * Jednoducha trida, ktera slouzi jako callback pri necekane udalosti v coroutine - napr. pri mazani
 * posledni stranky. Tato trida se musi zabalit do LiveData a pote ji lze pozorovat a cist.
 */
open class Event<out T>(private val content: T) {

    /**
     * Zda-li byla udalost zpracovana
     */
    private var hasBeenHandled = false

    /**
     * Vrati obsah, pokud ho jiz nekdo neprecetl
     */
    fun getContentIfNotHandledOrReturnNull(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

}