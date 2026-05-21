package com.example.mindscan.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mindscan.domain.model.JournalEntry
import com.example.mindscan.utils.Result
import com.example.mindscan.domain.model.MoodAnalysis
import com.example.mindscan.domain.usecase.AnalyzeMoodUseCase
import com.example.mindscan.domain.usecase.GetJournalHistoryUseCase
import com.example.mindscan.domain.usecase.GetMoodStatsUseCase
import com.example.mindscan.domain.usecase.SaveJournalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class JournalUiState(
    val journalText: String = "",
    val isAnalyzing: Boolean = false,
    val analysis: MoodAnalysis? = null,
    val isSaved: Boolean = false,
    val error: String? = null,
    val entries: List<JournalEntry> = emptyList(),
    val weeklyMoodAvg: Float? = null,
    val monthlyMoodAvg: Float? = null
)

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val analyzeMoodUseCase: AnalyzeMoodUseCase,
    private val saveJournalUseCase: SaveJournalUseCase,
    private val getHistoryUseCase: GetJournalHistoryUseCase,
    private val getMoodStatsUseCase: GetMoodStatsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(JournalUiState())
    val uiState: StateFlow<JournalUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getHistoryUseCase().collect { entries ->
                _uiState.update { it.copy(entries = entries) }
            }
        }
        loadMoodStats()
    }

    fun onTextChanged(text: String) {
        _uiState.update { it.copy(journalText = text, error = null, analysis = null, isSaved = false) }
    }

    fun analyzeAndSave() {

        val text = _uiState.value.journalText

        if (text.isBlank()) {
            _uiState.update {
                it.copy(error = "Journal text cannot be empty")
            }
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isAnalyzing = true,
                    error = null,
                    isSaved = false
                )
            }

            when (val result = analyzeMoodUseCase(text)) {

                is Result.Success<*> -> {

                    val analysis = result.data as MoodAnalysis

                    _uiState.update {
                        it.copy(
                            analysis = analysis,
                            isAnalyzing = false
                        )
                    }

                    when (val saveResult = saveJournalUseCase(text, analysis)) {

                        is Result.Success<*> -> {

                            _uiState.update {
                                it.copy(
                                    isSaved = true
                                )
                            }

                            loadMoodStats()
                        }

                        is Result.Error -> {

                            _uiState.update {
                                it.copy(
                                    isAnalyzing = false,
                                    error = "Save failed: ${saveResult.message}"
                                )
                            }
                        }

                        is Result.Loading -> {
                        }
                    }
                }

                is Result.Error -> {

                    _uiState.update {
                        it.copy(
                            isAnalyzing = false,
                            error = result.message
                        )
                    }
                }

                is Result.Loading -> {
                }
            }
        }
    }

    fun resetEntry() {
        _uiState.update { it.copy(journalText = "", analysis = null, isSaved = false, error = null) }
    }

    private fun loadMoodStats() {
        viewModelScope.launch {
            val weekly = getMoodStatsUseCase.weeklyAverage()
            val monthly = getMoodStatsUseCase.monthlyAverage()
            _uiState.update { it.copy(weeklyMoodAvg = weekly, monthlyMoodAvg = monthly) }
        }
    }
}

