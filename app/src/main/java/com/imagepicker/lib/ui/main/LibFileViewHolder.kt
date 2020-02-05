package com.imagepicker.lib.ui.main

/**
 * This file is created by Lalit N. Hajare on 2/4/2020.
 */

import android.media.ThumbnailUtils
import android.provider.MediaStore
import android.util.Size
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toFile
import com.imagepicker.lib.R
import com.imagepicker.lib.ui.explorer.DirectoryFragment
import com.imagepicker.lib.models.LibFile
import com.imagepicker.lib.utils.FileIconProvider
import com.imagepicker.lib.common.ViewHolder
import com.squareup.picasso.Picasso
import java.io.File

class LibFileViewHolder(
    itemView: View,
    private var mCallback: (LibFile, DirectoryFragment.Ops) -> Unit
) :
    ViewHolder(itemView) {

    private val imgFile = itemView.findViewById<ImageView>(R.id.img_file)
    private val txtFileName = itemView.findViewById<TextView>(R.id.txt_file_name)
    private val txtFileDesc = itemView.findViewById<TextView>(R.id.txt_file_desc)
    private val imgTick = itemView.findViewById<ImageView>(R.id.img_tick)

    override fun onBindView(model: Any) {
        val file = model as LibFile

        if (file.isSelected) {
            imgTick.visibility = View.VISIBLE
        } else {
            imgTick.visibility = View.INVISIBLE
        }

        if (!file.name.isNullOrEmpty()) {
            Picasso.get().load(file.uri).resize(100, 100).into(imgFile)
//            imgFile.setImageDrawable(FileIconProvider.getFileIcon(itemView.context, file.name))
            txtFileName.text = file.name
        }

        txtFileDesc.text = file.size.toString()

        itemView.setOnClickListener {
            if (imgTick.visibility == View.INVISIBLE || imgTick.visibility == View.GONE)
                mCallback(file, DirectoryFragment.Ops.ADD)
            else
                mCallback(file, DirectoryFragment.Ops.REMOVE)
        }
    }

}