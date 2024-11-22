package com.pm.ce.presentation.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

@Composable
fun HomeScreen() {
    Text(text = "This is Home Screen")
}

fun NavGraphBuilder.composableHomeScreen() {
    composable(route = Screens.HOME.route) {
        HomeScreen()
    }
}