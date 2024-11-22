package com.pm.ce.presentation.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.HOME.route) {
        composableHomeScreen()
        composableRatesScreen()
    }
}