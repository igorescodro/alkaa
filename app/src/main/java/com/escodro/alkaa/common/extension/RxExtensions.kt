package com.escodro.alkaa.common.extension

import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Extension to apply the default schedulers to the [Observable].
 */
fun <T> Observable<T>.applySchedulers(): Observable<T> =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

/**
 * Extension to apply the default schedulers to the [Flowable].
 */
fun <T> Flowable<T>.applySchedulers(): Flowable<T> =
    subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
