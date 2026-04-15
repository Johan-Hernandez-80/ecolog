package com.example.ecolog

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.ecolog.ui.theme.EcoLogTheme

class SuccessActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcoLogTheme {
                SuccessScreen(
                    onContinue = {
                        finish()
                    }
                )
            }
        }
    }
}
