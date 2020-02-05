package com.imagepicker.lib.ui.main

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.imagepicker.lib.R
import com.imagepicker.lib.ui.explorer.FileChooserActivity
import com.imagepicker.lib.models.CustomFileModel
import com.imagepicker.lib.models.LibFile
import com.imagepicker.lib.utils.beginActivityForResult
import java.io.File

class HomeActivity : AppCompatActivity() {

    private val REQ_FILE_ACCESS = 23
    private val REQ_FILE_EXPLORER = 78

    lateinit var mPageViewModel: PageViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        mPageViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(PageViewModel::class.java)

        initActionBar()
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
                initSections()
            }
        } else {
            initSections()
        }
    }


    private fun initSections() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
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
            initSections()
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finishAndPassResult()
            return true
        } else if (item.itemId == R.id.action_folder_search) {
            beginActivityForResult(FileChooserActivity::class.java, REQ_FILE_EXPLORER)
        }
        return super.onOptionsItemSelected(item)
    }

    fun finishAndPassResult() {
        val intent = Intent()
        val uriList = arrayListOf<Uri>()
        mPageViewModel.fileList.value?.forEach { file ->
            uriList.add(file.uri)
        }
        intent.putExtra("uri-list", uriList)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_FILE_EXPLORER) {
            if (resultCode == Activity.RESULT_OK) {
                val fileList = ArrayList<CustomFileModel>()
                val bundle = data?.getBundleExtra("file-bundle")
                if (bundle?.getSerializable("file-list") != null) {
                    fileList.addAll(bundle.getSerializable("file-list") as ArrayList<CustomFileModel>)
                    for (file in fileList) {
                        val libFile = LibFile(file.toUri(), true, file.name, 0, 0)
                        mPageViewModel.addFile(libFile)
                    }
                }
                setResult(resultCode)
                //Call only when single selection is required
                finishAndPassResult()
            }
        }
    }
}