package com.riza0004.checkmymarket.dataclass

import androidx.room.Entity
import androidx.room.PrimaryKey


data class CustomerDataClass(
    val id: Long = 0L,
    val name: String,
    val phoneNum: String = ""
)
