package com.airy.v2plus.ui.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airy.v2plus.App
import com.airy.v2plus.showToastShort
import com.google.gson.JsonParseException
import kotlinx.coroutines.*
import java.net.ConnectException
import java.net.SocketTimeoutException


/**
 * Created by Airy on 2019-11-08
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

open class BaseViewModel: ViewModel() {

    @ExperimentalCoroutinesApi
    override fun onCleared() {
        viewModelScope.cancel()
        super.onCleared()
    }

    val error: MutableLiveData<Throwable> = MutableLiveData()

    private suspend fun baseLaunch(tryBlock: suspend CoroutineScope.() -> Unit,
                                   catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
                                   finalBlock: suspend CoroutineScope.() -> Unit): Job {
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

    fun launchOnIO(tryBlock: suspend CoroutineScope.() -> Unit,
                           catchBlock: suspend CoroutineScope.(Throwable) -> Unit = { t-> handleException(t as Exception)},
                           finalBlock: suspend CoroutineScope.() -> Unit = {}): Job {
        return viewModelScope.launch(Dispatchers.IO) {
            baseLaunch(tryBlock, catchBlock, finalBlock)
        }
    }

    fun launchOnMain(tryBlock: suspend CoroutineScope.() -> Unit,
                           catchBlock: suspend CoroutineScope.(Throwable) -> Unit = {},
                           finalBlock: suspend CoroutineScope.() -> Unit = {}): Job {
        return viewModelScope.launch(Dispatchers.Main) {
            baseLaunch(tryBlock, catchBlock, finalBlock)
        }
    }

    private fun handleException(e: Exception){
        Log.e(this.javaClass.simpleName, e.message, e)
//        error.postValue(e)
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
            else -> {
                // 其他错误
                e.message?.let { App.getAppContext().showToastShort(it) }
            }
        }
    }

}