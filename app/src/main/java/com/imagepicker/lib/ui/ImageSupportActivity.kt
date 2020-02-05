package com.imagepicker.lib.ui

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import com.imagepicker.lib.R
import com.imagepicker.lib.ui.main.HomeActivity
import com.imagepicker.lib.utils.CircleTransform
import com.imagepicker.lib.utils.FileHandler
import com.imagepicker.lib.utils.beginActivityForResult
import com.imagepicker.lib.utils.toast
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import kotlinx.android.synthetic.main.activity_test.*
import java.io.File

/**
 * This file is created by Lalit N. Hajare on 2/5/2020.
 */

private const val REQ_CAMERA_INTENT = 78
private const val REQ_CAMERA_PERM = 12
private const val REQ_IMAGE_GALLERY = 37

abstract class ImageSupportActivity : AppCompatActivity() {

    private var _cameraImageFile: File? = null

    protected fun showImageDialogue() {
        val view = layoutInflater.inflate(R.layout.dialogue_image_selection, null)

        val dialogue = Dialog(this)
        dialogue.setContentView(view)

        val rgImageOptions = dialogue.findViewById<RadioGroup>(R.id.rg_image_options)
        val btnProceed = dialogue.findViewById<Button>(R.id.btn_proceed)

        btnProceed.setOnClickListener {
            when (rgImageOptions.checkedRadioButtonId) {
                R.id.rb_camera -> {
                    getCameraPerms()
                    dialogue.cancel()
                }
                R.id.rb_gallery -> {
                    beginActivityForResult(HomeActivity::class.java, REQ_IMAGE_GALLERY)
                    dialogue.cancel()
                }
            }
        }

        dialogue.show()
    }

    private fun getCameraPerms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), REQ_CAMERA_PERM)
            } else {
                startCamera()
            }
        } else {
            startCamera()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty()) {
            if (permissions[0] == Manifest.permission.CAMERA && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //access camera
                startCamera()
            } else {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), REQ_CAMERA_PERM)
            }
        }
    }

    private fun startCamera() {
        _cameraImageFile = FileHandler.createTempFileExternal(this, "profile_pic", ".jpg")
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            FileProvider.getUriForFile(
                this
                , applicationContext.packageName + ".utils.GenericFileProvider"
                , _cameraImageFile!!
            )
        )
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivityForResult(cameraIntent, REQ_CAMERA_INTENT)
    }

    private fun startCropping(file: File) {
        CropImage.activity(Uri.fromFile(file))
            .setGuidelines(CropImageView.Guidelines.ON)
            .start(this)
    }

    abstract fun imageLoaded(file: File)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_CAMERA_INTENT && resultCode == Activity.RESULT_OK) {
            startCropping(_cameraImageFile!!)
        } else if (requestCode == REQ_IMAGE_GALLERY && resultCode == Activity.RESULT_OK) {
            val uriList = ArrayList<Uri>()
            if (data?.getSerializableExtra("uri-list") != null) {
                uriList.addAll(data.getSerializableExtra("uri-list") as ArrayList<Uri>)
                if (uriList.isNotEmpty()) {
                    val imgFile = FileHandler.createTempFile(this, "profile_pic", "jpg")
                    FileHandler.copyFileFromContentUri(contentResolver, uriList[0], imgFile) {
                        startCropping(imgFile)
                    }
                }
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                val resultUri = result.uri
                when {
                    resultUri.toString().startsWith("content") -> {
                        val imgFile = FileHandler.createTempFile(this, "profile_pic", ".jpg")
                        FileHandler.copyFileFromContentUri(contentResolver, resultUri, imgFile) {
                            Picasso.get().load(imgFile).transform(CircleTransform())
                                .into(img_profile)
                            imageLoaded(imgFile)
                        }

                    }
                    resultUri.toString().startsWith("file") -> {
                        val inFile = resultUri.toFile()
                        val outFile = FileHandler.createTempFile(this, "profile_pic", ".jpg")
                        FileHandler.copyFile(inFile, outFile) {
                            Picasso.get().load(outFile).transform(CircleTransform())
                                .into(img_profile)
                            imageLoaded(outFile)
                        }
                    }
                    else -> {
                        toast("Unknown uri scheme")
                    }
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                toast(error?.localizedMessage!!)
            }
        }
    }


}