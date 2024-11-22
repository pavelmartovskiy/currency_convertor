package com.pm.ce.presentation.screen

import com.pm.ce.presentation.screen.home.HomeViewModel
import org.koin.dsl.module

enum class Screens(val route: String) {
    HOME(route = "home"),
    RATES(route = "rates")
}

fun screensModule() = module {
    single { HomeViewModel() }
}

