package me.pushkaragnihotri.yogaai.features.splash.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.ui.theme.YogaAITheme

@Composable
fun SplashScreenContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF), // Center White
                        Color(0xFFF1F8E9)  // Outer Light Green
                    ),
                    radius = 1500f // Approximate radius
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Center Content: Logo + Title + Subtitle
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo Container
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF557168)), // Slate Green from image
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.SelfImprovement,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color(0xFFC8E6C9) // Light Green Tint
                )
            }
            
            Spacer(Modifier.height(24.dp))
            
            // Title
            Text(
                text = stringResource(R.string.app_name_splash),
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF557168) // Match Logo Bg
            )
            
            Spacer(Modifier.height(8.dp))
            
            // Subtitle
            Text(
                text = stringResource(R.string.app_tagline),
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF8FA39D) // Muted Green/Gray
            )
        }
        
        // Bottom Content: Loading
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 64.dp)
                .padding(horizontal = 48.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.splash_loading),
                style = MaterialTheme.typography.labelSmall.copy(
                    letterSpacing = 2.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = Color(0xFF6B877B)
            )
            
            Spacer(Modifier.height(12.dp))
            
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(CircleShape),
                color = Color(0xFF4CAF50), // Bright Green
                trackColor = Color(0xFFE8F5E9) // Very Light Green
            )
        }
    }
}

@DevicePreviews
@Composable
fun SplashScreenPreview() {
    YogaAITheme {
        SplashScreenContent()
    }
}
