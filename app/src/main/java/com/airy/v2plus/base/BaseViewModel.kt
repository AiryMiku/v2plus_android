package com.airy.v2plus.base

import android.util.Log
import androidx.lifecycle.*
import com.airy.v2plus.App
import com.airy.v2plus.showToastShort
import com.google.gson.JsonParseException
import kotlinx.coroutines.*
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException


/**
 * Created by Airy on 2019-11-08
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

open class BaseViewModel : ViewModel() {

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }

    val error: MutableLiveData<Throwable> = MutableLiveData()

    inline fun <T> launchOnViewModelScope(crossinline block: suspend () -> LiveData<T>): LiveData<T> {
        return liveData(viewModelScope.coroutineContext + Dispatchers.IO) {
            emitSource(block())
        }
    }

    private suspend fun baseLaunch(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
        finalBlock: suspend CoroutineScope.() -> Unit
    ): Job {
        return viewModelScope.launch {
            try {
                tryBlock()
            } catch (e: Throwable) {
                catchBlock(e)
            } finally {
                finalBlock()
            }
        }
    }

    fun launchOnIO(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit = { t ->
            handleException(t as Exception)
        },
        finalBlock: suspend CoroutineScope.() -> Unit = {}
    ): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            baseLaunch(tryBlock, catchBlock, finalBlock)
        }
    }

    fun launchOnMain(
        tryBlock: suspend CoroutineScope.() -> Unit,
        catchBlock: suspend CoroutineScope.(Throwable) -> Unit = {},
        finalBlock: suspend CoroutineScope.() -> Unit = {}
    ): Job {
        return viewModelScope.launch(Dispatchers.Main) {
            baseLaunch(tryBlock, catchBlock, finalBlock)
        }
    }

    open fun handleException(e: Exception) {
//        Logger.e(e, createLogMessage(e))
        Timber.e(e, createLogMessage(e))
        error.postValue(e)
        when (e) {  //todo
            is ConnectException -> {
                // 连接失败
                App.getAppContext().showToastShort("Connect failed")
            }
            is SocketTimeoutException -> {
                // 请求超时
                App.getAppContext().showToastShort("Timeout")
            }
            is JsonParseException -> {
                // 数据解析错误
                App.getAppContext().showToastShort("JsonParse failed")
            }
            is CancellationException -> {
                // 取消了 job
            }
            else -> {
                // 其他错误
                e.message?.let { App.getAppContext().showToastShort(it) }
            }
        }
    }


    private fun createLogMessage(
        e: Throwable?
    ): String {
        return "Class: ${this.javaClass.simpleName}\n" +
                "Trace: ${Log.getStackTraceString(e)}"
    }
}