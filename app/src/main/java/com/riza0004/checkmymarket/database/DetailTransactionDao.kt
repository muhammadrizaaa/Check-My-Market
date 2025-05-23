package com.riza0004.checkmymarket.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.riza0004.checkmymarket.dataclass.DetailTransactionDataClass
import com.riza0004.checkmymarket.dataclass.ProductDataClass
import kotlinx.coroutines.flow.Flow

@Dao
interface DetailTransactionDao {
    @Insert
    suspend fun insertListTransaction(detailTransaction: DetailTransactionDataClass)

    @Query("SELECT * FROM `detailTransaction` WHERE transactionId = :id")
    fun getDetailTransaction(id:Long): Flow<List<DetailTransactionDataClass>>

    @Query("SELECT * FROM `detailTransaction` WHERE id = :id")
    suspend fun getDetailTransactionById(id:Long): DetailTransactionDataClass?

    @Query("SELECT * FROM product WHERE id = :id")
    fun getProduct(id: Long): Flow<ProductDataClass?>

    @Update
    suspend fun updateProduct(product: ProductDataClass)
}