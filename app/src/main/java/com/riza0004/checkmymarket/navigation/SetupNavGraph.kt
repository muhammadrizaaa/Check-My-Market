package com.riza0004.checkmymarket.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.riza0004.checkmymarket.ui.screen.MainAddProductScreen
import com.riza0004.checkmymarket.ui.screen.MainHomeScreen

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
    }
}