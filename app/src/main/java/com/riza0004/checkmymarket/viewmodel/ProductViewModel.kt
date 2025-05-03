package com.riza0004.checkmymarket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riza0004.checkmymarket.database.ProductDao
import com.riza0004.checkmymarket.dataclass.ProductDataClass
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProductViewModel(private val dao: ProductDao):ViewModel() {
    val data: StateFlow<List<ProductDataClass>> = dao.getProduct().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    fun getProduct(id: Long):ProductDataClass?{
        return data.value.find { it.id == id }
    }

    fun insert(
        name: String,
        desc: String,
        price: Int,
        stock: Long
    ){
        val product = ProductDataClass(
            name = name,
            desc = desc,
            price = price,
            stock = stock,
            onInsert = formatter.format(Date()),
            onUpdate = formatter.format(Date())
        )

        viewModelScope.launch {
            dao.insert(product)
        }
    }
}