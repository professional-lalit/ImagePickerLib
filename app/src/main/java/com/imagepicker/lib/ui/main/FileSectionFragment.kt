package com.imagepicker.lib.ui.main

import android.content.ContentUris
import android.database.Cursor
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.imagepicker.lib.R
import com.imagepicker.lib.ui.explorer.DirectoryFragment
import com.imagepicker.lib.models.LibFile
import com.imagepicker.lib.utils.getClassName


/**
 * A placeholder fragment containing a simple view.
 */
class FileSectionFragment : Fragment() {


    private lateinit var pageViewModel: PageViewModel

    enum class FragmentIndices(var index: Int) {
        IMAGES(1), VIDEOS(2), AUDIOS(3), DOCUMENTS(4);

        fun getValue(): Int {
            return index
        }
    }

    private lateinit var mAdapter: LibFileAdapter
    private lateinit var recyclerFiles: RecyclerView
    private val mFileList = arrayListOf<LibFile>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = (activity as HomeActivity).mPageViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_file_section, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter(view)
        when (arguments?.getInt(ARG_SECTION_NUMBER)) {
            FragmentIndices.IMAGES.getValue() -> {
                getImageFiles(null)
            }
            FragmentIndices.VIDEOS.getValue() -> {
                getVideoFiles(null)
            }
            FragmentIndices.AUDIOS.getValue() -> {
                getAudioFiles(null)
            }
            FragmentIndices.DOCUMENTS.getValue() -> {
                getDocFiles(null)
            }
        }
    }

    private fun setAdapter(view: View) {
        recyclerFiles = view.findViewById(R.id.recycler_files)
        recyclerFiles.layoutManager = LinearLayoutManager(context)
        mAdapter =
            LibFileAdapter(mFileList) { file, ops ->

                when (ops.ordinal) {
                    DirectoryFragment.Ops.ADD.ordinal -> {
                        file.isSelected = true
                        pageViewModel.addFile(file)

                        //Call only when single selection is required
                        (activity as HomeActivity).finishAndPassResult()
                    }
                    DirectoryFragment.Ops.REMOVE.ordinal -> {
                        file.isSelected = false
                        pageViewModel.removeFile(file)
                    }
                }

                mAdapter.notifyItemChanged(mFileList.indexOf(file))
            }
        recyclerFiles.adapter = mAdapter
    }

    private fun getImageFiles(filter: String?) {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
        )
        val query = activity!!.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        query?.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getInt(sizeColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                val libFile = LibFile(contentUri, false, name, 0, size)
                libFile.isSelected = checkIfSelected(libFile)
                mFileList += libFile
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    private fun checkIfSelected(file: LibFile): Boolean {
        if (pageViewModel.fileList.value != null)
            return pageViewModel.fileList.value!!.contains(file)
        return false
    }

    private fun getVideoFiles(filter: String?) {
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE
        )
        val query = activity!!.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        query?.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getInt(sizeColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                Log.d(getClassName(), " FILE URI : $contentUri")
                mFileList += LibFile(contentUri, false, name, 0, size)
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    private fun getAudioFiles(filter: String?) {
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.SIZE
        )
        val query = activity!!.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        query?.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val nameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE)

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val size = cursor.getInt(sizeColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    id
                )
                Log.d(getClassName(), " FILE URI : $contentUri")
                mFileList += LibFile(contentUri, false, name, 0, size)
            }
        }
        mAdapter.notifyDataSetChanged()
    }

    private fun getDocFiles(filter: String?) {
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.MIME_TYPE,
            MediaStore.Files.FileColumns.DATE_ADDED,
            MediaStore.Files.FileColumns.DATE_MODIFIED,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.SIZE
        )

        val mimeTypes =
            "'application/pdf','text/plain','application/msword','application/powerpoint'"

        val whereClause =
            MediaStore.Files.FileColumns.MIME_TYPE + " IN (" + mimeTypes + ")"
        val orderBy = MediaStore.Files.FileColumns.SIZE + " DESC"
        val query: Cursor = activity!!.contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            projection,
            whereClause,
            null,
            orderBy
        )!!

        query.use { cursor ->
            // Cache column indices.
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE)
            val mimeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE)

            while (cursor.moveToNext()) {
                // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                var name = cursor.getString(nameColumn)
                var mime = cursor.getString(mimeColumn)
                val size = cursor.getInt(sizeColumn)

                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Files.getContentUri("external"),
                    id
                )
                Log.d(getClassName(), " FILE URI : $contentUri")
                name = name ?: ""

                if (!mime.isNullOrEmpty())
                    name += ".$mime".replace("application/", "")
                        .replace("video/", "")
                        .replace("image/", "")
                        .replace("text/", "")

                mFileList += LibFile(contentUri, false, name, 0, size)
            }
        }
        query.close()

        mAdapter.notifyDataSetChanged()
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): FileSectionFragment {
            return FileSectionFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

}
