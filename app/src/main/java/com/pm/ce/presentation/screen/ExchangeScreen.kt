package com.pm.ce.presentation.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

@Composable
fun ExchangeScreen() {

}

fun NavGraphBuilder.composableExchangeScreen() {
    composable(route = Screens.EXCHANGE.route) {
        ExchangeScreen()
    }
}