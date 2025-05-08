package com.riza0004.checkmymarket.navigation

import com.riza0004.checkmymarket.ui.screen.KEY_ID_Customer
import com.riza0004.checkmymarket.ui.screen.KEY_ID_PRODUCT
import com.riza0004.checkmymarket.ui.screen.KEY_ID_TRANSACTION

sealed class Screen(val route: String) {
    data object Home: Screen("homeScreen")
    data object ListProduct: Screen("listProductScreen")
    data object AddProduct: Screen("addProductScreen")
    data object DetailProduct: Screen("detailProductScreen/{$KEY_ID_PRODUCT}"){
        fun withId(id: Long) = "detailProductScreen/$id"
    }
    data object ListCustomer: Screen("listCustomerScreen")
    data object AddCustomer: Screen("addCustomerScreen")
    data object DetailCustomer: Screen("detailCustomerScreen/{$KEY_ID_Customer}"){
        fun withId(id: Long) = "detailCustomerScreen/$id"
    }
    data object Cart: Screen("cartScreen")
    data object Transaction: Screen("transactionScreen")
    data object DetailTransaction: Screen("detailTransactionScreen/{$KEY_ID_TRANSACTION}"){
        fun withId(id: Long) = "detailTransactionScreen/$id"
    }
    data object DeletedProduct: Screen("deletedProductScreen")
}