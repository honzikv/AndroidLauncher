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
 * https://stackoverflow.com/questions/47374580/how-can-i-perform-livedata-transformations-on-a-background-thread
 */
object BackgroundTransformations {

    fun <X, Y> map(
        source: LiveData<X>,
        mapFunction: (X) -> Y
    ): LiveData<Y> {
        val result = MediatorLiveData<Y>()

        result.addSource(source, Observer { x ->
            if (x == null) return@Observer
            CoroutineScope(Dispatchers.Default).launch {
                result.postValue(mapFunction(x))
            }
        })

        return result
    }

    fun <X, Y> switchMap(
        source: LiveData<X>,
        switchMapFunction: (X) -> LiveData<Y>
    ): LiveData<Y> {
        val result = MediatorLiveData<Y>()
        result.addSource(source, object : Observer<X> {
            var mSource: LiveData<Y>? = null

            override fun onChanged(x: X) {
                if (x == null) {
                    return
                }

                CoroutineScope(Dispatchers.Default).launch {
                    val newLiveData = switchMapFunction(x)
                    if (mSource == newLiveData) {
                        return@launch
                    }
                    if (mSource != null) {
                        result.removeSource(mSource!!)
                    }
                    mSource = newLiveData
                    if (mSource != null) {
                        result.addSource(mSource!!) { y ->
                            result.setValue(y)
                        }
                    }
                }
            }
        })
        return result
    }

}