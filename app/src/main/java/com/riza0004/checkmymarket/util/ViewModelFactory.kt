package com.riza0004.checkmymarket.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riza0004.checkmymarket.database.ProductDb
import com.riza0004.checkmymarket.viewmodel.ProductViewModel

class ViewModelFactory(
    private val context: Context
): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val dao = ProductDb.getInstance(context).dao
        if(modelClass.isAssignableFrom(ProductViewModel::class.java)){
            return ProductViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}