package com.honzikv.androidlauncher.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Tato implementace by mela zabranit transformaci na hlavnim vlakne, ktera muze teoreticky zpomalit
 * vykon aplikace - misto na hlavnim vlakne provede transformaci v Coroutine
 */
object BackgroundTransformations {

    fun <X, Y> map(
        sourceLiveData: LiveData<X>,
        mappingFunction: (X) -> Y
    ): LiveData<Y> {
        val result = MediatorLiveData<Y>()
        result.addSource(sourceLiveData, Observer { x ->
            if (x == null) return@Observer
            CoroutineScope(Dispatchers.Default).launch {
                result.postValue(mappingFunction(x))
            }
        })

        return result
    }

}