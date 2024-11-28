package com.pm.cc.presentation.screen.rates

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pm.cc.presentation.screen.Screens

@Composable
fun RatesScreen() {

}

fun NavGraphBuilder.composableRatesScreen() {
    composable(route = Screens.RATES.route) {
        RatesScreen()
    }
}