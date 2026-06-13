package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val id: Int = 1, // Single profile for simplicity or family profiles
    val name: String = "Kuda Kuda",
    val ageGroup: Int = 1, // 1: 3-5 Years, 2: 6-8 Years, 3: 9-12 Years
    val avatarId: String = "dolphin", // dolphin, turtle, parrot, crab
    val level: Int = 1,
    val xp: Int = 0,
    val stars: Int = 0,
    val coins: Int = 0,
    val dailyStreak: Int = 1,
    val lastActiveTimestamp: Long = System.currentTimeMillis(),
    val parentPin: String = "1234", // Default PIN
    val screenTimeLimitMinutes: Int = 30, // Default 30 min limit
    val todayTimeSpentMinutes: Int = 0
) : Serializable

@Entity(tableName = "lesson_progress")
data class LessonProgress(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String, // "thaana", "numbers", "colors", "shapes", "vocabulary", "grammar"
    val lessonId: String, // e.g. letter "ހ", vocabulary "animals_cat"
    val isCompleted: Boolean = true,
    val starsEarned: Int = 0,
    val lastAccessed: Long = System.currentTimeMillis()
) : Serializable

@Entity(tableName = "ai_stories")
data class AiStory(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val contentDhivehi: String,
    val contentEnglish: String,
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val correctAnswer: String, // "A", "B", "C"
    val timestamp: Long = System.currentTimeMillis()
) : Serializable
