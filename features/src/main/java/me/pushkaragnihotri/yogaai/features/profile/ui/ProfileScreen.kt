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
import me.pushkaragnihotri.yogaai.core.data.HealthConnectManager
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.res.stringResource
import androidx.annotation.OptIn
import me.pushkaragnihotri.yogaai.features.R
import timber.log.Timber

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
        Timber.d("Launching permissions request for: ${viewModel.permissions}")
        permissionLauncher.launch(viewModel.permissions)
    }
}

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
            if (sdkStatus != HealthConnectManager.SDK_AVAILABLE) {
                 Spacer(Modifier.height(16.dp))
                 Text("Debug Status: $sdkStatus")
            }

            when (sdkStatus) {
                HealthConnectManager.SDK_AVAILABLE -> {
                    if (hasPermissions) {
                        Text(stringResource(R.string.profile_health_data_header))
                        Text(stringResource(R.string.profile_steps_format, steps))
                        Text(stringResource(R.string.profile_calories_format, calories))
                    } else {
                        Text(stringResource(R.string.profile_connect_prompt))
                        Button(onClick = {
                            Timber.d("User clicked Connect button (SDK Available)")
                            onConnectClick()
                        }) {
                            Text(stringResource(R.string.profile_connect_button))
                        }
                    }
                }
                HealthConnectManager.SDK_UPDATE_REQUIRED -> {
                     Text("Health Connect is not installed or needs an update.")
                     Spacer(Modifier.height(8.dp))
                     val context = androidx.compose.ui.platform.LocalContext.current
                     Button(onClick = {
                         try {
                             Timber.d("User clicked UPDATE button. Launching Market Intent.")
                             val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                 data = android.net.Uri.parse("market://details?id=com.google.android.apps.healthdata")
                                 setPackage("com.android.vending")
                             }
                             context.startActivity(intent)
                         } catch (e: Exception) {
                             Timber.e(e, "Failed to launch market intent")
                             try {
                                 val webIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata"))
                                 context.startActivity(webIntent)
                             } catch (e2: Exception) {
                                 Timber.e(e2, "Failed to launch web intent")
                                 android.widget.Toast.makeText(context, "Could not open Play Store", android.widget.Toast.LENGTH_SHORT).show()
                             }
                         }
                     }) {
                         Text("FORCE UPDATE (Status 3)")
                     }
                }
                else -> {
                    Text("Health Connect is not available on this device.")
                    Spacer(Modifier.height(8.dp))
                    val context = androidx.compose.ui.platform.LocalContext.current
                    Button(onClick = {
                         try {
                             Timber.d("User clicked INSTALL button. Launching Market Intent.")
                             val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                                 data = android.net.Uri.parse("market://details?id=com.google.android.apps.healthdata")
                                 setPackage("com.android.vending")
                             }
                             context.startActivity(intent)
                         } catch (e: Exception) {
                             Timber.e(e, "Failed to launch market intent")
                             try {
                                 val webIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata"))
                                 context.startActivity(webIntent)
                             } catch (e2: Exception) {
                                 Timber.e(e2, "Failed to launch web intent")
                                 android.widget.Toast.makeText(context, "Could not open Play Store", android.widget.Toast.LENGTH_SHORT).show()
                             }
                         }
                    }) {
                        Text("FORCE INSTALL (Status $sdkStatus)")
                    }
                }
            }
            
            Spacer(Modifier.height(32.dp))
            HorizontalDivider()
            Spacer(Modifier.height(16.dp))
            Text("Troubleshooting Actions:", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            val context = androidx.compose.ui.platform.LocalContext.current
            Button(
                onClick = {
                    try {
                        val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                            data = android.net.Uri.parse("market://details?id=com.google.android.apps.healthdata")
                            setPackage("com.android.vending")
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                         val webIntent = android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.healthdata"))
                         context.startActivity(webIntent)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
            ) {
                Text("Open Health Connect on Play Store")
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
