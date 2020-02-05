package com.imagepicker.lib.ui.explorer


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imagepicker.lib.R

import com.imagepicker.lib.models.CustomFileModel


/**
 * A simple [Fragment] subclass.
 */
class DirectoryFragment : Fragment() {

    private lateinit var mDir: CustomFileModel
    private lateinit var mAdapter: FileAdapter
    private lateinit var recyclerFiles: RecyclerView
    private val mFileList = arrayListOf<CustomFileModel>()

    private lateinit var mFolderViewModel: FolderViewModel

    enum class Ops(opId: Int) {
        ADD(1), REMOVE(2), NO_OP(3)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_directory, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mFolderViewModel = (activity as FileChooserActivity).mFolderViewModel
        mDir = arguments?.getSerializable("dir") as CustomFileModel
        setAdapter(view)
        getDirectories()
    }

    private fun setAdapter(view: View) {
        recyclerFiles = view.findViewById(R.id.recycler_files)
        recyclerFiles.layoutManager = LinearLayoutManager(context)
        mAdapter =
            FileAdapter(mFileList) { file, ops ->


                when (ops.ordinal) {
                    Ops.ADD.ordinal -> {
                        var fileList = mFolderViewModel.mChosenFileList.value
                        if (fileList == null) {
                            fileList = ArrayList()
                        }
                        fileList.add(file)
                        mFolderViewModel.mChosenFileList.postValue(fileList)
                        file.isSelected = true

                        //Call only when single selection is required
                        (activity as FileChooserActivity).finishAndPassResult()
                    }
                    Ops.REMOVE.ordinal -> {
                        val fileList = mFolderViewModel.mChosenFileList.value
                        fileList?.remove(file)
                        mFolderViewModel.mChosenFileList.postValue(fileList)
                        file.isSelected = false
                    }
                    else -> {
                        mFolderViewModel.mCurrentDir.postValue(file)
                    }
                }

                mAdapter.notifyItemChanged(mFileList.indexOf(file))
            }
        recyclerFiles.adapter = mAdapter
    }


    private fun getDirectories() {
        if (mDir.exists() && mDir.listFiles() != null && mDir.listFiles()!!.isNotEmpty()) {
            for (file in mDir.listFiles()!!) {
                val customFileModel = CustomFileModel(file.parentFile, file.name)
                customFileModel.isSelected =
                    mFolderViewModel.mChosenFileList.value?.contains(customFileModel) == true
                mFileList.add(customFileModel)
            }
            mAdapter.notifyDataSetChanged()
        } else {
            Toast.makeText(context, "Folder does not exists", Toast.LENGTH_SHORT).show()
        }
    }


}
