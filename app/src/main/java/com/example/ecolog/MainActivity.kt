package com.example.ecolog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecolog.ui.theme.EcoLogTheme

class MainActivity : ComponentActivity() {

    // ViewModel con Factory para pasar el DataStoreManager
    private val viewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val dataStoreManager = (application as EcoLogApplication).dataStoreManager
                return MainViewModel(dataStoreManager) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            EcoLogTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route, // Pantalla inicial
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(
                                logsFlow = viewModel.logs,
                                onAddClick = { navController.navigate(Screen.AddLog.route) },
                                onDeleteLog = { id -> viewModel.deleteLog(id) }
                            )
                        }

                        composable(Screen.AddLog.route) {
                            AddLogScreen(
                                onBack = { navController.popBackStack() },
                                onSave = { name, category, emission ->
                                    viewModel.addLog(name, category, emission)
                                    navController.navigate(Screen.Success.route) {
                                        popUpTo(Screen.Home.route)
                                    }
                                }
                            )
                        }

                        composable(Screen.Success.route) {
                            SuccessScreen(
                                onContinue = {
                                    navController.popBackStack(Screen.Home.route, inclusive = false)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
