package com.pm.ce.presentation.screen.home

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.pm.ce.presentation.screen.Screens

@Composable
fun HomeScreen() {
    Text(text = "This is Home Screen")
}

fun NavGraphBuilder.composableHomeScreen() {
    composable(route = Screens.HOME.route) {
        HomeScreen()
    }
}