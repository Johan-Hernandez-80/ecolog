package com.example.ecolog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ecolog.ui.theme.EcoLogTheme
import kotlinx.coroutines.launch
import java.util.UUID

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val dataStoreManager = (application as EcoLogApplication).dataStoreManager

        setContent {
            EcoLogTheme {
                val navController = rememberNavController()
                
                // Obtenemos los logs directamente del DataStore
                val logs by dataStoreManager.logsFlow.collectAsState(initial = emptyList())

                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(
                                logs = logs,
                                onAddClick = { navController.navigate(Screen.AddLog.route) },
                                onDeleteLog = { id ->
                                    lifecycleScope.launch {
                                        dataStoreManager.deleteLog(id)
                                    }
                                }
                            )
                        }

                        composable(Screen.AddLog.route) {
                            AddLogScreen(
                                onBack = { navController.popBackStack() },
                                onSave = { name, category, emission ->
                                    lifecycleScope.launch {
                                        dataStoreManager.addLog(
                                            CarbonLog(
                                                id = UUID.randomUUID().toString(),
                                                activityName = name,
                                                category = category,
                                                carbonEmission = emission
                                            )
                                        )
                                        navController.navigate(Screen.Success.route) {
                                            popUpTo(Screen.Home.route)
                                        }
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
