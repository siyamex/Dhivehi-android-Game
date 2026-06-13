package com.example.data

import android.content.Context
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles WHERE id = 1 LIMIT 1")
    fun getProfileFlow(): Flow<UserProfile?>

    @Query("SELECT * FROM user_profiles WHERE id = 1 LIMIT 1")
    suspend fun getProfileDirect(): UserProfile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: UserProfile)

    @Update
    suspend fun updateProfile(profile: UserProfile)
}

@Dao
interface LessonProgressDao {
    @Query("SELECT * FROM lesson_progress")
    fun getAllProgress(): Flow<List<LessonProgress>>

    @Query("SELECT * FROM lesson_progress WHERE category = :category")
    fun getProgressByCategory(category: String): Flow<List<LessonProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProgress(progress: LessonProgress)

    @Query("DELETE FROM lesson_progress")
    suspend fun clearProgress()
}

@Dao
interface AiStoryDao {
    @Query("SELECT * FROM ai_stories ORDER BY timestamp DESC")
    fun getAllStoriesFlow(): Flow<List<AiStory>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: AiStory)

    @Query("DELETE FROM ai_stories WHERE id = :storyId")
    suspend fun deleteStory(storyId: Int)
}

@Database(entities = [UserProfile::class, LessonProgress::class, AiStory::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun lessonProgressDao(): LessonProgressDao
    abstract fun aiStoryDao(): AiStoryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dhivehi_kids_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
