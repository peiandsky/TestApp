package com.zlcdg.libandroid.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText

fun View.visiable() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun View.invisiable() {
    visibility = View.INVISIBLE
}


fun EditText.string(): String {
    return text.toString().trim()
}

fun EditText.setTextChangeListener(changeText: (String) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable) {
            changeText.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}