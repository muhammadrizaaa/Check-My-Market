package com.riza0004.checkmymarket.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.riza0004.checkmymarket.database.ProductDb
import com.riza0004.checkmymarket.viewmodel.ProductViewModel
import com.riza0004.checkmymarket.viewmodel.CustomerViewModel
import com.riza0004.checkmymarket.viewmodel.DetailTransactionViewModel
import com.riza0004.checkmymarket.viewmodel.TransactionViewModel

class ViewModelFactory(
    private val context: Context
): ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val productDao = ProductDb.getInstance(context).productDao
        val customerDao = ProductDb.getInstance(context).customerDao
        val transactionDao = ProductDb.getInstance(context).transactionDao
        val detailTransactionDao = ProductDb.getInstance(context).detailTransactionDao
        if(modelClass.isAssignableFrom(ProductViewModel::class.java)){
            return ProductViewModel(productDao) as T
        }
        else if(modelClass.isAssignableFrom(CustomerViewModel::class.java)){
            return CustomerViewModel(customerDao) as T
        }
        else if(modelClass.isAssignableFrom(TransactionViewModel::class.java)){
            return TransactionViewModel(transactionDao) as T
        }
        else if(modelClass.isAssignableFrom(DetailTransactionViewModel::class.java)){
            return DetailTransactionViewModel(detailTransactionDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}