package com.pm.cc.presentation.screen

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.pm.cc.presentation.screen.home.composableHomeScreen
import com.pm.cc.presentation.screen.rates.composableRatesScreen

@Composable
fun MainNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.HOME.route) {
        composableHomeScreen()
        composableRatesScreen()
    }
}