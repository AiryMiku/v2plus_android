package com.airy.v2plus.ui.base

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*


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
                                   finalBlock: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch {
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
                           finalBlock: suspend CoroutineScope.() -> Unit = {}) {
        viewModelScope.launch(Dispatchers.IO) {
            baseLaunch(tryBlock, catchBlock, finalBlock)
        }
    }

    fun launchOnMain(tryBlock: suspend CoroutineScope.() -> Unit,
                           catchBlock: suspend CoroutineScope.(Throwable) -> Unit = {},
                           finalBlock: suspend CoroutineScope.() -> Unit = {}) {
        viewModelScope.launch(Dispatchers.Main) {
            baseLaunch(tryBlock, catchBlock, finalBlock)
        }
    }

    fun handleException(e: Exception){
        Log.e(this.javaClass.simpleName, e.message, e)
        error.postValue(e)
//        when (e) {  //todo
//            is HttpException -> {
//            }
//            else -> {
//            }
//        }
    }

}