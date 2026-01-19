package me.pushkaragnihotri.yogaai.features.classes.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.common.ui.theme.YogaAITheme

@Composable
fun ClassesScreen() {
    Text(androidx.compose.ui.res.stringResource(me.pushkaragnihotri.yogaai.features.R.string.classes_placeholder))
}

@DevicePreviews
@Composable
fun ClassesScreenPreview() {
    YogaAITheme {
        ClassesScreen()
    }
}

