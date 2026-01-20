package com.appxs.apex.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.appxs.apex.presentation.screen.home.HomeRoute
import com.appxs.apex.presentation.ui.theme.ApexTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // This enables the "edge-to-edge" display, making system bars transparent
        // and allowing the app to draw behind them, which is the default in Android 15/16.
        enableEdgeToEdge()
        
        setContent {
            ApexTheme {
                HomeRoute()
            }
        }
    }
}
