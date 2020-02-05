package com.imagepicker.lib.utils

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.imagepicker.lib.R

/**
 * This file is created by Lalit N. Hajare on 2/4/2020.
 */

object FileIconProvider {

    fun getFileIcon(context: Context, name: String): Drawable {
        if (name.endsWith(".txt") || name.endsWith(".plain")) {
            return ContextCompat.getDrawable(
                context,
                R.drawable.ic_text
            )!!
        } else if (name.endsWith(".ppt")) {
            return ContextCompat.getDrawable(
                context,
                R.drawable.ic_ppt
            )!!
        } else if (name.endsWith(".pdf")) {
            return ContextCompat.getDrawable(
                context,
                R.drawable.ic_pdf
            )!!
        } else if (name.endsWith(".cfg") || name.endsWith(".config")) {
            return ContextCompat.getDrawable(
                context,
                R.drawable.ic_config
            )!!
        } else if (name.endsWith(".mp3") || name.endsWith(".wav") || name.endsWith(".mp4")
            || name.endsWith(".3gp")
        ) {
            return ContextCompat.getDrawable(
                context,
                R.drawable.ic_music_note
            )!!
        } else if (name.endsWith(".png") || name.endsWith(".jpeg") || name.endsWith(".jpg")
            || name.endsWith(".giff")
        ) {
            return ContextCompat.getDrawable(
                context,
                R.drawable.ic_photo
            )!!

        }  else if (name.endsWith(".pdf")) {
            return ContextCompat.getDrawable(
                context,
                R.drawable.ic_pdf
            )!!
        } else {
            return ContextCompat.getDrawable(
                context,
                R.drawable.ic_unknown
            )!!

        }
    }

}