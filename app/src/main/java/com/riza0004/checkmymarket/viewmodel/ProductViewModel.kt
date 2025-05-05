package com.riza0004.checkmymarket.viewmodel

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riza0004.checkmymarket.database.ProductDao
import com.riza0004.checkmymarket.dataclass.CartDataClass
import com.riza0004.checkmymarket.dataclass.ProductDataClass
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val _cart: MutableStateFlow<List<CartDataClass>> = MutableStateFlow(
        emptyList()
    )

    val cart = _cart.asStateFlow()

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
            dao.insertProduct(product)
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
            dao.updateProduct(product)
        }
    }

    fun delete(id: Long){
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteProductById(id)
        }
    }
    fun addToCart(product: ProductDataClass, qty: Int){
        _cart.value += CartDataClass(
            product = product,
            qty = qty,
            totalPrice = (product.price * qty)
        )
    }
    fun removeFromCart(cart: CartDataClass){
        _cart.value -= cart
    }
    fun plusQtyCart(product: ProductDataClass){
        _cart.value = _cart.value.map {
            if(it.product == product) it.copy(qty = it.qty+1) else it
        }
    }
    fun minQtyCart(product: ProductDataClass){
        _cart.value = _cart.value.mapNotNull {
            when {
                it.product == product && it.qty == 1 -> null
                it.product == product -> it.copy(qty = it.qty - 1)
                else -> it
            }
        }
    }
}