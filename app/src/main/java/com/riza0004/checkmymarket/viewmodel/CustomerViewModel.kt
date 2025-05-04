package com.riza0004.checkmymarket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riza0004.checkmymarket.database.CustomerDao
import com.riza0004.checkmymarket.dataclass.CustomerDataClass
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CustomerViewModel(private val dao: CustomerDao):ViewModel() {
    val data: StateFlow<List<CustomerDataClass>> = dao.getCustomer().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    suspend fun getCustomer(id: Long): CustomerDataClass?{
        return dao.getCustomerById(id)
    }

    fun insert(
        name: String,
        phoneNum: String
    ){
        val customer = CustomerDataClass(
            name = name,
            phoneNum = phoneNum,
            onInsert = formatter.format(Date()),
            onUpdate = formatter.format(Date())
        )
        viewModelScope.launch {
            dao.insertCustomer(customer)
        }
    }
    fun update(
        id:Long,
        name: String,
        phoneNum: String,
        onCreate: String
    ){
        val customer = CustomerDataClass(
            id = id,
            name = name,
            phoneNum = phoneNum,
            onInsert = onCreate,
            onUpdate = formatter.format(Date())
        )
        viewModelScope.launch {
            dao.updateCustomer(customer)
        }
    }

    fun delete(id:Long){
        viewModelScope.launch {
            dao.deleteCustomerById(id)
        }
    }
}