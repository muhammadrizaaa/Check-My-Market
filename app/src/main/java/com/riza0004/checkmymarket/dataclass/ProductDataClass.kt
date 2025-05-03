package com.riza0004.checkmymarket.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product")
data class ProductDataClass(
    @PrimaryKey(autoGenerate = true)
    val id: Long  = 0L,
    val name: String,
    val price: Int,
    val stock: Long,
    val desc: String = "",
    val onInsert: String,
    val onUpdate: String
)
