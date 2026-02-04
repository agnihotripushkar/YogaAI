package me.pushkaragnihotri.yogaai.features.onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun OnboardingScreen(
    onOnboardingComplete: () -> Unit
) {
    IntroScreen(onFinished = onOnboardingComplete)
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
            Text(stringResource(R.string.connect_skip))
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

@DevicePreviews
@Composable
fun IntroScreenPreview() {
    YogaAITheme {
        IntroScreen(onFinished = {})
    }
}

@Preview
@Composable
fun IntroPagePreview() {
    YogaAITheme {
        IntroPage(
            page = 0,
            onNext = {},
            onFinished = {},
            isLastPage = false
        )
    }
}
