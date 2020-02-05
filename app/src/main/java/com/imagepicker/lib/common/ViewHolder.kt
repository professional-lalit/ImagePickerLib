package com.imagepicker.lib.common

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/**
 * This file is created by Lalit N. Hajare on 1/30/2020.
 */

abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun onBindView(model: Any)
}