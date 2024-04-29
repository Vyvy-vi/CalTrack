package com.example.caltrack

import android.content.Context
import android.widget.Toast
import com.google.gson.Gson

class InternalStorageHelper(private val context: Context) {
    private val gson = Gson()
    fun writeFile(fileName:String, data: Any) {
        try {
            val jsonData = gson.toJson(data)
            val fOut = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            fOut.write(jsonData.toByteArray())
            fOut.close()
            showToast("File updated successfully")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun readFile(fileName: String, data: Any): Any {
        var jsonString = ""
        var obj = data
        try {
            val fIn = context.openFileInput(fileName)
            var c: Int
            val temp = StringBuilder()

            while (fIn.read().also { c = it } != -1) {
                temp.append(c.toChar())
            }
            jsonString = temp.toString()
            println(jsonString)
            fIn.close()
            showToast("Data fetched from file successfully")
            obj = gson.fromJson(jsonString, data::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return obj
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
