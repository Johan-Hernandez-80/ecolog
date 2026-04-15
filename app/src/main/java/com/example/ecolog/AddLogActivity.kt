package com.example.ecolog

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.example.ecolog.ui.theme.EcoLogTheme
import kotlinx.coroutines.launch
import java.util.UUID

class AddLogActivity : ComponentActivity() {
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        dataStoreManager = (application as EcoLogApplication).dataStoreManager

        setContent {
            EcoLogTheme {
                AddLogScreen(
                    onBack = { finish() },
                    onSave = { name, category, emission ->
                        val newLog = CarbonLog(
                            id = UUID.randomUUID().toString(),
                            activityName = name,
                            category = category,
                            carbonEmission = emission
                        )
                        
                        lifecycleScope.launch {
                            dataStoreManager.addLog(newLog)
                            val intent = Intent(this@AddLogActivity, SuccessActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                )
            }
        }
    }
}
