package com.riza0004.checkmymarket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riza0004.checkmymarket.database.TransactionDao
import com.riza0004.checkmymarket.dataclass.CustomerDataClass
import com.riza0004.checkmymarket.dataclass.TransactionDataClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionViewModel(private val dao: TransactionDao):ViewModel() {
    val data: StateFlow<List<TransactionDataClass>> = dao.getTransactionDesc().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    suspend fun getTransaction(id: Long): TransactionDataClass?{
        return dao.getTransactionById(id)
    }

    fun getCustomer(id:Long): Flow<CustomerDataClass?> {
        return dao.getCustomer(id)
    }

    fun insert(
        idCustomer: Long,
        price:Long,
        onSuccess: (Long) -> Unit
    ){
        val transaction = TransactionDataClass(
            idCustomer = idCustomer,
            price = price,
            timeStamp = formatter.format(Date())
        )
        viewModelScope.launch {
            val id = dao.insertTransaction(transaction)
            onSuccess(id)
        }
    }

}