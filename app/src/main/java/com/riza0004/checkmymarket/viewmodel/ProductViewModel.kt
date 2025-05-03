package com.riza0004.checkmymarket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riza0004.checkmymarket.database.ProductDao
import com.riza0004.checkmymarket.dataclass.ProductDataClass
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ProductViewModel(dao: ProductDao):ViewModel() {
    val data: StateFlow<List<ProductDataClass>> = dao.getProduct().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    fun getProduct(id: Long):ProductDataClass?{
        return data.value.find { it.id == id }
    }
}