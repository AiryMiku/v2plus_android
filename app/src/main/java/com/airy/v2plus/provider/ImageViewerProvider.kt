package com.airy.v2plus.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import com.airy.v2plus.BuildConfig
import com.airy.v2plus.util.MiscUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import java.io.File
import java.io.IOException

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ImageViewerProvider: ContentProvider() {
    override fun onCreate(): Boolean = true

    override fun getType(uri: Uri): String? {
        return "application/octet-stream"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException("No external inserts")
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        var innerProjection = projection
        val file = getFileForUri(uri)

        if (innerProjection == null) {
            innerProjection = COLUMNS
        }

        var cols = arrayOfNulls<String>(innerProjection.size)
        var values = arrayOfNulls<Any>(innerProjection.size)
        var i = 0
        for (col in innerProjection) {
            when (col) {
                OpenableColumns.DISPLAY_NAME -> {
                    cols[i] = OpenableColumns.DISPLAY_NAME
                    values[i++] = file.name
                }
                OpenableColumns.SIZE -> {
                    cols[i] = OpenableColumns.SIZE
                    values[i++] = file.length()
                }
            }
        }

        cols = cols.sliceArray(0 until i)
        values = values.sliceArray(0 until i)

        val cursor = MatrixCursor(cols, 1)
        cursor.addRow(values)
        return cursor
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("No external updates")
    }

    override fun delete(uri: Uri, selection: String?,
                        selectionArgs: Array<String>?): Int {
        throw UnsupportedOperationException("No external deletes")
    }

    override fun openFile(uri: Uri, mode: String): ParcelFileDescriptor? {
        val file = getFileForUri(uri)
        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
    }

    private fun getFileForUri(uri: Uri): File {
        val path = uri.encodedPath

        if (path != "/image" || tempPath == null) {
            throw IllegalArgumentException("Invalid file path")
        }

        var file = File(tempPath)
        try {
            file = file.canonicalFile
        } catch (e: IOException) {
            throw IllegalArgumentException("Failed to resolve canonical path for $file")
        }

        return file
    }

    companion object {
        private const val AUTHORITY = BuildConfig.APPLICATION_ID + ".viewer"
        var tempPath: String? = null

        private val COLUMNS = arrayOf(OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE)

        fun getUriForFile(): Uri {
            return Uri.Builder()
                .scheme("content")
                .authority(AUTHORITY)
                .encodedPath("image").build()
        }

        fun viewImage(context: Context, url: String) {
            val innerUrl = MiscUtil.formatUrl(url)
            Glide.with(context)
                .downloadOnly()
                .load(innerUrl)
                .into(object: CustomTarget<File>() {
                    override fun onLoadCleared(placeholder: Drawable?) { }
                    override fun onResourceReady(resource: File, transition: Transition<in File>?) {
                        tempPath = resource.canonicalPath

                        val uri = getUriForFile()
                        val intent = MiscUtil.getViewImageIntent(context, uri)
                        context.startActivity(intent)
                    }
                })
        }
    }
}