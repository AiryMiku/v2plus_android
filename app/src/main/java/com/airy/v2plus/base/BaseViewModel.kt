package com.airy.v2plus.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope


/**
 * Created by Airy on 2019-11-08
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class BaseViewModel: ViewModel() {

    val error: MutableLiveData<Throwable> = MutableLiveData()



    suspend fun runBaseCoroutine(processBlock: suspend CoroutineScope.() -> Unit,
                         catchBlock: suspend CoroutineScope.(Throwable) -> Unit,
                         finalBlock: suspend CoroutineScope.() -> Unit) {
        coroutineScope {
            try {
                processBlock()
            } catch (e: Throwable) {
                catchBlock(e)
            } finally {
                finalBlock()
            }
        }


    }

}