package com.stephen.redfindemo.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.TextView
import com.stephen.redfindemo.R

class CommonDialog(
    context: Context,
    val titleResId: Int,
    private val listener: OnDialogButonClickListener
) : Dialog(context), OnClickListener {

    private lateinit var btnConfirm: Button
    private lateinit var btnCancel: Button
    private lateinit var tvTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_commondialog)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        setCancelable(false)

        btnCancel = findViewById(R.id.btn_dialog_cancel)
        btnConfirm = findViewById(R.id.btn_dialog_confirm)
        btnConfirm.setOnClickListener(this)
        btnCancel.setOnClickListener(this)
        tvTitle = findViewById(R.id.tv_dialog_title)
    }

    override fun show() {
        super.show()
        tvTitle.text = context.getString(titleResId)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_dialog_confirm -> listener.onConfirmClick(this)
            R.id.btn_dialog_cancel -> listener.onCancelClick(this)
        }
    }

    interface OnDialogButonClickListener {
        fun onConfirmClick(dialog: CommonDialog)
        fun onCancelClick(dialog: CommonDialog)
    }
}