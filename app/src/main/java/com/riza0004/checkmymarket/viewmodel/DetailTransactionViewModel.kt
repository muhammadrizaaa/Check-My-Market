package com.riza0004.checkmymarket.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riza0004.checkmymarket.database.DetailTransactionDao
import com.riza0004.checkmymarket.dataclass.CartDataClass
import com.riza0004.checkmymarket.dataclass.DetailTransactionDataClass
import com.riza0004.checkmymarket.dataclass.ProductDataClass
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailTransactionViewModel(private val dao: DetailTransactionDao):ViewModel() {
    val data: StateFlow<List<DetailTransactionDataClass>> = dao.getDetailTransaction().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList()
    )

    fun insert(
        idTransaction: Long,
        cart: List<CartDataClass>
        ){
        cart.map{
            val detailTransaction = DetailTransactionDataClass(
                transactionId = idTransaction,
                qty = it.qty,
                productId = it.product.id
            )
            val product = ProductDataClass(
                id = it.product.id,
                name = it.product.name,
                price = it.product.price,
                stock = it.product.stock - it.qty,
                onInsert = it.product.onInsert,
                onUpdate = it.product.onUpdate
            )
            viewModelScope.launch {
                dao.insertListTransaction(detailTransaction)
                dao.updateProduct(product)
            }


        }
    }
    suspend fun getDetailTransaction(id:Long): DetailTransactionDataClass?{
        return dao.getDetailTransactionById(id)
    }

    fun getProductById(id:Long): Flow<ProductDataClass?> {
        return dao.getProduct(id)
    }
}