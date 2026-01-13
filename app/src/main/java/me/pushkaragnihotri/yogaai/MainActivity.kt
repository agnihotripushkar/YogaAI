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
import me.pushkaragnihotri.yogaai.ui.theme.YogaAITheme
import me.pushkaragnihotri.yogaai.ui.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = org.koin.androidx.compose.koinViewModel()
            val startDestinationState = viewModel.startDestination.collectAsState()
            val startDestination = startDestinationState.value
            
            YogaAITheme {
                if (startDestination != null) {
                    MainScreen(startDestination = startDestination)
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