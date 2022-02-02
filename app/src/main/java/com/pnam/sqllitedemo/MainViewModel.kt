package com.pnam.sqllitedemo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val dbHelper: DBHelper = DBHelper(application)
    internal var list: List<TestModel> = dbHelper.findTestModels()
    internal val selected: MutableLiveData<TestModel> by lazy { MutableLiveData(null) }

    internal fun onCreate() {
        list = dbHelper.findTestModels()
    }

    internal fun findTestModelsByName(name: String) {
        list = dbHelper.findTestModelsByName(name)
    }

    internal fun insert(name: String) {
        dbHelper.insertTestModel(name)
        list = dbHelper.findTestModels()
    }

    internal fun update(testModel: TestModel) {
        dbHelper.updateTestModel(testModel)
        list = dbHelper.findTestModels()
    }

    internal fun delete(id: Int) {
        dbHelper.deleteTestModel(id)
        list = dbHelper.findTestModels()
    }
}