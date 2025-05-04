package com.riza0004.checkmymarket.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer")
data class CustomerDataClass(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val phoneNum: String = "",
    val onInsert: String,
    val onUpdate: String
)
