package com.example.mindscan.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mindscan.presentation.ui.entry.JournalEntryScreen
import com.example.mindscan.presentation.ui.home.HomeScreen
import com.example.mindscan.presentation.viewmodel.JournalViewModel

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object NewEntry : Screen("new_entry")
}

@Composable
fun MindScanNavGraph(
    navController: NavHostController = rememberNavController(),
    viewModel: JournalViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    NavHost(navController = navController, startDestination = Screen.Home.route) {

        composable(Screen.Home.route) {
            HomeScreen(
                uiState = uiState,
                onNewEntryClick = { navController.navigate(Screen.NewEntry.route) },
                onEntryClick = { /* Navigate to detail */ },
                onInsightsClick = { /* Navigate to insights */ }
            )
        }

        composable(Screen.NewEntry.route) {
            JournalEntryScreen(
                uiState = uiState,
                onTextChanged = viewModel::onTextChanged,
                onAnalyzeClick = viewModel::analyzeAndSave,
                onNewEntry = viewModel::resetEntry,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
