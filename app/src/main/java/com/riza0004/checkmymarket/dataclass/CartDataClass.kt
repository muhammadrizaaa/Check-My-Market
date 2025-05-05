package com.riza0004.checkmymarket.dataclass

data class CartDataClass(
    val product: ProductDataClass,
    var qty: Int,
    val totalPrice: Int
)
