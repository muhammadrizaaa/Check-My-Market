package com.riza0004.checkmymarket.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.riza0004.checkmymarket.dataclass.CustomerDataClass
import com.riza0004.checkmymarket.dataclass.TransactionDataClass
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun insertTransaction(transaction: TransactionDataClass): Long

    @Query("SELECT * FROM `transaction` ORDER BY timeStamp DESC")
    fun getTransactionDesc(): Flow<List<TransactionDataClass>>

    @Query("SELECT * FROM `transaction` ORDER BY timeStamp ASC")
    fun getTransactionAsc(): Flow<List<TransactionDataClass>>

    @Query("SELECT * FROM `transaction` WHERE id = :id")
    suspend fun getTransactionById(id:Long): TransactionDataClass?

    @Query("SELECT * FROM customer WHERE id = :id")
    suspend fun getCustomer(id: Long): CustomerDataClass?
}