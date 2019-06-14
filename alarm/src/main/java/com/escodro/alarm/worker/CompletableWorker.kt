package com.escodro.alarm.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.escodro.core.extension.applySchedulers
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import org.koin.core.KoinComponent
import timber.log.Timber
import java.util.concurrent.LinkedBlockingQueue

/**
 * Abstract [Worker] class to encapsulate the [Completable] logic.
 */
abstract class CompletableWorker(context: Context, params: WorkerParameters) :
    Worker(context, params), KoinComponent {

    private val compositeDisposable = CompositeDisposable()

    override fun doWork(): Result {
        Timber.d("doWork")

        val result = LinkedBlockingQueue<Result>()
        val disposable = getObservable().applySchedulers().subscribe(
            {
                onSuccess()
                result.put(Result.success())
            },
            {
                onError(it)
                result.put(Result.failure())
            }
        )
        compositeDisposable.add(disposable)

        return try {
            result.take()
        } catch (e: InterruptedException) {
            Result.retry()
        }
    }

    override fun onStopped() {
        Timber.d("onStopped")
        compositeDisposable.clear()
    }

    /**
     * Returns the [Completable] observable to be executed.
     *
     * @return the [Completable] observable to be executed
     */
    abstract fun getObservable(): Completable

    /**
     * Function executed when the observable is successfully executed.
     */
    abstract fun onSuccess()

    /**
     * Function executed when the observable has failed.
     *
     * @param error the error from the observable process
     */
    abstract fun onError(error: Throwable)
}
