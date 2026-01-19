package me.pushkaragnihotri.yogaai.features.profile.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.PermissionController
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.res.stringResource
import me.pushkaragnihotri.yogaai.features.R

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel()
) {
    val steps by viewModel.steps.collectAsState()
    val calories by viewModel.calories.collectAsState()
    val hasPermissions = viewModel.hasPermissions.value

    val permissionLauncher = rememberLauncherForActivityResult(
        PermissionController.createRequestPermissionResultContract()
    ) { granted ->
        viewModel.onPermissionsResult(granted)
    }

    LaunchedEffect(Unit) {
        viewModel.initialLoad()
    }

    ProfileScreen(hasPermissions, steps, calories) {
        permissionLauncher.launch(viewModel.permissions)
    }
}

@Composable
fun ProfileScreen(
    hasPermissions: Boolean,
    steps: Long,
    calories: Double,
    onConnectClick: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(stringResource(R.string.title_profile), style = MaterialTheme.typography.headlineMedium)

        if (hasPermissions) {
            Text(stringResource(R.string.profile_health_data_header))
            Text(stringResource(R.string.profile_steps_format, steps))
            Text(stringResource(R.string.profile_calories_format, calories))
        } else {
            Text(stringResource(R.string.profile_connect_prompt))
            Button(onClick = onConnectClick) {
                Text(stringResource(R.string.profile_connect_button))
            }
        }
    }
}

@DevicePreviews
@Composable
fun ProfileScreenPreview() {
    YogaAITheme {
        ProfileScreen(hasPermissions = true, steps = 10000, calories = 2500.0) {}
    }
}

@DevicePreviews
@Composable
fun ProfileScreenNoPermissionsPreview() {
    YogaAITheme {
        ProfileScreen(hasPermissions = false, steps = 0, calories = 0.0) {}
    }
}
