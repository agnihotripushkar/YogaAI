package me.pushkaragnihotri.yogaai.features.ui.theme

import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/** Emphasis cards (e.g. risk / alerts) — asymmetric, Material-forward. */
val ExpressiveCutCardShape = CutCornerShape(topStart = 20.dp, bottomEnd = 24.dp)

val Shapes = Shapes(
    // Chips, small badges
    extraSmall = RoundedCornerShape(8.dp),
    // Input fields, small cards
    small = RoundedCornerShape(16.dp),
    // Standard cards, dialogs
    medium = RoundedCornerShape(28.dp),
    // Hero cards, bottom sheets
    large = RoundedCornerShape(36.dp),
    // Full pill buttons, FABs
    extraLarge = RoundedCornerShape(50.dp)
)
