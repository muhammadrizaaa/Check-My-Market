package com.riza0004.checkmymarket.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction")
data class TransactionDataClass(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val idCustomer: Long,
    val price: Long,
    val timeStamp: String
)
