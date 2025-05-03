package com.riza0004.checkmymarket.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.riza0004.checkmymarket.dataclass.ProductDataClass
import kotlin.concurrent.Volatile

@Database(entities = [ProductDataClass::class], version = 1, exportSchema = false)
abstract class ProductDb:RoomDatabase(){

    abstract val dao: ProductDao

    companion object {
        @Volatile
        private var INSTANCE: ProductDb? = null

        fun getInstance(context: Context): ProductDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ProductDb::class.java,
                        "product.db"
                    ).build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}