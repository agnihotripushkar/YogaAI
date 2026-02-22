package me.pushkaragnihotri.yogaai.features.common.ui

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(name = "Phone", device = "spec:width=411dp,height=891dp", showBackground = true)
@Preview(name = "Tablet", device = "spec:width=1280dp,height=800dp,dpi=240", showBackground = true)
//@Preview(name = "Desktop", device = "spec:width=1920dp,height=1080dp,dpi=160", showBackground = true)
@Preview(name = "Foldable", device = "spec:width=673dp,height=841dp,dpi=420", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
annotation class DevicePreviews