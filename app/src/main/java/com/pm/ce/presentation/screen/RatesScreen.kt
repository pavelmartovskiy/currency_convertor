package com.pm.ce.presentation.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

@Composable
fun RatesScreen() {

}

fun NavGraphBuilder.composableRatesScreen() {
    composable(route = Screens.RATES.route) {
        RatesScreen()
    }
}