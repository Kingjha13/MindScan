package com.example.mindscan.presentation.ui.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mindscan.domain.model.JournalEntry
import com.example.mindscan.presentation.theme.moodScoreToColor
import com.example.mindscan.presentation.viewmodel.JournalUiState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: JournalUiState,
    onNewEntryClick: () -> Unit,
    onEntryClick: (JournalEntry) -> Unit,
    onInsightsClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("MindScan", fontWeight = FontWeight.Bold)
                        Text("Your AI Journal", style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                },
                actions = {
                    IconButton(onClick = onInsightsClick) {
                        Icon(Icons.Default.Analytics, contentDescription = "Insights")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = onNewEntryClick,
                icon = { Icon(Icons.Default.Edit, contentDescription = null) },
                text = { Text("New Entry") },
                containerColor = MaterialTheme.colorScheme.primary
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                MoodSummaryCard(
                    weeklyAvg = uiState.weeklyMoodAvg,
                    monthlyAvg = uiState.monthlyMoodAvg,
                    entryCount = uiState.entries.size
                )
            }

            item {
                Text(
                    "Recent Entries",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (uiState.entries.isEmpty()) {
                item { EmptyState(onNewEntryClick) }
            } else {
                items(uiState.entries) { entry ->
                    JournalEntryCard(entry = entry, onClick = { onEntryClick(entry) })
                }
            }
        }
    }
}

@Composable
fun MoodSummaryCard(
    weeklyAvg: Float?,
    monthlyAvg: Float?,
    entryCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Mood Overview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MoodStat(
                    label = "This Week",
                    value = weeklyAvg?.let { "${(it * 100).toInt()}%" } ?: "—",
                    color = weeklyAvg?.let { moodScoreToColor(it) } ?: Color.Gray
                )
                MoodStat(
                    label = "This Month",
                    value = monthlyAvg?.let { "${(it * 100).toInt()}%" } ?: "—",
                    color = monthlyAvg?.let { moodScoreToColor(it) } ?: Color.Gray
                )
                MoodStat(
                    label = "Total Entries",
                    value = entryCount.toString(),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            if (weeklyAvg != null) {
                Spacer(Modifier.height(16.dp))
                Text(
                    "Weekly mood",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { weeklyAvg },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = moodScoreToColor(weeklyAvg),
                    trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                )
            }
        }
    }
}

@Composable
fun MoodStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold, color = color)
        Text(label, style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
    }
}

@Composable
fun JournalEntryCard(entry: JournalEntry, onClick: () -> Unit) {
    val moodColor = moodScoreToColor(entry.moodScore)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(moodColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    moodEmoji(entry.mood),
                    fontSize = 24.sp
                )
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(entry.mood, fontWeight = FontWeight.SemiBold,
                        color = moodColor)
                    Text(
                        formatDate(entry.timestamp),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    entry.content,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    entry.emotions.take(2).forEach { emotion ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text(emotion, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState(onNewEntry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("🧠", fontSize = 48.sp)
        Spacer(Modifier.height(16.dp))
        Text("No entries yet", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(
            "Write your first journal entry and let AI understand your mood",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(Modifier.height(20.dp))
        Button(onClick = onNewEntry) {
            Text("Write First Entry")
        }
    }
}

fun moodEmoji(mood: String): String = when (mood.lowercase()) {
    "happy", "joyful", "excited", "elated" -> "😊"
    "sad", "depressed", "unhappy" -> "😢"
    "anxious", "worried", "nervous", "stressed" -> "😰"
    "angry", "frustrated", "irritated" -> "😠"
    "calm", "peaceful", "relaxed" -> "😌"
    "grateful", "thankful" -> "🙏"
    "hopeful", "optimistic" -> "✨"
    "tired", "exhausted" -> "😴"
    else -> "🤔"
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
