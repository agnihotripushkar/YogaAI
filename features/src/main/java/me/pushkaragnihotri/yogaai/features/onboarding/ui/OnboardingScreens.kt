
package me.pushkaragnihotri.yogaai.features.onboarding.ui

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.health.connect.client.PermissionController
import androidx.health.connect.client.HealthConnectClient
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import me.pushkaragnihotri.yogaai.core.data.HealthConnectManager

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme
import org.koin.androidx.compose.koinViewModel
import androidx.compose.ui.res.stringResource
import me.pushkaragnihotri.yogaai.features.R

@Composable
fun OnboardingScreen(
    viewModel: OnboardingViewModel = koinViewModel(),
    onOnboardingComplete: () -> Unit
) {
    val currentStep by viewModel.currentStep.collectAsState()
    
    AnimatedContent(targetState = currentStep, label = "OnboardingNav") { step ->
        when (step) {
            OnboardingStep.SPLASH -> SplashScreen(onFinished = viewModel::onSplashFinished)
            OnboardingStep.INTRO -> IntroScreen(onFinished = viewModel::onIntroFinished)
            OnboardingStep.CONSENT -> ConsentScreen(onConsentGiven = viewModel::onConsentGranted)
            OnboardingStep.CONNECT -> ConnectScreen(viewModel = viewModel, onFinished = viewModel::onConnectFinished)
            OnboardingStep.PROFILE -> ProfileScreen(onFinished = { name, age, level ->
                viewModel.onProfileFinished(name, age, level)
                onOnboardingComplete()
            })
        }
    }
}

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onFinished()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Rounded.SelfImprovement,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(Modifier.height(16.dp))
            Text(
                stringResource(R.string.app_name_splash),
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                stringResource(R.string.app_tagline),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}


@Composable
fun IntroScreen(onFinished: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                IntroPage(
                    page = page,
                    onNext = {
                        scope.launch { pagerState.animateScrollToPage(page + 1) }
                    },
                    onFinished = onFinished,
                    isLastPage = page == 2
                )
            }
            
            // Pager Indicator
            Row(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(3) { iteration ->
                    val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                    )
                }
            }
        }

        // Skip Button
        TextButton(
            onClick = onFinished,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Text(stringResource(R.string.connect_skip)) // Reusing existing skip string or should I add a new one? "Skip" is generic.
        }
    }
}

@Composable
fun IntroPage(
    page: Int,
    onNext: () -> Unit,
    onFinished: () -> Unit,
    isLastPage: Boolean
) {
    val title = when (page) {
        0 -> stringResource(R.string.intro_title_1)
        1 -> stringResource(R.string.intro_title_2)
        2 -> stringResource(R.string.intro_title_3)
        else -> ""
    }
    val description = when (page) {
        0 -> stringResource(R.string.intro_desc_1)
        1 -> stringResource(R.string.intro_desc_2)
        2 -> stringResource(R.string.intro_desc_3)
        else -> ""
    }
    val icon = when (page) {
        0 -> Icons.Rounded.Insights
        1 -> Icons.Rounded.Security
        2 -> Icons.Rounded.Warning
        else -> Icons.Rounded.Info
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(MaterialTheme.colorScheme.secondaryContainer, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
        
        Spacer(Modifier.height(48.dp))
        
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(Modifier.height(16.dp))
        
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(Modifier.height(48.dp))
        
        if (isLastPage) {
            Button(
                onClick = onFinished,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(stringResource(R.string.intro_button_start))
            }
        } else {
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(stringResource(R.string.intro_button_next))
            }
        }
    }
}

@Composable
fun ConsentScreen(onConsentGiven: () -> Unit) {
    var checked by remember { mutableStateOf(false) }
    
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Spacer(Modifier.height(24.dp))
        Icon(Icons.Rounded.VerifiedUser, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(48.dp))
        Spacer(Modifier.height(16.dp))
        Text(stringResource(R.string.consent_title), style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))
        
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp).verticalScroll(rememberScrollState())) {
                Text(
                    stringResource(R.string.consent_terms),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        Spacer(Modifier.height(16.dp))
        
        Row(verticalAlignment = Alignment.Top) {
            Checkbox(checked = checked, onCheckedChange = { checked = it })
            Text(
                stringResource(R.string.consent_checkbox),
                modifier = Modifier.padding(top = 12.dp, start = 8.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        Spacer(Modifier.height(24.dp))
        
        Button(
            onClick = onConsentGiven,
            enabled = checked,
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text(stringResource(R.string.consent_button))
        }
    }
}

@Composable
fun ConnectScreen(viewModel: OnboardingViewModel, onFinished: () -> Unit) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = PermissionController.createRequestPermissionResultContract(),
        onResult = viewModel::onPermissionsResult
    )

    ConnectScreen(
        onConnectClick = { 
            val availability = viewModel.getHealthConnectAvailability()
            if (availability == HealthConnectManager.SDK_AVAILABLE) {
                permissionLauncher.launch(viewModel.permissions)
            } else {
                Toast.makeText(context, "Health Connect is not available on this device.", Toast.LENGTH_LONG).show()
            }
        },
        onSkipClick = onFinished
    )
}

@Composable
fun ConnectScreen(onConnectClick: () -> Unit, onSkipClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Rounded.MonitorHeart, null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(80.dp))
        Spacer(Modifier.height(32.dp))
        Text(stringResource(R.string.connect_title), style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Text(
            stringResource(R.string.connect_desc),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(48.dp))
        
        Button(
            onClick = onConnectClick,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
        ) {
            Icon(Icons.Rounded.Sync, null)
            Spacer(Modifier.width(8.dp))
            Text(stringResource(R.string.connect_button))
        }
        
        Spacer(Modifier.height(16.dp))
        
        TextButton(onClick = onSkipClick) {
            Text(stringResource(R.string.connect_skip))
        }
    }
}

@Composable
fun ProfileScreen(onFinished: (String, Int, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var level by remember { mutableStateOf("Beginner") }
    
    val levels = listOf(stringResource(R.string.profile_level_beginner), stringResource(R.string.profile_level_intermediate), stringResource(R.string.profile_level_advanced))
    
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
            onValueChange = { name = it },
            label = { Text(stringResource(R.string.profile_name_label)) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = { Icon(Icons.Rounded.Person, null) }
        )
        
        Spacer(Modifier.height(16.dp))
        
        OutlinedTextField(
            value = age,
            onValueChange = { if (it.all { char -> char.isDigit() }) age = it },
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
                    onClick = { level = item },
                    label = { Text(item) }
                )
            }
        }
        
        Spacer(Modifier.weight(1f))
        
        Button(
            onClick = { 
                val ageInt = age.toIntOrNull() ?: 0
                onFinished(name, ageInt, level)
            },
            enabled = name.isNotBlank(),
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Text(stringResource(R.string.profile_complete_button))
        }
    }
}

@DevicePreviews
@Composable
fun SplashScreenPreview() {
    YogaAITheme {
        SplashScreen(onFinished = {})
    }
}

@DevicePreviews
@Composable
fun IntroScreenPreview() {
    YogaAITheme {
        IntroScreen(onFinished = {})
    }
}

@DevicePreviews
@Composable
fun ConsentScreenPreview() {
    YogaAITheme {
        ConsentScreen(onConsentGiven = {})
    }
}

@DevicePreviews
@Composable
fun ConnectScreenPreview() {
    YogaAITheme {
        ConnectScreen(onConnectClick = {}, onSkipClick = {})
    }
}

@DevicePreviews
@Composable
fun OnboardingProfileScreenPreview() {
    YogaAITheme {
        ProfileScreen(onFinished = { _, _, _ -> })
    }
}
