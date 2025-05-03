package com.riza0004.checkmymarket.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("homeScreen")
    data object AddProduct: Screen("addProductScreen")
}