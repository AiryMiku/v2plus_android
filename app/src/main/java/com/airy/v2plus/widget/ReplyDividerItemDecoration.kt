package com.airy.v2plus.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.marginStart
import androidx.recyclerview.widget.RecyclerView
import com.airy.v2plus.R

/**
 * Created by Airy on 2020/9/25
 * Mail: a532710813@gmail.com
 * Github: AiryMiku
 */
class ReplyDividerItemDecoration : RecyclerView.ItemDecoration() {

    private val paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.GRAY
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
        val itemCount = parent.adapter?.itemCount
        for (i in 0..parent.childCount) {
            val child = parent.getChildAt(i)
            val index = parent.getChildAdapterPosition(child)
            //                    c.drawText(index.toString(), child.left.toFloat(), child.top.toFloat(), paint)
            if (child != null) {
                val userName = child.findViewById<TextView>(R.id.user_name)
                if (userName != null) {
                    if (index != 0 && itemCount != null && index != itemCount - 1) {
                        val sX = (userName.left + parent.marginStart).toFloat()
                        val sY = child.bottom.toFloat()
                        val eX = child.right.toFloat()
                        val eY = child.bottom.toFloat()
                        c.drawLine(sX, sY, eX, eY, paint)
                    }
                }
            }
        }
    }
}