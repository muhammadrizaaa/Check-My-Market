package com.riza0004.checkmymarket.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "detailTransaction")
data class DetailTransactionDataClass(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val productId: Long,
    val qty: Int,
    val transactionId: Long
)
