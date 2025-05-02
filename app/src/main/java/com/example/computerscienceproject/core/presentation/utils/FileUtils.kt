package com.example.computerscienceproject.core.presentation.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.content.FileProvider
import java.io.File


fun getUriOfFile(context: Context,file: File): Uri {
    val uriFile = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".fileprovider", file)
    return uriFile!!
}

fun getBytesFromUri(context: Context,uri: Uri): ByteArray? {
    // Use a try-catch block to handle possible exceptions
    return try {
        // Use the content resolver to open an input stream from the uri
        val inputStream = context.contentResolver?.openInputStream(uri)
        // Use the readBytes() extension function to read the bytes from the input stream
        inputStream?.readBytes()
    } catch (e: Exception) {
        // Log the error or handle it appropriately
        Log.e("Error", "Failed to get bytes from uri: ${e.message}")
        null
    }
}

@SuppressLint("Range")
fun getFileNameFromUri(context : Context, uri: Uri): String {
    try {
        // Get the content resolver from the context
        val resolver = context.contentResolver

        val cursor = resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)
        // Get the file name from the cursor

        return cursor?.use {
            // Move the cursor to the first row
            if (it.moveToFirst()) {
                // Get the file name as a string
                it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            } else {
                null
            }
        } ?: ""
    }catch (e:Exception){
        return ""
    }
}

fun getIntentToSelectFile() : Intent {
    return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        // Set the type to pdf
        type = "image/*"
        // Add a category to show the system file picker
        addCategory(Intent.CATEGORY_OPENABLE)
    }
}

fun getFileFromUri(context: Context,fileUri: Uri,fileName: String) : File {

    val file = File(context.cacheDir, fileName)

    val inputStream = context.contentResolver.openInputStream(fileUri)?.use { inputStream ->
        file.outputStream().use { outputStream ->
            inputStream.copyTo(outputStream)
        }
    }

    return file
}