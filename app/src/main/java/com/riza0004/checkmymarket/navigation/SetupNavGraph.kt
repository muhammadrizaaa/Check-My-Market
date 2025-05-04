package com.riza0004.checkmymarket.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.riza0004.checkmymarket.ui.screen.KEY_ID_Customer
import com.riza0004.checkmymarket.ui.screen.KEY_ID_PRODUCT
import com.riza0004.checkmymarket.ui.screen.MainAddCustomerScreen
import com.riza0004.checkmymarket.ui.screen.MainAddProductScreen
import com.riza0004.checkmymarket.ui.screen.MainHomeScreen
import com.riza0004.checkmymarket.ui.screen.MainListCustomerScreen
import com.riza0004.checkmymarket.ui.screen.MainListProductScreen

@Composable
fun SetupNavGraph(navHostController: NavHostController = rememberNavController()){
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home.route
    ) {
        composable(
            route = Screen.Home.route
        ){
            MainHomeScreen(navHostController)
        }
        composable(
            route = Screen.AddProduct.route
        ){
            MainAddProductScreen(navHostController)
        }
        composable(
            route = Screen.DetailProduct.route,
            arguments = listOf(
                navArgument(KEY_ID_PRODUCT) {type = NavType.LongType}
            )
        ) {navBackStackEntry ->  
            val id = navBackStackEntry.arguments?.getLong(KEY_ID_PRODUCT)
            MainAddProductScreen(navHostController, id)
        }
        composable(
            route = Screen.ListProduct.route
        ){
            MainListProductScreen(navHostController)
        }
        composable(
            route = Screen.ListCustomer.route
        ){
            MainListCustomerScreen(navHostController)
        }
        composable(
            route = Screen.AddCustomer.route
        ) {
            MainAddCustomerScreen(navHostController)
        }
        composable(
            route = Screen.DetailCustomer.route,
            arguments = listOf(
                navArgument(KEY_ID_Customer) {type = NavType.LongType}
            )
        ) {navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong(KEY_ID_Customer)
            MainAddCustomerScreen(navHostController, id)
        }
    }
}