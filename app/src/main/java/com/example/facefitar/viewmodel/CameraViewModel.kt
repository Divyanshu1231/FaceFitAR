package com.example.facefitar.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.facefitar.data.model.FilterModel
import com.example.facefitar.data.repository.FilterRepository

class CameraViewModel : ViewModel() {


    private val _filters = MutableLiveData<List<FilterModel>>()
    val filters: LiveData<List<FilterModel>> = _filters

    init {
        loadFilters()
    }

    private fun loadFilters() {
        _filters.value = FilterRepository.getFilters()
    }


}
