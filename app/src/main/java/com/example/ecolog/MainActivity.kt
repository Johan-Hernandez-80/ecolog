package com.example.ecolog

import android.content.Intent
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
                
                // Dashboard (Task 2)
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

                        // Task 1: Navigation to Registration Screen
                        composable(Screen.AddLog.route) {
                            AddLogScreen(
                                onBack = { navController.popBackStack() },
                                onSave = { name, category, emission ->
                                    // Task 3: Background storage with Coroutines
                                    lifecycleScope.launch {
                                        dataStoreManager.addLog(
                                            CarbonLog(
                                                id = UUID.randomUUID().toString(),
                                                activityName = name,
                                                category = category,
                                                carbonEmission = emission
                                            )
                                        )
                                        // Task 3: Launch SuccessActionActivity via Intent
                                        val intent = Intent(this@MainActivity, SuccessActionActivity::class.java)
                                        startActivity(intent)
                                        
                                        // Go back to home so the list is updated when returning
                                        navController.popBackStack(Screen.Home.route, inclusive = false)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
