package com.imagepicker.lib.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import com.imagepicker.lib.models.LibFile

class PageViewModel : ViewModel() {

    private val _index = MutableLiveData<Int>()
    val text: LiveData<String> = map(_index) {
        "Hello world from section: $it"
    }

    fun setIndex(index: Int) {
        _index.value = index
    }

    private val mChosenFileList = MutableLiveData<ArrayList<LibFile>>()
    val fileList: LiveData<ArrayList<LibFile>> = mChosenFileList

    fun addFile(file: LibFile) {
        if (mChosenFileList.value == null) {
            mChosenFileList.value = ArrayList()
        }
        if (!mChosenFileList.value!!.contains(file)) {
            val list = mChosenFileList.value
            list!!.add(file)
            mChosenFileList.postValue(list)
        }
    }

    fun removeFile(file: LibFile) {
        if (mChosenFileList.value != null && mChosenFileList.value!!.contains(file)) {
            val list = mChosenFileList.value
            list!!.remove(file)
            mChosenFileList.postValue(list)
        }
    }
}