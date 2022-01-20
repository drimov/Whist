package com.example.whist.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView

const val verticalSpacingItemDecoratorValue: Int = 10

class VerticalSpacingItemDecorator(private val verticalSpaceHeight: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.bottom = verticalSpaceHeight
    }
}

fun addDecoration(recyclerView: RecyclerView, context: Context?) {
    recyclerView.addItemDecoration(
        DividerItemDecoration(
            context,
            LinearLayout.VERTICAL
        )
    )
    recyclerView.addItemDecoration(
        VerticalSpacingItemDecorator(
            verticalSpacingItemDecoratorValue
        )
    )
}

