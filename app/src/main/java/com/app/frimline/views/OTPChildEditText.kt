package com.app.frimline.views

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.app.frimline.R

internal class OTPChildEditText : androidx.appcompat.widget.AppCompatEditText {

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        isCursorVisible = false
        setTextColor(ContextCompat.getColor(context, R.color.transparent))
        //setBackgroundDrawable(null)
        background = null
        inputType = InputType.TYPE_CLASS_NUMBER
        setSelectAllOnFocus(false)
        setTextIsSelectable(false)
    }

    public override fun onSelectionChanged(start: Int, end: Int) {

        val text = text
        text?.let { text1 ->
            if (start != text1.length || end != text1.length) {
                setSelection(text1.length, text1.length)
                return
            }
        }

        super.onSelectionChanged(start, end)
    }

}