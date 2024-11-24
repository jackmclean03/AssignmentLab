package com.example.assignmentlab

import android.app.Dialog
import android.content.Context.MODE_APPEND
import android.content.Context.MODE_PRIVATE
import android.content.DialogInterface
import android.icu.util.Output
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class Page2Fragment : Fragment() {
    lateinit var viewModel: MyViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = activity?.run {
            ViewModelProvider(this)[MyViewModel::class.java]
        } ?: throw Exception("Invalid Activity")
        val v = inflater.inflate(R.layout.page2_fragment, container, false)
        val valueView = v.findViewById<TextView>(R.id.textView)
        val valueObserver = Observer<String> { newValue ->
            valueView.text = newValue.toString()
        }
        val noteValue = v.findViewById<EditText>(R.id.editNote)
        v.findViewById<Button>(R.id.clearTextBut).setOnClickListener() {
            noteValue.setText("")
        }

        val closeDialogButton = { dialog: DialogInterface, which: Int ->
            dialog.dismiss()
        }

        fun checkNoteLimit(): Boolean {
            val limit = 20
            val storageState = Environment.getExternalStorageState()
            if (storageState == Environment.MEDIA_MOUNTED || storageState == Environment.MEDIA_MOUNTED_READ_ONLY) {
                val file = File(context?.getExternalFilesDir(null), "notes.txt")
                if (file.exists() && file.length() > 0) {
                    val fileInput = FileInputStream(file)
                    val inputStream = InputStreamReader(fileInput)
                    val inputBuffer = CharArray(2000)
                    val readLength = inputStream.read(inputBuffer)
                    val inputString = String(inputBuffer, 0, readLength)
                    val noteArray = inputString?.split("\n\n")?.filter { it.isNotBlank() }
                    if (noteArray != null) {
                        return if (noteArray.size - 2 >= limit) {
                            true
                        } else {
                            false
                        }
                    }

                }
            }
            return false
        }

        v.findViewById<Button>(R.id.addNoteToSDButton).setOnClickListener() {
            try {
                val storageState = Environment.getExternalStorageState()
                val dateValue = valueView.text.toString()
                val noteValue = noteValue.text.toString()
                val fullNote = "$dateValue \n$noteValue \n\n"
                if (dateValue != "No Date Selected") {
                    if (noteValue != "") {
                        println(dateValue)
                        if (storageState == Environment.MEDIA_MOUNTED) {
                            val file = File(context?.getExternalFilesDir(null), "notes.txt")
                            val fos = FileOutputStream(file, true)
                            val outputStream = OutputStreamWriter(fos)
                            var full = checkNoteLimit()
                            if (!full) {
                                outputStream.write(fullNote)
                                outputStream.flush()
                                outputStream.close()
                                fos.close()
                            } else {
                                val message = context?.let { it1 -> AlertDialog.Builder(it1) }
                                message?.setTitle("Too Many Notes!")
                                message?.setMessage("To add another note you must clear your notes first!")
                                message?.setPositiveButton(
                                    "OK",
                                    DialogInterface.OnClickListener(function = closeDialogButton)
                                )
                                message?.create()?.show()
                            }
                        }
                    } else {
                        val message = context?.let { it1 -> AlertDialog.Builder(it1) }
                        message?.setTitle("No Text Entered!")
                        message?.setMessage("You must input some text to store a note")
                        message?.setPositiveButton(
                            "OK",
                            DialogInterface.OnClickListener(function = closeDialogButton)
                        )
                        message?.create()?.show()
                    }
                } else {
                    val message = context?.let { it1 -> AlertDialog.Builder(it1) }
                    message?.setTitle("No Date Selected!")
                    message?.setMessage("You must pick a date to store a note!")
                    message?.setPositiveButton(
                        "OK",
                        DialogInterface.OnClickListener(function = closeDialogButton)
                    )
                    message?.create()?.show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        viewModel.value.observe(viewLifecycleOwner, valueObserver)
        return v;
    }
}