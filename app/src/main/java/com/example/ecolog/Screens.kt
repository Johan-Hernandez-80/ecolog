package com.example.ecolog

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddLog : Screen("add_log")
    object Success : Screen("success")
}