package com.riza0004.checkmymarket.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.riza0004.checkmymarket.dataclass.CustomerDataClass
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomerDao {
    @Insert
    suspend fun insertCustomer(customer: CustomerDataClass)

    @Update
    suspend fun updateCustomer(customer: CustomerDataClass)

    @Query("SELECT * FROM customer ORDER BY name ASC")
    fun getCustomer():Flow<List<CustomerDataClass>>

    @Query("SELECT * FROM customer WHERE id = :id")
    suspend fun getCustomerById(id:Long):CustomerDataClass?

    @Query("DELETE FROM customer WHERE id = :id")
    suspend fun deleteCustomerById(id:Long)
}