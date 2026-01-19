package me.pushkaragnihotri.yogaai.features.profile.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.PermissionController
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme
import androidx.health.connect.client.HealthConnectClient
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

    val sdkStatus = viewModel.sdkStatus.value

    val permissionLauncher = rememberLauncherForActivityResult(
        PermissionController.createRequestPermissionResultContract()
    ) { granted ->
        viewModel.onPermissionsResult(granted)
    }

    LaunchedEffect(Unit) {
        viewModel.initialLoad()
    }

    ProfileScreen(sdkStatus, hasPermissions, steps, calories) {
        permissionLauncher.launch(viewModel.permissions)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    sdkStatus: Int,
    hasPermissions: Boolean,
    steps: Long,
    calories: Double,
    onConnectClick: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_profile)) },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            if (sdkStatus != HealthConnectClient.SDK_AVAILABLE) {
                 Spacer(Modifier.height(16.dp))
            }

            when (sdkStatus) {
                HealthConnectClient.SDK_AVAILABLE -> {
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
                HealthConnectClient.SDK_UNAVAILABLE_PROVIDER_UPDATE_REQUIRED -> {
                     Text("Health Connect is not installed or needs an update.")
                }
                else -> {
                    Text("Health Connect is not available on this device.")
                }
            }
        }
    }
}

@DevicePreviews
@Composable
fun ProfileScreenPreview() {
    YogaAITheme {
        ProfileScreen(HealthConnectClient.SDK_AVAILABLE, hasPermissions = true, steps = 10000, calories = 2500.0) {}
    }
}

@DevicePreviews
@Composable
fun ProfileScreenNoPermissionsPreview() {
    YogaAITheme {
        ProfileScreen(HealthConnectClient.SDK_AVAILABLE, hasPermissions = false, steps = 0, calories = 0.0) {}
    }
}
