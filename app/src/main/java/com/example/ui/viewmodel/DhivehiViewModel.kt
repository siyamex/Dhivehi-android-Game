package com.example.ui.viewmodel

import android.app.Application
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.GeminiService
import com.example.api.ReadingScore
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Locale

class DhivehiViewModel(application: Application) : AndroidViewModel(application), TextToSpeech.OnInitListener {

    private val db = AppDatabase.getDatabase(application)
    private val profileDao = db.userProfileDao()
    private val progressDao = db.lessonProgressDao()
    private val storyDao = db.aiStoryDao()

    private var tts: TextToSpeech? = null
    var isTtsReady = false
        private set

    // Navigation state swapping inside parent hosts
    private val _currentScreen = MutableStateFlow("splash")
    val currentScreen: StateFlow<String> = _currentScreen.asStateFlow()

    // Sub-states selection
    private val _selectedLetter = MutableStateFlow<ThaanaLetter?>(null)
    val selectedLetter: StateFlow<ThaanaLetter?> = _selectedLetter.asStateFlow()

    private val _selectedVocabCategory = MutableStateFlow("Animals")
    val selectedVocabCategory: StateFlow<String> = _selectedVocabCategory.asStateFlow()

    // AI Coaching & Story States
    private val _isGeneratingStory = MutableStateFlow(false)
    val isGeneratingStory: StateFlow<Boolean> = _isGeneratingStory.asStateFlow()

    private val _aiStoryResult = MutableStateFlow<AiStory?>(null)
    val aiStoryResult: StateFlow<AiStory?> = _aiStoryResult.asStateFlow()

    private val _aiEvaluationResult = MutableStateFlow<ReadingScore?>(null)
    val aiEvaluationResult: StateFlow<ReadingScore?> = _aiEvaluationResult.asStateFlow()

    private val _isEvaluatingReading = MutableStateFlow(false)
    val isEvaluatingReading: StateFlow<Boolean> = _isEvaluatingReading.asStateFlow()

    // Screen Time timer
    private val _timeLeftSeconds = MutableStateFlow(1800) // 30 minutes
    val timeLeftSeconds: StateFlow<Int> = _timeLeftSeconds.asStateFlow()

    private val _isScreenTimeLocked = MutableStateFlow(false)
    val isScreenTimeLocked: StateFlow<Boolean> = _isScreenTimeLocked.asStateFlow()

    init {
        // Init TTS
        tts = TextToSpeech(application, this)
        
        // Start screen timer countdown ticker representatives
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(1000)
                if (_timeLeftSeconds.value > 0) {
                    _timeLeftSeconds.value--
                } else if (!_isScreenTimeLocked.value) {
                    _isScreenTimeLocked.value = true
                }
            }
        }
    }

    // Exposing reactive flows for DB tables
    val userProfile: StateFlow<UserProfile> = profileDao.getProfileFlow()
        .map { it ?: UserProfile() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )

    val lessonProgress: StateFlow<List<LessonProgress>> = progressDao.getAllProgress()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val aiStories: StateFlow<List<AiStory>> = storyDao.getAllStoriesFlow()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun setScreen(screenName: String) {
        _currentScreen.value = screenName
    }

    fun selectLetter(letter: ThaanaLetter?) {
        _selectedLetter.value = letter
    }

    fun selectVocabCategory(category: String) {
        _selectedVocabCategory.value = category
    }

    // TTS Callback
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.US) // Standard english reading as fallback
            if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                isTtsReady = true
            }
        }
    }

    // High quality audio synthesizer or spelling speaker
    fun speakText(text: String) {
        if (isTtsReady && tts != null) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    /**
     * Complete or participate in lessons to award XP and Coins!
     */
    fun awardCompletion(category: String, lessonId: String, starsEarned: Int) {
        viewModelScope.launch {
            val progressList = lessonProgress.value
            val alreadyPresent = progressList.any { it.category == category && it.lessonId == lessonId }
            
            // 1. Save lesson progress to Room
            progressDao.saveProgress(
                LessonProgress(
                    category = category,
                    lessonId = lessonId,
                    isCompleted = true,
                    starsEarned = starsEarned
                )
            )

            // 2. Award currency & achievements in profile
            val currentProfile = profileDao.getProfileDirect() ?: UserProfile()
            
            // Base reward details
            val xpGain = if (alreadyPresent) 5 else 25
            val coinGain = if (alreadyPresent) 2 else 10
            val newXp = currentProfile.xp + xpGain
            val newCoins = currentProfile.coins + coinGain
            val newStars = currentProfile.stars + starsEarned
            val newLevel = (newXp / 100) + 1

            profileDao.saveProfile(
                currentProfile.copy(
                    xp = newXp,
                    coins = newCoins,
                    stars = newStars,
                    level = newLevel
                )
            )
        }
    }

    /**
     * Save child's onboarding selections
     */
    fun saveProfileSettings(name: String, ageGroup: Int, avatarId: String) {
        viewModelScope.launch {
            val currentProfile = profileDao.getProfileDirect() ?: UserProfile()
            profileDao.saveProfile(
                currentProfile.copy(
                    name = name,
                    ageGroup = ageGroup,
                    avatarId = avatarId
                )
            )
        }
    }

    /**
     * Reset standard screen limit
     */
    fun updateScreenLimit(minutes: Int) {
        viewModelScope.launch {
            val currentProfile = profileDao.getProfileDirect() ?: UserProfile()
            profileDao.saveProfile(
                currentProfile.copy(
                    screenTimeLimitMinutes = minutes
                )
            )
            _timeLeftSeconds.value = minutes * 60
            _isScreenTimeLocked.value = false
        }
    }

    /**
     * Unlock screen lock with pin code verification
     */
    fun unlockScreenWithPin(pin: String): Boolean {
        val configuredPin = userProfile.value.parentPin
        if (pin == configuredPin) {
            _isScreenTimeLocked.value = false
            // Grant 30 more minutes
            _timeLeftSeconds.value = 30 * 60
            return true
        }
        return false
    }

    /**
     * Generate an AI custom child story centered on Maldives!
     */
    fun searchAndGenerateStory(theme: String) {
        viewModelScope.launch {
            _isGeneratingStory.value = true
            _aiStoryResult.value = null
            try {
                val story = GeminiService.generateStory(theme)
                if (story != null) {
                    _aiStoryResult.value = story
                    // Insert into local cache
                    storyDao.insertStory(story)
                    // Reward kid with 15 XP for generation
                    val profile = profileDao.getProfileDirect() ?: UserProfile()
                    profileDao.saveProfile(
                        profile.copy(
                            xp = profile.xp + 15,
                            coins = profile.coins + 5
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e("DhivehiViewModel", "Story generation failure", e)
            } finally {
                _isGeneratingStory.value = false
            }
        }
    }

    /**
     * Evaluate spelling or pronunciations
     */
    fun evaluatePronunciation(targetDhivehi: String, kidTyping: String) {
        viewModelScope.launch {
            _isEvaluatingReading.value = true
            _aiEvaluationResult.value = null
            try {
                val score = GeminiService.evaluateReading(targetDhivehi, kidTyping)
                if (score != null) {
                    _aiEvaluationResult.value = score
                    // Give rewards based on accuracyScore
                    if (score.accuracyScore > 50) {
                        val profile = profileDao.getProfileDirect() ?: UserProfile()
                        profileDao.saveProfile(
                            profile.copy(
                                xp = profile.xp + 10,
                                coins = profile.coins + 2
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("DhivehiViewModel", "Evaluation failure", e)
            } finally {
                _isEvaluatingReading.value = false
            }
        }
    }

    override fun onCleared() {
        if (tts != null) {
            tts?.stop()
            tts?.shutdown()
        }
        super.onCleared()
    }
}
