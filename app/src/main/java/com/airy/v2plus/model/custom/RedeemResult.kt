package com.airy.v2plus.model.custom


/**
 * Created by Airy on 2019-12-06
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

data class RedeemResult(val status: Status = Status.FAILED, val messages: List<String> = emptyList()) {
    enum class Status {
        SUCCESS, FAILED, HASREDEEM
    }
}