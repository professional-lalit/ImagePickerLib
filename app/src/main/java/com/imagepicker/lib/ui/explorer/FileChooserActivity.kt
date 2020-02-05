package com.imagepicker.lib.ui.explorer

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.imagepicker.lib.R
import com.imagepicker.lib.models.CustomFileModel
import java.io.File


class FileChooserActivity : AppCompatActivity() {
    val REQ_FILE_ACCESS = 232
    private lateinit var fileContainer: FrameLayout
    private lateinit var rootDir: File
    lateinit var mFolderViewModel: FolderViewModel

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_chooser)

        rootDir = Environment.getExternalStorageDirectory()
        initActionBar()
        setViews()
        getPerms()
    }


    private fun initActionBar() {
        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_left_arrow)
            setHomeButtonEnabled(true)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(true)
        }
    }


    private fun setViews() {
        fileContainer = findViewById(R.id.file_container)
        mFolderViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(FolderViewModel::class.java)
        mFolderViewModel.mCurrentDir.observe(this, Observer { currentDir ->
            if (currentDir.isDirectory)
                addFragment(currentDir)
        })
    }

    private fun getPerms() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ), REQ_FILE_ACCESS
                )
            } else {
                setRootFileDir()
            }
        } else {
            setRootFileDir()
        }
    }


    private fun setRootFileDir() {
        mFolderViewModel.mCurrentDir.postValue(CustomFileModel(rootDir.parentFile, rootDir.name))
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            && grantResults[1] == PackageManager.PERMISSION_GRANTED
        ) {
            setRootFileDir()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ), REQ_FILE_ACCESS
                )
            }
        }
    }

    private fun addFragment(currentDir: CustomFileModel) {
        val dirFragment =
            DirectoryFragment()
        dirFragment.arguments = Bundle()
        dirFragment.arguments?.putSerializable("dir", currentDir)
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
            .add(fileContainer.id, dirFragment)
            .addToBackStack(currentDir.name)
            .commit()
        supportActionBar?.title = currentDir.name
    }

    private fun popFragment() {
        supportFragmentManager.popBackStack()
        Handler().postDelayed({
            supportActionBar?.title = supportFragmentManager
                .getBackStackEntryAt(supportFragmentManager.fragments.size - 1)
                .name
        }, 200)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (supportFragmentManager.backStackEntryCount == 1) {
                finishAndPassResult()
            } else
                popFragment()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    fun finishAndPassResult() {
        val bundle = Bundle()
        bundle.putSerializable("file-list", mFolderViewModel.mChosenFileList.value)
        val intent = Intent()
        intent.putExtra("file-bundle", bundle)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }
}
