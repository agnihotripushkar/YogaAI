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
import me.pushkaragnihotri.yogaai.core.presentation.ObserveAsEvents
import me.pushkaragnihotri.yogaai.core.presentation.UiText
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskLevel
import me.pushkaragnihotri.yogaai.features.home.data.model.RiskPrediction
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.ui.theme.YogaAITheme
import me.pushkaragnihotri.yogaai.features.home.ui.components.HomeScreenContent
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun HomeRoot(
    onOpenPoseLibrary: () -> Unit = {},
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = PermissionController.createRequestPermissionResultContract(),
        onResult = { granted ->
            Timber.d("HomeRoot: Health Connect permissions result: $granted")
            viewModel.onAction(HomeAction.OnPermissionsResult(granted))
        }
    )

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is HomeEvent.RequestPermissions -> permissionLauncher.launch(event.permissions)
            is HomeEvent.ShowSnackbar -> { /* Snackbar handled via state.error */ }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                Timber.d("HomeRoot: ON_RESUME — re-checking Health Connect permissions")
                viewModel.onAction(HomeAction.OnResumed)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    HomeScreen(
        state = state,
        onAction = viewModel::onAction,
        onOpenPoseLibrary = onOpenPoseLibrary
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    onOpenPoseLibrary: () -> Unit = {}
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            HomeScreenContent(
                state = state,
                onGrantPermissionClick = { onAction(HomeAction.OnGrantPermissionClick) },
                onOpenPoseLibrary = onOpenPoseLibrary
            )
        }
    }
}

@DevicePreviews
@Composable
private fun HomeScreenLoadedPreview() {
    YogaAITheme {
        HomeScreen(
            state = HomeState(
                isLoading = false,
                hasPermissions = true,
                sdkAvailable = true,
                riskPrediction = RiskPrediction(
                    riskLevel = RiskLevel.LOW,
                    explanation = UiText.DynamicString("Your stress levels are low and recovery is high. Great day for a workout!"),
                    contributingSignals = emptyList()
                ),
                wellnessItems = emptyList()
            ),
            onAction = {}
        )
    }
}

@DevicePreviews
@Composable
private fun HomeScreenLoadingPreview() {
    YogaAITheme {
        HomeScreen(
            state = HomeState(isLoading = true),
            onAction = {}
        )
    }
}

@DevicePreviews
@Composable
private fun HomeScreenNoPermissionsPreview() {
    YogaAITheme {
        HomeScreen(
            state = HomeState(
                isLoading = false,
                hasPermissions = false,
                sdkAvailable = true
            ),
            onAction = {}
        )
    }
}
