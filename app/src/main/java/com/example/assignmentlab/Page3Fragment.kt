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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class Page3Fragment : Fragment() {
    lateinit var viewModel: MyViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProvider(this)[MyViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        val v = inflater.inflate(R.layout.page3_fragment, container, false)

        fun sortNewestToOldest() {

            val storageState = Environment.getExternalStorageState()
            if (storageState == Environment.MEDIA_MOUNTED || storageState == Environment.MEDIA_MOUNTED_READ_ONLY) {
                val file = File(context?.getExternalFilesDir(null), "notes.txt")
                if (file.exists() && file.length() > 0) {
                    val fileInput = FileInputStream(file)
                    val inputStream = InputStreamReader(fileInput)
                    val inputBuffer = CharArray(2000)
                    val readLength = inputStream.read(inputBuffer)
                    val inputString = String(inputBuffer, 0, readLength)
                    val textView = v.findViewById<TextView>(R.id.textView2)

                    val stringArray = inputString.split("\n\n").filter { it.isNotBlank() }
                    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.UK)

                    val sortedNotes = stringArray.sortedByDescending { note ->
                        val dateString = note.substringBefore("\n").trim()
                        LocalDate.parse(dateString, dateFormatter)
                    }

                    textView.text = "$sortedNotes"
                }
            } else {
                val textView = v.findViewById<TextView>(R.id.textView2)
                textView.text = "Notes are empty"
            }
        }

        fun sortOldestToNewest() {
            val storageState = Environment.getExternalStorageState()
            if (storageState == Environment.MEDIA_MOUNTED || storageState == Environment.MEDIA_MOUNTED_READ_ONLY) {
                val file = File(context?.getExternalFilesDir(null), "notes.txt")
                if (file.exists() && file.length() > 0) {
                    val fileInput = FileInputStream(file)
                    val inputStream = InputStreamReader(fileInput)
                    val inputBuffer = CharArray(2000)
                    val readLength = inputStream.read(inputBuffer)
                    val inputString = String(inputBuffer, 0, readLength)
                    val textView = v.findViewById<TextView>(R.id.textView2)

                    val stringArray = inputString.split("\n\n").filter { it.isNotBlank() }
                    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.UK)

                    val sortedNotes = stringArray.sortedBy { note ->
                        val dateString = note.substringBefore("\n").trim()
                        LocalDate.parse(dateString, dateFormatter)
                    }

                    textView.text = "$sortedNotes"
                } else {
                    val textView = v.findViewById<TextView>(R.id.textView2)
                    textView.text = "Notes are empty"
                }
            }

        }

        fun displaySDNotes() {
            val storageState = Environment.getExternalStorageState()
            if (storageState == Environment.MEDIA_MOUNTED || storageState == Environment.MEDIA_MOUNTED_READ_ONLY) {
                val file = File(context?.getExternalFilesDir(null), "notes.txt")
                if (file.exists() && file.length() > 0) {
                    val fileInput = FileInputStream(file)
                    val inputStream = InputStreamReader(fileInput)
                    val inputBuffer = CharArray(2000)
                    val readLength = inputStream.read(inputBuffer)
                    val inputString = String(inputBuffer, 0, readLength)
                    val textView = v.findViewById<TextView>(R.id.textView2)
                    textView.text = "$inputString"

                    inputStream.close()
                    fileInput.close()
                } else {
                    val textView = v.findViewById<TextView>(R.id.textView2)
                    textView.text = "Notes are empty"
                }

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
                val textView = v.findViewById<TextView>(R.id.textView2)
                textView.text = "Notes are empty"
            }

        }

        v.findViewById<Button>(R.id.displaySDNoteBut).setOnClickListener() {
            displaySDNotes()
        }

        v.findViewById<Button>(R.id.sortNewestBut).setOnClickListener() {
            sortNewestToOldest()
        }

        v.findViewById<Button>(R.id.sortOldestBut).setOnClickListener() {
            sortOldestToNewest()
        }

        return v
    }
}