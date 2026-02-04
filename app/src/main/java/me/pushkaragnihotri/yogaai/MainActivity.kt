package me.pushkaragnihotri.yogaai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.collectAsState
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme
import me.pushkaragnihotri.yogaai.ui.MainScreen

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = org.koin.androidx.compose.koinViewModel()
            val finalDestinationState = viewModel.finalDestination.collectAsState()
            val finalDestination = finalDestinationState.value
            
            val themeMode by viewModel.themeMode.collectAsState()
            val isDarkTheme = when (themeMode) {
                1 -> false // Light
                2 -> true  // Dark
                else -> isSystemInDarkTheme() // System (Default)
            }
            
            YogaAITheme(darkTheme = isDarkTheme) {
                if (finalDestination != null) {
                    MainScreen(finalDestination = finalDestination)
                } else {
                     // Splash or loading
                     androidx.compose.foundation.layout.Box(
                         modifier = Modifier.fillMaxSize(), 
                         contentAlignment = androidx.compose.ui.Alignment.Center
                     ) { 
                         androidx.compose.material3.CircularProgressIndicator() 
                     }
                }
            }
        }
    }
}