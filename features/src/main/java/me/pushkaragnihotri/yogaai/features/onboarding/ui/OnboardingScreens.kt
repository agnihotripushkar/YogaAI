package me.pushkaragnihotri.yogaai.features.onboarding.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

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
            OnboardingStep.CONNECT -> ConnectScreen(onFinished = viewModel::onConnectFinished)
            OnboardingStep.PROFILE -> ProfileScreen(onFinished = {
                viewModel.onProfileFinished()
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
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Wellness Risk", style = MaterialTheme.typography.displayLarge)
            Text("Predictive Wellness Insights, Not Just Numbers", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun IntroScreen(onFinished: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (page) {
                    0 -> {
                        Text("What this app does", style = MaterialTheme.typography.headlineMedium)
                        Spacer(Modifier.height(16.dp))
                        Text("Converts wearable data into early warnings for fatigue/sleep issues.", textAlign = TextAlign.Center)
                    }
                    1 -> {
                        Text("How your data is used", style = MaterialTheme.typography.headlineMedium)
                        Spacer(Modifier.height(16.dp))
                        Text("Data minimization, pseudonymous IDs, and no sharing without consent.", textAlign = TextAlign.Center)
                    }
                    2 -> {
                        Text("Disclaimer", style = MaterialTheme.typography.headlineMedium)
                        Spacer(Modifier.height(16.dp))
                        Text("This is NOT medical advice.", textAlign = TextAlign.Center)
                    }
                }
            }
        }
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (pagerState.currentPage < 2) {
                TextButton(onClick = onFinished) { Text("Skip") }
                Button(onClick = { 
                    scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) } 
                }) { Text("Next") }
            } else {
                Spacer(Modifier.width(16.dp))
                Button(onClick = onFinished, modifier = Modifier.fillMaxWidth()) { Text("Get Started") }
            }
        }
    }
}

@Composable
fun ConsentScreen(onConsentGiven: () -> Unit) {
    var checked by remember { mutableStateOf(false) }
    
    Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
        Text("Consent", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))
        Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Text("We need your permission to process your health data. " +
                    "Your data will be stored locally and used only for providing wellness insights. " +
                    "You can withdraw consent at any time by deleting your data.")
            // Add more legal text here
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = checked, onCheckedChange = { checked = it })
            Text("I agree to participate and share my data.")
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onConsentGiven,
            enabled = checked,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue")
        }
    }
}

@Composable
fun ConnectScreen(onFinished: () -> Unit) {
    // In a real app, check permissions status
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Connect Data Source", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Text("We need access to Steps, Sleep, and Heart Rate.", textAlign = TextAlign.Center)
        Spacer(Modifier.height(24.dp))
        
        Button(onClick = { /* TODO: Launch Health Connect Permission Request */ }) {
            Text("Connect Health Connect")
        }
        Spacer(Modifier.height(16.dp))
        OutlinedButton(onClick = onFinished) {
            Text("Continue") // Allow skipping/proceeding after connect
        }
    }
}

@Composable
fun ProfileScreen(onFinished: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Profile Setup", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(16.dp))
        Text("Optional: Tell us a bit about yourself.", textAlign = TextAlign.Center)
        // Add form fields if needed
        Spacer(Modifier.height(24.dp))
        Button(onClick = onFinished) {
            Text("Finish")
        }
    }
}
