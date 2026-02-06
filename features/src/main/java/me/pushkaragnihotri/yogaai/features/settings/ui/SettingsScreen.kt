package me.pushkaragnihotri.yogaai.features.settings.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.health.connect.client.PermissionController
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import me.pushkaragnihotri.yogaai.features.R
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import me.pushkaragnihotri.yogaai.features.settings.ui.components.SettingsScreenContent
import org.koin.androidx.compose.koinViewModel
import android.content.Intent
import android.net.Uri
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme
import me.pushkaragnihotri.yogaai.features.settings.viewmodel.SettingsViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    onNavigateUp: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val themeMode by viewModel.themeMode.collectAsState(initial = 0)
    val language by viewModel.language.collectAsState(initial = "English")
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.title_settings)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, stringResource(R.string.content_description_back))
                    }
                }
            )
        }
    ) { padding ->
        val windowSizeClass = calculateWindowSizeClass(context as android.app.Activity)
        val widthSizeClass = windowSizeClass.widthSizeClass

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            val contentModifier = if (widthSizeClass == WindowWidthSizeClass.Compact) {
                Modifier.fillMaxWidth()
            } else {
                Modifier.widthIn(max = 640.dp)
            }
            
            Box(modifier = contentModifier) {
                SettingsScreenContent(
                    themeMode = themeMode,
                    sdkStatus = sdkStatus,
                    hasPermissions = hasPermissions,
                    steps = steps,
                    calories = calories,
                    language = language,
                    onThemeChange = viewModel::setTheme,
                    onLanguageChange = viewModel::setLanguage,
                    onDeleteData = viewModel::deleteData,
                    onConnectClick = {
                         permissionLauncher.launch(viewModel.permissions)
                    },
                    onDisconnectWearable = viewModel::disconnectWearable,
                    onPrivacyPolicyClick = {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com")) // Placeholder URL
                        context.startActivity(intent)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun SettingsScreenContentPreview() {
    YogaAITheme {
        SettingsScreenContent(
            themeMode = 0,
            sdkStatus = HealthConnectManager.SDK_AVAILABLE,
            hasPermissions = true,
            steps = 5201,
            calories = 120.0,
            language = "English",
            onThemeChange = {},
            onLanguageChange = {},
            onDeleteData = {},
            onConnectClick = {},
            onDisconnectWearable = {},
            onPrivacyPolicyClick = {}
        )
    }
}
