package com.imagepicker.lib.ui.explorer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.imagepicker.lib.models.CustomFileModel

/**
 * This file is created by Lalit N. Hajare on 1/30/2020.
 */

class FolderViewModel : ViewModel() {

    var mCurrentDir = MutableLiveData<CustomFileModel>()
    val mChosenFileList = MutableLiveData<ArrayList<CustomFileModel>>()

    override fun onCleared() {
        super.onCleared()
    }

}