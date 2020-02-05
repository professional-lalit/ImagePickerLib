package com.imagepicker.lib.ui.explorer

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.imagepicker.lib.R
import com.imagepicker.lib.models.CustomFileModel
import com.imagepicker.lib.utils.DateFormatter
import com.imagepicker.lib.utils.FileIconProvider
import com.imagepicker.lib.common.ViewHolder
import java.util.*

/**
 * This file is created by Lalit N. Hajare on 1/30/2020.
 */
class FileViewHolder(
    itemView: View,
    private var mCallback: (CustomFileModel, DirectoryFragment.Ops) -> Unit
) :
    ViewHolder(itemView) {

    private val imgFile = itemView.findViewById<ImageView>(R.id.img_file)
    private val txtFileName = itemView.findViewById<TextView>(R.id.txt_file_name)
    private val txtFileDesc = itemView.findViewById<TextView>(R.id.txt_file_desc)
    private val imgTick = itemView.findViewById<ImageView>(R.id.img_tick)

    override fun onBindView(model: Any) {
        val file = model as CustomFileModel

        if (file.isSelected) {
            imgTick.visibility = View.VISIBLE
        } else {
            imgTick.visibility = View.INVISIBLE
        }

        if (file.isDirectory)
            imgFile.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.ic_folder
                )!!
            )
        else
            imgFile.setImageDrawable(FileIconProvider.getFileIcon(itemView.context, file.name))


        if (!file.name.isNullOrEmpty()) {
            txtFileName.text = file.name
        }
        if (!file.list().isNullOrEmpty()) {
            txtFileDesc.text = "(${file.list()?.size!!})"
        } else if (file.isDirectory) {
            txtFileDesc.text = "(0)"
        } else {
            txtFileDesc.text = "Last Modified : ${DateFormatter.getStringFromDate(
                Date(file.lastModified()),
                DateFormatter.dd_MM_yyyy_HH_mm
            )}"
        }

        itemView.setOnClickListener {
            if (file.listFiles() != null && file.listFiles()?.isNotEmpty()!! && file.isDirectory) {
                mCallback(file, DirectoryFragment.Ops.NO_OP)
            } else if (!file.isDirectory) {
                if (imgTick.visibility == View.INVISIBLE || imgTick.visibility == View.GONE)
                    mCallback(file, DirectoryFragment.Ops.ADD)
                else
                    mCallback(file, DirectoryFragment.Ops.REMOVE)
            }
        }
    }

}