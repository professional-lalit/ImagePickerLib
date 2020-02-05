package com.imagepicker.lib.models

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

/**
 * This file is created by Lalit N. Hajare on 2/4/2020.
 */

data class LibFile(
    val uri: Uri,
    var isSelected: Boolean,
    val name: String?,
    val duration: Int,
    val size: Int
)
