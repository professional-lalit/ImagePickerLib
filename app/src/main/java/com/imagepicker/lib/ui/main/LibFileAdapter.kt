package com.imagepicker.lib.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.imagepicker.lib.R
import com.imagepicker.lib.ui.explorer.DirectoryFragment
import com.imagepicker.lib.models.LibFile
import com.imagepicker.lib.common.ViewHolder

/**
 * This file is created by Lalit N. Hajare on 2/4/2020.
 */
class LibFileAdapter(
    private var mList: ArrayList<LibFile>,
    private var mCallback: (LibFile, DirectoryFragment.Ops) -> Unit
) :
    RecyclerView.Adapter<ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return LibFileViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_file_layout,
                null,
                false
            ), mCallback
        )
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindView(mList[position])
    }

}