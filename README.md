<div align="center">

<img src="app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.webp" alt="MindScan Logo" width="100" height="100"/>

# �? MindScan � Your AI Journal

**Understand your emotions. Track your mood. Grow every day.**

MindScan is an AI-powered mood journaling Android app that analyzes your journal entries using Google Gemini AI and delivers empathetic insights, emotion detection, and personalized daily suggestions � all saved locally on your device.

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)](https://android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Gemini AI](https://img.shields.io/badge/AI-Gemini%202.5%20Flash-8E75B2?logo=google&logoColor=white)](https://ai.google.dev)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

</div>

---

## �? Screenshots

<div align="center">

|                                           �? Write Your Entry                                            |                                       �? AI Analysis Result                                        |                                            �? Home Dashboard                                             |
|:--------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------:|
| <img src="https://raw.githubusercontent.com/Kingjha13/MindScan/main/screenshots/img1.jpeg" width="220"/> | <img src="https://raw.githubusercontent.com/Kingjha13/MindScan/main/shots/img2.jpeg" width="220"/> | <img src="https://raw.githubusercontent.com/Kingjha13/MindScan/main/screenshots/img3.jpeg" width="220"/> |
|                               Write freely and let AI understand your mood                               |                             Instant mood score, emotions & AI insight                              |                                 Track your mood history and weekly stats                                 |

</div>

---

## �? Features

- �? **AI-Powered Journal** � Write freely; Gemini 2.5 Flash analyzes your emotions instantly
- �? **Mood Score** � Visual progress bar showing your positivity level (0�100%)
- �? **Empathetic Insights** � Personalized observations based on what you wrote
- �? **Actionable Suggestions** � Practical daily tips tailored to your current mood
- �? **Emotion Tags** � Detects 2�4 specific emotions per entry (e.g., Longing, Clarity, Optimism)
- �? **Mood Dashboard** � Weekly & monthly average mood overview at a glance
- �? **Offline Storage** � All entries saved locally using Room database
- �? **Dynamic Theming** � Card colors shift based on your mood score

---

## �? Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Kotlin |
| **UI** | Jetpack Compose + Material 3 |
| **Architecture** | MVVM + Clean Architecture |
| **AI / NLP** | Google Gemini 2.5 Flash (REST API) |
| **Local Database** | Room |
| **Dependency Injection** | Hilt (Dagger) |
| **Networking** | Retrofit 2 + OkHttp 4 |
| **Async** | Kotlin Coroutines + StateFlow |
| **Navigation** | Jetpack Navigation Compose |

---

## �? Project Structure

```
MindScan/
��� data/
�   ��� local/          # Room DB � entities, DAO, database
�   ��� remote/         # Gemini API service, DTOs
��� di/                 # Hilt dependency injection modules
��� domain/
�   ��� model/          # Business models (JournalEntry, MoodAnalysis)
�   ��� repository/     # Repository interface + implementation
�   ��� usecase/        # AnalyzeMood, SaveJournal, GetHistory, GetStats
��� presentation/
�   ��� theme/          # Material 3 theme, mood colors
�   ��� ui/
�   �   ��� home/       # Home screen & journal entry cards
�   �   ��� entry/      # New entry screen & AI analysis card
�   �   ��� navigation/ # NavGraph setup
�   ��� viewmodel/      # JournalViewModel + JournalUiState
��� utils/              # Result sealed class
```

---

## �? Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- Android SDK 24+
- A Google Gemini API key � [Get one here](https://aistudio.google.com/app/apikey)

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/Kingjha13/MindScan.git
   cd MindScan
   ```

2. **Add your Gemini API key**

   Open `JournalRepositoryImpl.kt` and replace the placeholder:
   ```kotlin
   private val apiKey = "YOUR_API_KEY_HERE"
   ```
   with your actual key:
   ```kotlin
   private val apiKey = "AIza..."
   ```

   > � **Security tip:** For production, store the key in `local.properties` and access it via `BuildConfig` to avoid exposing it in source control.

3. **Build and run**

   Open the project in Android Studio and click **Run** �, or:
   ```bash
   ./gradlew assembleDebug
   ```

---

## �? How It Works

```
User writes journal entry
        �
[AnalyzeMoodUseCase] validates input (min 10 chars)
        �
[JournalRepositoryImpl] builds a structured prompt
        �
[GeminiApiService] sends request to Gemini 2.5 Flash
        �
AI returns JSON � { mood, moodScore, emotions, insight, suggestion }
        �
Parsed � MoodAnalysis domain model
        �
[SaveJournalUseCase] persists entry to Room DB
        �
UI updates with animated AI Analysis Card
```

---

## �? Roadmap

- [ ] Mood trend chart (line graph over 30 days)
- [ ] Entry detail screen with full AI analysis view
- [ ] Daily reminder notification
- [ ] Export journal entries as PDF
- [ ] Dark mode support
- [ ] Secure API key via `BuildConfig`
- [ ] Widget for quick mood logging

---

## �? Contributing

Contributions are welcome! Feel free to open an issue or submit a pull request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## �? License

This project is licensed under the MIT License � see the [LICENSE](LICENSE) file for details.

---

<div align="center">

Made with � by [Kingjha13](https://github.com/Kingjha13)

*Write. Reflect. Grow.*

</div>
