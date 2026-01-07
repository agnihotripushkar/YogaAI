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
import me.pushkaragnihotri.yogaai.ui.theme.YogaAITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            YogaAITheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // Pass innerPadding to YogaNavigation if needed for global padding, 
                    // or handle it inside screens. For now, we'll apply it to the NavHost container if possible,
                    // but YogaNavigation doesn't take modifier yet. 
                    // Let's just wrap it in a Box for now or modify YogaNavigation.
                    // Actually, usually Scaffolds are per-screen or top-level.
                    // Let's just call YogaNavigation inside a Box with padding.
                    androidx.compose.foundation.layout.Box(modifier = Modifier.padding(innerPadding)) {
                         me.pushkaragnihotri.yogaai.features.navigation.YogaNavigation()
                    }
                }
            }
        }
    }
}