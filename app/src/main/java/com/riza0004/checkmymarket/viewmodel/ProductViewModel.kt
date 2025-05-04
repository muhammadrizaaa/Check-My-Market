package com.riza0004.checkmymarket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riza0004.checkmymarket.database.ProductDao
import com.riza0004.checkmymarket.dataclass.ProductDataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
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

    val dataDummy: StateFlow<List<ProductDataClass>> = MutableStateFlow(
        listOf(
            ProductDataClass(
                name = "Aqua",
                price = 4000,
                stock = 5,
                onInsert = "2020-05-12 12:12:12",
                onUpdate = "2020-05-12 12:12:12"
            )
        )
    )

    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)

    suspend fun getProduct(id: Long):ProductDataClass?{
        return dao.getProductById(id)
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
    fun update(
        id:Long,
        name: String,
        desc: String,
        price: Int,
        stock: Long,
        onInsert: String
    ){
        val product = ProductDataClass(
            id = id,
            name = name,
            desc = desc,
            price = price,
            stock = stock,
            onInsert = onInsert,
            onUpdate = formatter.format(Date())
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(product)
        }
    }
}