package com.example.mindscan.presentation.ui.entry

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindscan.domain.model.MoodAnalysis
import com.example.mindscan.presentation.theme.moodScoreToColor
import com.example.mindscan.presentation.ui.home.moodEmoji
import com.example.mindscan.presentation.viewmodel.JournalUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalEntryScreen(
    uiState: JournalUiState,
    onTextChanged: (String) -> Unit,
    onAnalyzeClick: () -> Unit,
    onNewEntry: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Entry") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "How are you feeling today?",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                "Write freely — our AI will understand your mood and provide insights.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            OutlinedTextField(
                value = uiState.journalText,
                onValueChange = onTextChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 200.dp),
                placeholder = {
                    Text(
                        "Today I felt... I've been thinking about...\nWhat happened today...",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                },
                shape = RoundedCornerShape(16.dp),
                enabled = !uiState.isAnalyzing && uiState.analysis == null
            )

            Text(
                "${uiState.journalText.split("\\s+".toRegex()).filter { it.isNotBlank() }.size} words",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                modifier = Modifier.align(Alignment.End)
            )

            uiState.error?.let { error ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Error, contentDescription = null,
                            tint = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.width(8.dp))
                        Text(error, color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                }
            }

            AnimatedVisibility(visible = uiState.analysis == null) {
                Button(
                    onClick = onAnalyzeClick,
                    enabled = uiState.journalText.length > 10 && !uiState.isAnalyzing,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    if (uiState.isAnalyzing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Analyzing with AI...")
                    } else {
                        Icon(Icons.Default.Psychology, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Analyze My Mood")
                    }
                }
            }

            AnimatedVisibility(
                visible = uiState.analysis != null,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn()
            ) {
                uiState.analysis?.let { analysis ->
                    AiAnalysisCard(
                        analysis = analysis,
                        isSaved = uiState.isSaved,
                        onNewEntry = onNewEntry
                    )
                }
            }
        }
    }
}

@Composable
fun AiAnalysisCard(
    analysis: MoodAnalysis,
    isSaved: Boolean,
    onNewEntry: () -> Unit
) {
    val moodColor = moodScoreToColor(analysis.moodScore)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = moodColor.copy(alpha = 0.08f)
        ),
        border = BorderStroke(1.dp, moodColor.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(moodColor.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(moodEmoji(analysis.mood), fontSize = 28.sp)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text("AI Analysis", style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    Text(analysis.mood, style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold, color = moodColor)
                }
                Spacer(Modifier.weight(1f))
                if (isSaved) {
                    Icon(Icons.Default.CheckCircle, contentDescription = "Saved",
                        tint = Color(0xFF4CAF50))
                }
            }

            Spacer(Modifier.height(16.dp))

            Text("Mood Score", style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                LinearProgressIndicator(
                    progress = { analysis.moodScore },
                    modifier = Modifier.weight(1f).height(10.dp).clip(RoundedCornerShape(5.dp)),
                    color = moodColor,
                    trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
                Spacer(Modifier.width(8.dp))
                Text("${(analysis.moodScore * 100).toInt()}%",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold, color = moodColor)
            }

            Spacer(Modifier.height(16.dp))

            Text("Detected Emotions", style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.horizontalScroll(rememberScrollState())) {
                analysis.emotions.forEach { emotion ->
                    AssistChip(
                        onClick = {},
                        label = { Text(emotion) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = moodColor.copy(alpha = 0.15f)
                        )
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = moodColor.copy(alpha = 0.2f))
            Spacer(Modifier.height(16.dp))

            Row {
                Icon(Icons.Default.Lightbulb, contentDescription = null,
                    tint = Color(0xFFFF9800), modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text("Insight", style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold, color = Color(0xFFFF9800))
            }
            Spacer(Modifier.height(8.dp))
            Text(
                analysis.insight,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 22.sp
            )

            Spacer(Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(Icons.Default.TipsAndUpdates, contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp).padding(top = 2.dp))
                    Spacer(Modifier.width(8.dp))
                    Column {
                        Text("Today's Suggestion", style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary)
                        Spacer(Modifier.height(4.dp))
                        Text(analysis.suggestion,
                            style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            OutlinedButton(
                onClick = onNewEntry,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Write Another Entry")
            }
        }
    }
}
