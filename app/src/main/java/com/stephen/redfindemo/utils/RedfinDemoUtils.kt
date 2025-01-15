package com.stephen.redfindemo.utils

import android.widget.Toast
import com.stephen.redfindemo.base.appContext

fun showSimpleToast(text: String) {
    Toast.makeText(appContext, text, Toast.LENGTH_LONG).show()
}