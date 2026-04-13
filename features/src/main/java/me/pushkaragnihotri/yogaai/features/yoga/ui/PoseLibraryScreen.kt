package me.pushkaragnihotri.yogaai.features.yoga.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import me.pushkaragnihotri.yogaai.features.R
import me.pushkaragnihotri.yogaai.features.common.ui.DevicePreviews
import me.pushkaragnihotri.yogaai.features.ui.theme.YogaAITheme
import me.pushkaragnihotri.yogaai.features.yoga.domain.model.PoseRepository
import androidx.compose.ui.unit.sp
import me.pushkaragnihotri.yogaai.features.ui.theme.PlayfairFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PoseLibraryScreen(onNavigateUp: () -> Unit) {
    val poses = PoseRepository.libraryPoses()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.pose_library_title),
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            Icons.AutoMirrored.Rounded.ArrowBack,
                            stringResource(R.string.content_description_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.pose_library_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(8.dp))
            }
            items(poses, key = { it.name }) { pose ->
                PoseLibraryCard(poseName = pose.name, sanskrit = pose.sanskritName, meaning = pose.meaning)
            }
        }
    }
}

@Composable
private fun PoseLibraryCard(poseName: String, sanskrit: String, meaning: String) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = poseName,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontFamily = PlayfairFamily,
                    fontSize = 24.sp
                ),
                color = MaterialTheme.colorScheme.primary
            )
            if (sanskrit.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = sanskrit,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (meaning.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = meaning,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@DevicePreviews
@Composable
private fun PoseLibraryScreenPreview() {
    YogaAITheme {
        PoseLibraryScreen(onNavigateUp = {})
    }
}
