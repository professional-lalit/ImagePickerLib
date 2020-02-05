package com.imagepicker.lib.ui

import android.os.Bundle
import com.imagepicker.lib.R
import com.imagepicker.lib.utils.toast
import kotlinx.android.synthetic.main.activity_test.*
import java.io.File


class TestActivity : ImageSupportActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        img_profile.setOnClickListener {
            showImageDialogue()
        }
    }

    override fun imageLoaded(file: File) {
        toast("Image loaded !!!")
    }


}
