package me.pushkaragnihotri.yogaai.features.yoga.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.stringResource
import me.pushkaragnihotri.yogaai.features.ui.theme.YogaAITheme
import me.pushkaragnihotri.yogaai.features.yoga.model.PoseRepository
import me.pushkaragnihotri.yogaai.features.R

@Composable
fun PoseResultScreen(
    poseName: String,
    duration: String,
    feedback: String,
    onHomeClick: () -> Unit
) {
    val poseDetail = PoseRepository.getPoseDetail(poseName)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.result_title),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Result Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = poseName,
                    style = MaterialTheme.typography.headlineMedium
                )
                if (poseDetail.sanskritName.isNotEmpty()) {
                    Text(
                        text = poseDetail.sanskritName,
                        style = MaterialTheme.typography.titleMedium,
                         fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                
                // Duration & Feedback
                Row(
                   modifier = Modifier.fillMaxWidth(),
                   horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                     Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(stringResource(R.string.result_label_duration), style = MaterialTheme.typography.labelLarge)
                        Text("$duration s", style = MaterialTheme.typography.titleLarge)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = feedback,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = if (feedback.contains("Great")) Color(0xFF006400) else MaterialTheme.colorScheme.error // Consider extracting 0xFF006400 (DarkGreen)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Detail Sections
        if (poseDetail.benefits.isNotEmpty()) {
            DetailSection(title = stringResource(R.string.result_section_benefits), items = poseDetail.benefits)
        }
        
        if (poseDetail.alignmentCues.isNotEmpty()) {
             DetailSection(title = stringResource(R.string.result_section_alignment), items = poseDetail.alignmentCues)
        }
        
        if (poseDetail.instructions.isNotEmpty()) {
             DetailSection(title = stringResource(R.string.result_section_instructions), items = poseDetail.instructions, isOrdered = true)
        }

         if (poseDetail.contraindications.isNotEmpty()) {
             DetailSection(title = stringResource(R.string.result_section_contraindications), items = poseDetail.contraindications, bulletColor = MaterialTheme.colorScheme.error)
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Button(
            onClick = onHomeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.result_back_home))
        }
    }
}

@Composable
fun DetailSection(
    title: String, 
    items: List<String>, 
    isOrdered: Boolean = false,
    bulletColor: Color = MaterialTheme.colorScheme.primary
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        items.forEachIndexed { index, item ->
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                Text(
                    text = if (isOrdered) "${index + 1}." else "•",
                    style = MaterialTheme.typography.bodyLarge,
                    color = bulletColor,
                    modifier = Modifier.width(24.dp)
                )
                Text(
                    text = item,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        Divider(modifier = Modifier.padding(vertical = 8.dp))
    }
}

@Preview
@Composable
fun PoseResultScreenPreview() {
    YogaAITheme {
        PoseResultScreen(
            poseName = "Warrior II",
            duration = "60",
            feedback = "Great job! Your form was perfect.",
            onHomeClick = {}
        )
    }
}

@Preview
@Composable
fun DetailSectionPreview() {
    YogaAITheme {
        DetailSection(
            title = "Benefits",
            items = listOf("Benefit 1", "Benefit 2", "Benefit 3")
        )
    }
}
