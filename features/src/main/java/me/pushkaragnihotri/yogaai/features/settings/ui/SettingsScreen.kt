package me.pushkaragnihotri.yogaai.features.settings.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.health.connect.client.PermissionController
import me.pushkaragnihotri.yogaai.core.HealthConnectManager
import me.pushkaragnihotri.yogaai.core.presentation.ObserveAsEvents
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.settings.ui.components.SettingsScreenContent
import me.pushkaragnihotri.yogaai.features.ui.theme.YogaAITheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun SettingsRoot(
    onNavigateUp: () -> Unit,
    onNavigateToAppearance: () -> Unit = {},
    viewModel: SettingsViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    val permissionLauncher = rememberLauncherForActivityResult(
        PermissionController.createRequestPermissionResultContract()
    ) { granted ->
        viewModel.onAction(SettingsAction.OnPermissionsResult(granted))
    }

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is SettingsEvent.NavigateToAppearance -> onNavigateToAppearance()
            is SettingsEvent.RequestPermissions -> permissionLauncher.launch(event.permissions)
            is SettingsEvent.OpenUrl -> {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.url))
                context.startActivity(intent)
            }
            SettingsEvent.RateApp -> {
                val pkg = context.packageName
                val market = Uri.parse("market://details?id=$pkg")
                val web = Uri.parse("https://play.google.com/store/apps/details?id=$pkg")
                try {
                    context.startActivity(Intent(Intent.ACTION_VIEW, market))
                } catch (_: ActivityNotFoundException) {
                    context.startActivity(Intent(Intent.ACTION_VIEW, web))
                }
            }
            is SettingsEvent.ShareApp -> {
                val send = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, event.text)
                }
                context.startActivity(Intent.createChooser(send, null))
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onAction(SettingsAction.OnInitialLoad)
    }

    val windowSizeClass = calculateWindowSizeClass(context as android.app.Activity)

    SettingsScreen(
        state = state,
        widthSizeClass = windowSizeClass.widthSizeClass,
        onNavigateUp = onNavigateUp,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsState,
    widthSizeClass: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    onNavigateUp: () -> Unit = {},
    onAction: (SettingsAction) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.title_settings),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, stringResource(R.string.content_description_back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.padding(padding).fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            val contentModifier = if (widthSizeClass == WindowWidthSizeClass.Compact) {
                Modifier.fillMaxWidth()
            } else {
                Modifier.widthIn(max = 640.dp)
            }

            Box(modifier = contentModifier) {
                SettingsScreenContent(
                    state = state,
                    onAction = onAction
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    YogaAITheme {
        SettingsScreen(
            state = SettingsState(
                themeMode = 0,
                language = "English",
                dynamicColor = false,
                userName = "Alex",
                userAge = 30,
                userLevel = "Beginner",
                appVersionName = "1.0",
                appVersionCode = 1L
            )
        )
    }
}
