package com.airy.v2plus.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Created by Airy on 2020/9/25
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
class ReplyDividerItemDecoration: RecyclerView.ItemDecoration() {

    private val paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.RED
            textSize = 64f
            strokeWidth = 1f
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        for (i in 0..parent.childCount) {
            val child = parent.getChildAt(i)
            child?.let {
                val index = parent.getChildAdapterPosition(child)
                if (index != 0) {
//                    c.drawText(index.toString(), child.left.toFloat(), child.top.toFloat(), paint)
                    c.drawLine(child.left.toFloat() + 100, child.bottom.toFloat(), child.right.toFloat(), child.bottom.toFloat(), paint)
                }
            }
        }
    }
}