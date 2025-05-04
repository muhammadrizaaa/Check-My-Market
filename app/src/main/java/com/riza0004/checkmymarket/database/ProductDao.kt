package com.riza0004.checkmymarket.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.riza0004.checkmymarket.dataclass.ProductDataClass
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {

    @Insert
    suspend fun insert(product: ProductDataClass)

    @Update
    suspend fun update(product: ProductDataClass)

    @Query("SELECT * FROM product ORDER BY name ASC")
    fun getProduct():Flow<List<ProductDataClass>>

    @Query("SELECT * FROM product WHERE id = :id")
    suspend fun getProductById(id:Long): ProductDataClass?
}