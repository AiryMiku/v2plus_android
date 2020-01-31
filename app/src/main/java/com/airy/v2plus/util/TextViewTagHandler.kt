package com.airy.v2plus.util

import android.content.Context
import android.text.Editable
import android.text.Html
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.view.View
import android.widget.Toast
import org.xml.sax.XMLReader
import java.util.*


/**
 * Created by Airy on 2020-01-08
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */

class TextViewTagHandler(private val context: Context): Html.TagHandler {
    override fun handleTag(
        opening: Boolean,
        tag: String?,
        output: Editable?,
        xmlReader: XMLReader?
    ) {
        if (tag?.toLowerCase(Locale.getDefault()).equals("img")) {
            output?.let {
                val len = it.length
                val images: Array<ImageSpan> = output.getSpans(len - 1, len, ImageSpan::class.java)
                val imageUrl = images.first().source
                imageUrl?.let {
                    output.setSpan(
                        ClickableImage(
                            context,
                            imageUrl
                        ), len-1, len, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }
    }

    private class ClickableImage(private val context: Context,
                                 private val url: String) : ClickableSpan() {
        override fun onClick(widget: View) {
            Toast.makeText(context, url, Toast.LENGTH_SHORT).show()
        }
    }
}