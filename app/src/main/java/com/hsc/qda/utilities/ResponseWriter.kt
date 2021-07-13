package com.hsc.qda.utilities

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File
import java.io.FileWriter

class ResponseWriter {
    companion object {
        fun newResponse(name: String) {
            try {
                val fileName = Environment.getExternalStorageDirectory()
                    .absolutePath + "/" + name
                val file = File(fileName)
                if (file.exists()) {
                    file.delete()
                }
            } catch (e: Exception) {
                Log.d("Error", e.localizedMessage!!.toString())
            }
        }

        fun writeResponse(response: String, name: String) {
            try {
                val fileName = Environment.getExternalStorageDirectory()
                    .absolutePath + "/" + name
                val fw = FileWriter(fileName, true)
                fw.write(response + "\n\n")
                fw.close()
            } catch (e: Exception) {
                Log.d("Error", e.localizedMessage!!.toString())
            }
        }

        fun openResponseFolder(context: Context) {
            val intent = Intent(Intent.ACTION_VIEW)
            val responseDir = Uri.parse("file://${Environment.getExternalStorageDirectory().absolutePath}")
            intent.setDataAndType(responseDir, "application/*")
            context.startActivity(intent)
        }
    }
}