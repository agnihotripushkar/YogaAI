package me.pushkaragnihotri.yogaai.features.connect.ui

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.health.connect.client.PermissionController
import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import me.pushkaragnihotri.yogaai.features.connect.ui.components.ConnectScreenContent
import me.pushkaragnihotri.yogaai.features.onboarding.ui.OnboardingAction
import me.pushkaragnihotri.yogaai.features.onboarding.ui.OnboardingViewModel
import timber.log.Timber
import androidx.compose.ui.tooling.preview.Preview
import me.pushkaragnihotri.yogaai.features.ui.theme.YogaAITheme

@Composable
fun ConnectScreen(viewModel: OnboardingViewModel, onFinished: () -> Unit) {
    val TAG = "ConnectScreen"
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = PermissionController.createRequestPermissionResultContract(),
        onResult = { granted ->
            viewModel.onAction(OnboardingAction.HealthPermissionsResult(granted))
            onFinished()
        }
    )

    ConnectScreenContent(
        onConnectClick = { 
            val availability = viewModel.getHealthConnectAvailability()
            Timber.d("$TAG Health Connect availability: $availability")
            if (availability == HealthConnectManager.SDK_AVAILABLE) {
                Timber.d("${TAG} Launching permissions request")
                permissionLauncher.launch(viewModel.permissions)
            } else {
                Toast.makeText(context, "Health Connect is not available on this device.", Toast.LENGTH_LONG).show()
            }
        },
        onSkipClick = onFinished
    )
}

@Preview(name = "Connect Screen")
@Composable
private fun ConnectScreenPreview() {
    YogaAITheme {
        ConnectScreenContent(
            onConnectClick = {},
            onSkipClick = {}
        )
    }
}
