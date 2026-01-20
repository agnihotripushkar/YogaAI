package me.pushkaragnihotri.yogaai.features.connect.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MonitorHeart
import androidx.compose.material.icons.rounded.Sync
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme

@Composable
fun ConnectScreenContent(onConnectClick: () -> Unit, onSkipClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Rounded.MonitorHeart, null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(80.dp))
        Spacer(Modifier.height(32.dp))
        Text(stringResource(R.string.connect_title), style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Text(
            stringResource(R.string.connect_desc),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(48.dp))
        
        Button(
            onClick = onConnectClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Icon(Icons.Rounded.Sync, null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.connect_button))
        }
        
        Spacer(Modifier.height(16.dp))
        
        TextButton(onClick = onSkipClick) {
            Text(stringResource(R.string.connect_skip))
        }
    }
}

@DevicePreviews
@Composable
fun ConnectScreenPreview() {
    YogaAITheme {
        ConnectScreenContent(onConnectClick = {}, onSkipClick = {})
    }
}
