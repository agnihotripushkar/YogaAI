package me.pushkaragnihotri.yogaai.features.profile.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.PermissionController
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.profile.ui.components.ProfileScreenContent
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = koinViewModel()
) {
    val steps by viewModel.steps.collectAsState()
    val calories by viewModel.calories.collectAsState()
    val hasPermissions = viewModel.hasPermissions.value
    val sdkStatus = viewModel.sdkStatus.value
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val permissionLauncher = rememberLauncherForActivityResult(
        PermissionController.createRequestPermissionResultContract()
    ) { granted ->
        viewModel.onPermissionsResult(granted)
    }

    LaunchedEffect(Unit) {
        viewModel.initialLoad()
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_profile)) },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            ProfileScreenContent(
                sdkStatus = sdkStatus,
                hasPermissions = hasPermissions,
                steps = steps,
                calories = calories,
                onConnectClick = {
                    Timber.d("Launching permissions request for: ${viewModel.permissions}")
                    permissionLauncher.launch(viewModel.permissions)
                }
            )
        }
    }
}
