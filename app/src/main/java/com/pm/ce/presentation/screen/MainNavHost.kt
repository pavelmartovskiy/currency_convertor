package com.pm.ce.presentation.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.pm.ce.presentation.screen.home.composableHomeScreen
import com.pm.ce.presentation.screen.rates.composableRatesScreen

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.HOME.route) {
        composableHomeScreen()
        composableRatesScreen()
    }
}