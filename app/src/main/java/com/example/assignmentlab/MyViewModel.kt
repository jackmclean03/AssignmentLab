package com.example.assignmentlab

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    private val _valueDate = MutableLiveData<String>()

    val value: MutableLiveData<String>
        get() = _valueDate

    init {
        _valueDate.value = "No Date Selected"
    }
}