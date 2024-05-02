package com.example.caltrack.customviews

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import com.example.caltrack.R

class CustomEditText(context: Context, attrs: AttributeSet?) : AppCompatEditText(context, attrs) {
    private var sethint: String? = null
    private var setcolor = false

    var darkButton: Drawable? = null
    var lightButton: Drawable? = null

    fun init() {
        darkButton = ResourcesCompat.getDrawable(resources, R.drawable.ic_clear_black_24dp, null)
        lightButton = ResourcesCompat.getDrawable(resources, R.drawable.ic_clear_black_22dp, null)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                showButton()
                show()
            }

            override fun afterTextChanged(s: Editable) {}
        })
    }

    fun show() {
        setOnTouchListener { view, motionEvent ->
            var isclicked: Boolean = false

            val clearButtonstart = (width - paddingEnd - darkButton!!.intrinsicWidth).toFloat()
            if (motionEvent.x > clearButtonstart) {
                isclicked = true
            }

            if (isclicked) {
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> text!!.clear()
                    MotionEvent.ACTION_UP -> hideButton()
                }
            }
            true
        }
    }

    fun showButton() {
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, darkButton, null)
    }

    fun hideButton() {
        setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, lightButton, null)
    }

    init {
        val tarry: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditText)
        try {
            sethint = tarry.getString(R.styleable.CustomEditText_setHint)

            if (sethint == null)
                setHint("Enter yout message")
            else
                setHint(sethint)

            setcolor = tarry.getBoolean(R.styleable.CustomEditText_setColor, false)

            if (setcolor)
                setTextColor(Color.MAGENTA)
        } finally {
            tarry.recycle()
            init()
        }
    }
}