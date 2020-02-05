package com.imagepicker.lib.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * This file is created by Lalit N. Hajare on 2/4/2020.
 */

fun Any?.getClassName(): String {
    return this!!::class.java.simpleName
}

fun <T : AppCompatActivity> AppCompatActivity.beginActivity(activityClass: Class<T>) {
    startActivity(Intent(this, activityClass))
}

fun <T : AppCompatActivity> AppCompatActivity.beginActivityForResult(
    activityClass: Class<T>,
    reqCode: Int
) {
    startActivityForResult(Intent(this, activityClass), reqCode)
}

fun Context.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}