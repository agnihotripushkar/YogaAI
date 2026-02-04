package me.pushkaragnihotri.yogaai.features.profilesetup.ui

import androidx.compose.runtime.*
import me.pushkaragnihotri.yogaai.features.profilesetup.ui.components.ProfileSetupScreenContent
import androidx.compose.ui.tooling.preview.Preview
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme

@Composable
fun ProfileSetupScreen(onFinished: (String, Int, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var level by remember { mutableStateOf("Beginner") }
    
    ProfileSetupScreenContent(
        name = name,
        onNameChange = { name = it },
        age = age,
        onAgeChange = { if (it.all { char -> char.isDigit() }) age = it },
        level = level,
        onLevelChange = { level = it },
        onCompleteClick = { 
            val ageInt = age.toIntOrNull() ?: 0
            onFinished(name, ageInt, level)
        }
    )
}

@Preview
@Composable
fun ProfileSetupScreenPreview() {
    YogaAITheme {
        ProfileSetupScreen(onFinished = { _, _, _ -> })
    }
}
