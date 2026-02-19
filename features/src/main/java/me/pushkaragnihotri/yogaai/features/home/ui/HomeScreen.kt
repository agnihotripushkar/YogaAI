package me.pushkaragnihotri.yogaai.features.home.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.health.connect.client.PermissionController
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskLevel
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskPrediction
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.ui.theme.YogaAITheme
import me.pushkaragnihotri.yogaai.features.home.ui.components.HomeScreenContent
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val hasPermissions = viewModel.hasPermissions.value
    val sdkStatus = viewModel.sdkStatus.value

    // Permission launcher for Health Connect
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = PermissionController.createRequestPermissionResultContract(),
        onResult = { granted ->
            Timber.d("HomeScreen: Health Connect permissions result: $granted")
            viewModel.onPermissionsResult(granted)
        }
    )

    // Lifecycle observer: re-check permissions every time the screen resumes
    // (user may have revoked them in system settings while the app was backgrounded)
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                Timber.d("HomeScreen: ON_RESUME — re-checking Health Connect permissions")
                viewModel.checkPermissionsAndLoad()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    StatelessHomeScreen(
        uiState = uiState,
        hasPermissions = hasPermissions,
        sdkAvailable = sdkStatus == HealthConnectManager.SDK_AVAILABLE,
        onGrantPermissionClick = {
            permissionLauncher.launch(viewModel.permissions)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatelessHomeScreen(
    uiState: HomeUiState,
    hasPermissions: Boolean = true,
    sdkAvailable: Boolean = true,
    onGrantPermissionClick: () -> Unit = {}
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            HomeScreenContent(
                uiState = uiState,
                hasPermissions = hasPermissions,
                sdkAvailable = sdkAvailable,
                onGrantPermissionClick = onGrantPermissionClick
            )
        }
    }
}

@DevicePreviews
@Composable
fun HomeScreenPreview() {
    YogaAITheme {
        StatelessHomeScreen(
            uiState = HomeUiState(
                isLoading = false,
                riskPrediction = RiskPrediction(
                    riskLevel = RiskLevel.LOW,
                    explanation = "Your stress levels are low and recovery is high. Great day for a workout!",
                    contributingSignals = emptyList()
                ),
                wellnessItems = emptyList()
            )
        )
    }
}
