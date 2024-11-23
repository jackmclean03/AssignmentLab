package com.example.assignmentlab

import android.content.Context.MODE_APPEND
import android.content.Context.MODE_PRIVATE
import android.icu.util.Output
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import java.io.File
import java.io.FileOutputStream
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

        v.findViewById<Button>(R.id.addNoteToSDButton).setOnClickListener() {
            try {
                val storageState = Environment.getExternalStorageState()
                val dateValue = valueView.text.toString()
                val noteValue = noteValue.text.toString()
                val fullNote = "$dateValue \n$noteValue \n\n"
                if (storageState == Environment.MEDIA_MOUNTED) {
                    val file = File(context?.getExternalFilesDir(null), "notes.txt")
                    val fos = FileOutputStream(file, true)
                    val outputStream = OutputStreamWriter(fos)
                    outputStream.write(fullNote)
                    outputStream.flush()
                    outputStream.close()
                    fos.close()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        viewModel.value.observe(viewLifecycleOwner, valueObserver)
        return v;
    }
}