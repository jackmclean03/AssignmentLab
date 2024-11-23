package com.example.assignmentlab

import android.content.Context.MODE_APPEND
import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class Page3Fragment : Fragment() {
    lateinit var viewModel: MyViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProvider(this)[MyViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        val v = inflater.inflate(R.layout.page3_fragment, container, false)

        fun displaySDNotes() {
            val storageState = Environment.getExternalStorageState()
            if (storageState == Environment.MEDIA_MOUNTED || storageState == Environment.MEDIA_MOUNTED_READ_ONLY) {
                val file = File(context?.getExternalFilesDir(null), "notes.txt")
                val fileInput = FileInputStream(file)
                val inputStream = InputStreamReader(fileInput)
                val inputBuffer = CharArray(128)
                inputStream.read(inputBuffer)
                val inputString = String(inputBuffer)
                val textView = v.findViewById<TextView>(R.id.textView2)
                textView.text = "$inputString"
                inputStream.close()
                fileInput.close()

            }
        }

        v.findViewById<Button>(R.id.clearSDNoteBut).setOnClickListener() {
            val storageState = Environment.getExternalStorageState()
            if (storageState == Environment.MEDIA_MOUNTED) {
                val file = File(context?.getExternalFilesDir(null), "notes.txt")
                val fos = FileOutputStream(file)
                val outputStream = OutputStreamWriter(fos)
                outputStream.write("")
                outputStream.flush()
                outputStream.close()
                fos.close()
                displaySDNotes()
            }

        }

        v.findViewById<Button>(R.id.displaySDNoteBut).setOnClickListener() {
            displaySDNotes()
        }
        return v
    }
}