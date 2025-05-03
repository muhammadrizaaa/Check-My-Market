package com.riza0004.checkmymarket.dataclass

data class ProductDataClass(
    val id: Long,
    val name: String,
    val price: Int,
    val stock: Short,
    val desc: String = ""
)
