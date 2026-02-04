package me.pushkaragnihotri.yogaai.features.consent.ui

import androidx.compose.runtime.*
import me.pushkaragnihotri.yogaai.features.consent.ui.components.ConsentScreenContent
import androidx.compose.ui.tooling.preview.Preview
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme

@Composable
fun ConsentScreen(onConsentGiven: () -> Unit) {
    var checked by remember { mutableStateOf(false) }
    
    ConsentScreenContent(
        checked = checked,
        onCheckedChange = { checked = it },
        onConsentGiven = onConsentGiven
    )
}

@Preview
@Composable
fun ConsentScreenPreview() {
    YogaAITheme {
        ConsentScreen(onConsentGiven = {})
    }
}
