package me.pushkaragnihotri.yogaai.features.onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme

@Composable
fun OnboardingScreen(
    windowSizeClass: WindowWidthSizeClass,
    onOnboardingComplete: () -> Unit
) {
    IntroScreen(
        windowSizeClass = windowSizeClass,
        onFinished = onOnboardingComplete
    )
}

@Composable
fun IntroScreen(
    windowSizeClass: WindowWidthSizeClass,
    onFinished: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()
    
    Box(modifier = Modifier.fillMaxSize()) {
        val isExpanded = windowSizeClass != WindowWidthSizeClass.Compact
        
        // Main Content Pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            IntroPage(
                page = page,
                pagerState = pagerState,
                onNext = {
                    scope.launch { pagerState.animateScrollToPage(page + 1) }
                },
                onFinished = onFinished,
                isLastPage = page == 2,
                isExpanded = isExpanded
            )
        }
            
        // Pager Indicator logic based on layout mode
        if (isExpanded) {
            // Expanded: Indicator at bottom center of the LEFT PANE (Image side)
            // Left pane is 50% width.
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f) // Matches Left Pane width
                    .fillMaxHeight()
                    .align(Alignment.CenterStart) // Left side container
            ) {
                Row(
                    Modifier
                        .wrapContentHeight()
                        .fillMaxWidth()
                        .padding(bottom = 32.dp)
                        .align(Alignment.BottomCenter), // Bottom of the image pane
                    horizontalArrangement = Arrangement.Center
                ) {
                    IndicatorDots(pagerState = pagerState)
                }
            }
        }


    }
}

@Composable
fun IndicatorDots(pagerState: androidx.compose.foundation.pager.PagerState) {
    repeat(3) { iteration ->
        val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        val width = if (pagerState.currentPage == iteration) 32.dp else 8.dp
        Box(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .clip(CircleShape)
                .background(color)
                .height(8.dp)
                .width(width)
        )
    }
}

@Composable
fun IntroPage(
    page: Int,
    pagerState: androidx.compose.foundation.pager.PagerState,
    onNext: () -> Unit,
    onFinished: () -> Unit,
    isLastPage: Boolean,
    isExpanded: Boolean = false
) {
    val title = when (page) {
        0 -> stringResource(R.string.intro_title_2)
        1 ->stringResource(R.string.intro_title_1)
        2 -> stringResource(R.string.intro_title_3)
        else -> ""
    }
    val description = when (page) {
        0 ->stringResource(R.string.intro_desc_2)
        1 ->stringResource(R.string.intro_desc_1)
        2 -> stringResource(R.string.intro_desc_3)
        else -> ""
    }
    
    val imageRes = when (page) {
        0 -> R.drawable.onboarding_3
        1 -> R.drawable.onboarding_2
        2 -> R.drawable.onboarding_1
        else -> R.drawable.onboarding_2
    }

    if (isExpanded) {
        IntroPageExpanded(
            title = title,
            description = description,
            imageRes = imageRes,
            onNext = onNext,
            onFinished = onFinished,
            isLastPage = isLastPage
        )
    } else {
        IntroPageCompact(
            title = title,
            description = description,
            imageRes = imageRes,
            pagerState = pagerState,
            onNext = onNext,
            onFinished = onFinished,
            isLastPage = isLastPage
        )
    }
}

@Composable
fun IntroPageExpanded(
    title: String,
    description: String,
    imageRes: Int,
    onNext: () -> Unit,
    onFinished: () -> Unit,
    isLastPage: Boolean
) {
    // Expanded: Row Layout (Image | Text)
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Image
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop, // Crop fills the pane
                alignment = Alignment.Center
            )
            
            // Skip Button (Text) - Top Right of Image
            if (!isLastPage) {
                TextButton(
                    onClick = onFinished,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.connect_skip),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant // Visible on image? Maybe need shadow or specific color
                        // Assuming image allows visibility. Usually onSurface or InversePrimary
                    )
                }
            }
        }
        
        // Right: Text Content
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
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
            
            val buttonModifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
            
            if (isLastPage) {
                Button(
                    onClick = onFinished,
                    modifier = buttonModifier,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(stringResource(R.string.intro_button_start))
                }
            } else {
                Button(
                    onClick = onNext,
                    modifier = buttonModifier,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(stringResource(R.string.intro_button_next))
                }
            }
        }
    }
}

@Composable
fun IntroPageCompact(
    title: String,
    description: String,
    imageRes: Int,
    pagerState: androidx.compose.foundation.pager.PagerState,
    onNext: () -> Unit,
    onFinished: () -> Unit,
    isLastPage: Boolean
) {
    // Compact: Column Layout (Image Top, Text Bottom)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background), // Ensure background is set
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        
        // Image Container - Top 70% (increased to show more image)
        Box(
            modifier = Modifier
                .weight(0.7f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter
        ) {
            androidx.compose.foundation.Image(
                painter = androidx.compose.ui.res.painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = androidx.compose.ui.layout.ContentScale.Crop, // Crop to fill
                alignment = Alignment.TopCenter // Align top to avoid cutting head
            )
            
             // Skip Button (Text) - Top Right of Image
            if (!isLastPage) {
                TextButton(
                    onClick = onFinished,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 48.dp, end = 16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.connect_skip),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface 
                    )
                }
            }
        }
        
        // Text Content / Bottom Sheet - Bottom 30%
        Column(
            modifier = Modifier
                .weight(0.3f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                .background(MaterialTheme.colorScheme.surface) // Bottom sheet background
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top 
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(Modifier.height(8.dp))
            
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Spacer(Modifier.weight(1f))
            
            // Indicator Dots
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                 IndicatorDots(pagerState = pagerState)
            }

             val buttonModifier = Modifier
                .fillMaxWidth()
                .height(56.dp)

             if (isLastPage) {
                Button(
                    onClick = onFinished,
                    modifier = buttonModifier,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(stringResource(R.string.intro_button_start))
                }
            } else {
                Button(
                    onClick = onNext,
                    modifier = buttonModifier,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(stringResource(R.string.intro_button_next))
                }
            }
        }
    }
}

@DevicePreviews
@Composable
fun IntroScreenPreview() {
    YogaAITheme {
        IntroScreen(
            windowSizeClass = WindowWidthSizeClass.Compact,
            onFinished = {}
        )
    }
}

@Preview(name = "Compact", widthDp = 360, heightDp = 640)
@Preview(name = "Expanded", widthDp = 840, heightDp = 600)
@Composable
fun IntroPagePreview() {
    val pagerState = rememberPagerState { 3 }
    YogaAITheme {
        IntroPage(
            page = 0,
            pagerState = pagerState,
            onNext = {},
            onFinished = {},
            isLastPage = false
        )
    }
}
