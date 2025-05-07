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
    fun getDetailTransactionById(id:Long): StateFlow<List<DetailTransactionDataClass>>{
        return dao.getDetailTransaction(id).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )
    }

    fun insert(
        idTransaction: Long,
        cart: List<CartDataClass>
        ){
        viewModelScope.launch {
            for (it in cart) {
                val detailTransaction = DetailTransactionDataClass(
                    transactionId = idTransaction,
                    qty = it.qty,
                    productId = it.product.id
                )
                val product = it.product.copy(
                    stock = it.product.stock - it.qty
                )

                dao.insertListTransaction(detailTransaction)
                dao.updateProduct(product)
            }
        }
    }

    fun getProductById(id:Long): Flow<ProductDataClass?> {
        return dao.getProduct(id)
    }
}