package com.riza0004.checkmymarket.navigation

import com.riza0004.checkmymarket.ui.screen.KEY_ID_PRODUCT

sealed class Screen(val route: String) {
    data object Home: Screen("homeScreen")
    data object ListProduct: Screen("listProductScreen")
    data object AddProduct: Screen("addProductScreen")
    data object DetailProduct: Screen("detailProductScreen/{$KEY_ID_PRODUCT}"){
        fun withId(id: Long) = "detailProductScreen/$id"
    }
}