package com.imagepicker.lib.utils

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/**
 * This file is created by Lalit N. Hajare on 1/30/2020.
 */
object FileHandler {

    fun handlePath(context: Context, file: File): Uri {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Uri.fromFile(file)
        } else {
            FileProvider.getUriForFile(context, context.packageName, file)
        }
    }

    fun createTempFile(activity: AppCompatActivity, name: String, ext: String): File {
        val dirOut = activity.cacheDir
        return File.createTempFile(name, ext, dirOut)
    }

    fun createTempFileExternal(activity: AppCompatActivity, name: String, ext: String): File {
        val dirOut = activity.getExternalFilesDir(null)
        return File.createTempFile(name, ext, dirOut)
    }

    fun writeBitmapToFile(file: File, bmp: Bitmap) {
        try {
            FileOutputStream(file).use { out ->
                bmp.compress(Bitmap.CompressFormat.JPEG, 100, out) // bmp is your Bitmap instance
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun writeBitmapToFile(file: File, bmp: Bitmap, cb: () -> Unit) {
        try {
            FileOutputStream(file).use { out ->
                bmp.compress(Bitmap.CompressFormat.PNG, 90, out) // bmp is your Bitmap instance
            }
            cb.invoke()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun copyFileFromContentUri(
        contentResolver: ContentResolver,
        uri: Uri,
        imgFile: File,
        cb: () -> Unit
    ) {
        val inputStream = contentResolver.openInputStream(uri)
        val outputStream = imgFile.outputStream()
        val buffer = ByteArray(inputStream?.available()!!)
        inputStream.read(buffer)
        outputStream.write(buffer)
        cb.invoke()
    }

    fun copyFile(inFile: File, outFile: File, cb: () -> Unit) {
        val inputStream = inFile.inputStream()
        val outputStream = outFile.outputStream()
        val buffer = ByteArray(inputStream.available())
        inputStream.read(buffer)
        outputStream.write(buffer)
        cb.invoke()
    }

}