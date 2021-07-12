package com.github.heyalex.cornersheet.behavior

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import com.github.heyalex.CornerDrawer
import com.github.heyalex.cornersheet.interpolate
import com.github.heyalex.cornersheet.interpolateArgb
import com.google.android.material.bottomsheet.BottomSheetBehavior

class CornerSheetHeaderBehavior<V : CornerDrawer> : CornerSheetBehavior<V> {
    constructor() : super()
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    var peekHeightListener: OnPeekHeightListener? = null

    @ColorInt
    private var contentColor: Int = 0

    @ColorInt
    private var headerColor: Int = 0

    private var container: FrameLayout? = null
    private var header: View? = null
    private var content: View? = null

    private var bottomInset: Int = 0
    private var topInset: Int = 0

    override fun initLayoutChild(view: V) {
        super.initLayoutChild(view)
        header = view.header
        content = view.content
        container = view.container
        contentColor = view.contentColor
        headerColor = view.headerColor
        topInset = view.topInset
        bottomInset = view.bottomInset

        val height = header?.height ?: 0
        val width = header?.width ?: 0
        if (peekHeight == PEEK_HEIGHT_AUTO) {
            peekHeight = height + bottomInset
        }
        if (expandedWidth == 0) {
            expandedWidth = width
        }
        onPeekHeightChanged(peekHeight)
        sheetBackground?.tintList = null
        onStartState()
    }

    private fun onStartState() {
        if (state == BottomSheetBehavior.STATE_HALF_EXPANDED || state == BottomSheetBehavior.STATE_EXPANDED) {
            container?.alpha = 1f
            header?.alpha = 0f
            sheetBackground?.fillColor = ColorStateList.valueOf(contentColor)
            sheetBackground?.interpolation = 0f
            header?.visibility = View.GONE
            content?.visibility = View.VISIBLE
            if(state != BottomSheetBehavior.STATE_HALF_EXPANDED) {
                container?.translationY = topInset.toFloat()
            }
        } else {
            sheetBackground?.fillColor = ColorStateList.valueOf(headerColor)
            header?.visibility = View.VISIBLE
            content?.visibility = View.GONE
            container?.alpha = 0f
            header?.alpha = 1f
            content?.visibility = View.GONE
        }
    }

    override fun internalOnSlide(slideOffset: Float) {
        super.internalOnSlide(slideOffset)
        sheetBackground?.interpolation =
            interpolate(
                1f,
                0f,
                0f,
                0.15f,
                slideOffset
            )
        sheetBackground?.fillColor = ColorStateList.valueOf(
            interpolateArgb(
                headerColor,
                contentColor,
                0f,
                halfExpandedRatio,
                slideOffset
            )
        )

        header?.alpha = interpolate(
            1f,
            0f,
            0f,
            0.15f,
            slideOffset
        )
        header?.visibility = if (slideOffset < 0.5) View.VISIBLE else View.GONE
        content?.visibility = if (slideOffset > 0.2) View.VISIBLE else View.GONE
        container?.alpha = interpolate(
            0f,
            1f,
            0.2f,
            halfExpandedRatio - 0.1f,
            slideOffset
        )

        container?.translationY = interpolate(
            0f,
            topInset.toFloat(),
            halfExpandedRatio - 0.1f,
            1f,
            slideOffset
        )
    }

    fun onPeekHeightChanged(newPeekHeight: Int) {
        peekHeightListener?.onPeekHeightChanged(newPeekHeight)
    }

    interface OnPeekHeightListener {
        fun onPeekHeightChanged(newPeekHeight: Int)
    }
}