package com.airy.v2plus.util

import android.widget.Toast
import com.airy.v2plus.V2plusApp.Companion.getAppContext


/**
 * Created by Airy on 2019-09-19
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

fun showShort(message: CharSequence) {
    Toast.makeText(getAppContext(), message, Toast.LENGTH_SHORT).show()
}

fun showLong(message: CharSequence) {
    Toast.makeText(getAppContext(), message, Toast.LENGTH_LONG).show()
}