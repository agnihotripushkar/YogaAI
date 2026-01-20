package me.pushkaragnihotri.yogaai.features.consent.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.VerifiedUser
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme

@Composable
fun ConsentScreen(onConsentGiven: () -> Unit) {
    var checked by remember { mutableStateOf(false) }
    
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Spacer(Modifier.height(24.dp))
        Icon(Icons.Rounded.VerifiedUser, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(48.dp))
        Spacer(Modifier.height(16.dp))
        Text(stringResource(R.string.consent_title), style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))
        
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
                Text(
                    stringResource(R.string.consent_terms),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        Row(verticalAlignment = Alignment.Top) {
            Checkbox(checked = checked, onCheckedChange = { checked = it })
            Text(
                stringResource(R.string.consent_checkbox),
                modifier = Modifier.padding(top = 12.dp, start = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        Spacer(Modifier.height(24.dp))
        
        Button(
            onClick = onConsentGiven,
            enabled = checked,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text(stringResource(R.string.consent_button))
        }
    }
}

@DevicePreviews
@Composable
fun ConsentScreenPreview() {
    YogaAITheme {
        ConsentScreen(onConsentGiven = {})
    }
}
