package com.riza0004.checkmymarket.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import com.riza0004.checkmymarket.dataclass.CustomerDataClass
import com.riza0004.checkmymarket.dataclass.DetailTransactionDataClass
import com.riza0004.checkmymarket.dataclass.ProductDataClass
import com.riza0004.checkmymarket.dataclass.TransactionDataClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.concurrent.Volatile

@Database(
    entities = [
        ProductDataClass::class,
        CustomerDataClass::class,
        TransactionDataClass::class,
        DetailTransactionDataClass::class
    ],
    version = 2,
    exportSchema = false
)
abstract class ProductDb:RoomDatabase(){

    abstract val productDao: ProductDao
    abstract val customerDao: CustomerDao
    abstract val transactionDao: TransactionDao
    abstract val detailTransactionDao: DetailTransactionDao

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
                    )
                        .addCallback(object: Callback(){
                            override fun onCreate(connection: SQLiteConnection) {
                                super.onCreate(connection)
                                CoroutineScope(Dispatchers.IO).launch {
                                    getInstance(context).customerDao.insertCustomer(
                                        CustomerDataClass(
                                            id = 1,
                                            name = "Default",
                                            phoneNum = "",
                                            onInsert = "",
                                            onUpdate = ""
                                        )
                                    )
                                }
                            }
                        })
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}