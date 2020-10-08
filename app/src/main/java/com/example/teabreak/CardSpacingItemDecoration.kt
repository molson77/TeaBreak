package com.example.teabreak

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * CardSpacingItemDecoration:
 *
 * @desc Class that sets the spacing between the CardViews displayed in the RecyclerView.
 *
 * @property vSpace the uniform vertical space between the
 * CardViews and the top/bottom of the RecyclerView
 */

class CardSpacingItemDecoration(val vSpace: Int): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        // if first in the list, add space on top of the CardView
        if(parent.getChildLayoutPosition(view) == 0) {
            outRect.top = vSpace
        }
        outRect.bottom = vSpace
    }

}