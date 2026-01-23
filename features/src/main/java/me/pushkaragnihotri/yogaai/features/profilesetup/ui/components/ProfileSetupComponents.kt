package me.pushkaragnihotri.yogaai.features.profilesetup.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Cake
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme

@Composable
fun ProfileSetupScreenContent(
    name: String,
    onNameChange: (String) -> Unit,
    age: String,
    onAgeChange: (String) -> Unit,
    level: String,
    onLevelChange: (String) -> Unit,
    onCompleteClick: () -> Unit
) {
    val levels = listOf(
        stringResource(R.string.profile_level_beginner),
        stringResource(R.string.profile_level_intermediate),
        stringResource(R.string.profile_level_advanced)
    )
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(40.dp))
        Text(
            stringResource(R.string.profile_setup_title), 
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            stringResource(R.string.profile_setup_desc), 
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(Modifier.height(32.dp))
        
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text(stringResource(R.string.profile_name_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = { Icon(Icons.Rounded.Person, null) }
        )
        
        Spacer(Modifier.height(16.dp))
        
        OutlinedTextField(
            value = age,
            onValueChange = onAgeChange,
            label = { Text(stringResource(R.string.profile_age_label)) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            leadingIcon = { Icon(Icons.Rounded.Cake, null) }
        )
        
        Spacer(Modifier.height(24.dp))
        
        Text(stringResource(R.string.profile_exp_level), style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(), 
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            levels.forEach { item ->
                FilterChip(
                    selected = level == item,
                    onClick = { onLevelChange(item) },
                    label = { Text(item) }
                )
            }
        }
        
        Spacer(Modifier.weight(1f))
        
        Button(
            onClick = onCompleteClick,
            enabled = name.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text(stringResource(R.string.profile_complete_button))
        }
    }
}

@DevicePreviews
@Composable
fun ProfileSetupScreenPreview() {
    YogaAITheme {
        ProfileSetupScreenContent(
            name = "John Doe",
            onNameChange = {},
            age = "25",
            onAgeChange = {},
            level = "Beginner",
            onLevelChange = {},
            onCompleteClick = {}
        )
    }
}
