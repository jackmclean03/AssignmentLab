package com.example.assignmentlab

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider

class Page1Fragment : Fragment() {
    lateinit var viewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = activity?.run {
            ViewModelProvider(this)[MyViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        val view = inflater.inflate(R.layout.page1_fragment, container, false)
        val supportFragmentManager = requireActivity().supportFragmentManager
        
        view.findViewById<Button>(R.id.pickDate).setOnClickListener() {
            val dateFragment = DatePickerFragment()
            dateFragment.show(supportFragmentManager, "datePicker")
        }
        supportFragmentManager.setFragmentResultListener(
            "REQUEST_KEY",
            viewLifecycleOwner
        ) { resultKey, bundle ->
            if (resultKey == "REQUEST_KEY") {
                val date = bundle.getString("REQUEST_CODE")
                viewModel.value.setValue(date)
            }
        }

        return view
    }
}