package com.app.frimline.Common

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.toolbox.ImageRequest
import com.app.frimline.R
import java.io.File
import java.io.FileOutputStream
import android.os.AsyncTask



const val READ_EXTERNAL_STORAGE_PERMISSION = 2004

var downloadUrl = "http://www.africau.edu/images/default/sample.pdf";
private const val outputDir = "Android11Permissions"
private const val REQ_DOWNLOAD_FILE = 3
val storagePermission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

class InvoiceDownloadHelper(var act: Activity) {
    var invoiceLink="";

    fun downloadImage(invoiceLink: String) {
        this.invoiceLink =invoiceLink;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (permissionToReadWrite) {
                downloadImageToDownloadFolder()
            } else {
                permissionForReadWrite()
            }

        } else {
            downloadImageToDownloadFolder()
        }
    }

    private fun downloadImageToAppFolder() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (permissionToReadWrite) {
                downloadToAppFolder()
            } else {
                permissionForReadWrite()
            }

        } else {
            downloadToAppFolder()
        }
    }

    //Downloading file to Internal Folder
    private fun downloadToAppFolder() {
        try {
            val file = File(
                act.getExternalFilesDir(
                    null
                ), "test2.pdf"
            )

            if (!file.exists())
                file.createNewFile()

            var fileOutputStream: FileOutputStream? = null

            fileOutputStream = FileOutputStream(file)
            val bitmap = (ContextCompat.getDrawable(act, R.drawable.logo_demo) as BitmapDrawable).bitmap

            bitmap?.compress(Bitmap.CompressFormat.PNG, 80, fileOutputStream)
            Toast.makeText(
                act,"Success" + file.absolutePath,
                Toast.LENGTH_LONG
            ).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var permissionToReadWrite: Boolean = false
        get() {
            val permissionGrantedResult: Int = ContextCompat.checkSelfPermission(
                act,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            )
            return permissionGrantedResult == PackageManager.PERMISSION_GRANTED
        }

    //Request Permission For Read Storage
    private fun permissionForReadWrite() {
        ActivityCompat.requestPermissions(
            act,
            arrayOf(
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ), READ_EXTERNAL_STORAGE_PERMISSION
        )
    }

    private fun downloadImageToDownloadFolder() {
        val mgr = act.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val downloadUri = Uri.parse(invoiceLink)
        val request = DownloadManager.Request(
            downloadUri
        )
        request.setAllowedNetworkTypes(
            DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE
        )
            .setAllowedOverRoaming(false).setTitle("Invoice Download")
            .setDescription("order invoice download")
            .setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                "test1.pdf"
            )

        Toast.makeText(
            act,
            "Downloaded successfully to ${downloadUri?.path}",
            Toast.LENGTH_LONG
        ).show()

        mgr.enqueue(request)





    }

}