package com.krishna.assessmenttest.Room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData

class MyViewModel(application: Application) : AndroidViewModel(application) {

    private val dataDao = DataBase.getInstance(application).dataDao()

    fun getAllData(): LiveData<List<Model>> {
        return dataDao.getAllData().asLiveData()
    }
}