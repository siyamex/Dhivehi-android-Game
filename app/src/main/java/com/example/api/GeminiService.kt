package com.example.api

import android.util.Log
import com.example.BuildConfig
import com.example.data.AiStory
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.Serializable
import java.util.concurrent.TimeUnit

@JsonClass(generateAdapter = true)
data class ReadingScore(
    val accuracyScore: Int,
    val feedbackDhivehi: String,
    val feedbackEnglish: String,
    val suggestions: List<String>
) : Serializable

object GeminiService {
    private const val TAG = "GeminiService"
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val API_URL = "https://generativelanguage.googleapis.com/v1beta/models/$MODEL_NAME:generateContent"

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    /**
     * Checks if the Gemini API key is default or configured.
     */
    fun isKeyConfigured(): Boolean {
        val key = BuildConfig.GEMINI_API_KEY
        return key.isNotEmpty() && key != "MY_GEMINI_API_KEY" && !key.contains("API_KEY")
    }

    /**
     * Generates a sweet, educational Maldivian kids story offline/online via Gemini
     */
    suspend fun generateStory(theme: String): AiStory? = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (!isKeyConfigured()) {
            Log.e(TAG, "Gemini API Key is not configured.")
            return@withContext null
        }

        val prompt = """
            Write a short, highly engaging, and educational Maldivian children's story based on the theme: "$theme".
            Include popular characters like Velaa the Turtle, Koamas the Dolphin, or Kakuni the Beach Crab in the beautiful Maldives.
            The story should be colorful and safe for young readers.
            
            YOU MUST respond with a single JSON object containing EXACTLY these keys:
            {
              "title": "Story title in English",
              "contentDhivehi": "The story text beautifully written in Dhivehi (using correct Thaana characters)",
              "contentEnglish": "The full English translation of the story",
              "question": "A simple English question about the story for kids",
              "optionA": "Option A text about the answer in Dhivehi",
              "optionB": "Option B text about the answer in Dhivehi",
              "optionC": "Option C text about the answer in Dhivehi",
              "correctAnswer": "A" (Must be EXACTLY 'A', 'B', or 'C')
            }
            
            Return ONLY the valid, minified JSON object. Do not wrap in markdown tags like ```json or prefix/suffix with any greeting text.
        """.trimIndent()

        try {
            val rawResponse = postRequest(apiKey, prompt) ?: return@withContext null
            val cleanedJson = cleanJsonString(rawResponse)
            
            val jsonObject = JSONObject(cleanedJson)
            return@withContext AiStory(
                title = jsonObject.optString("title", "A Tropical Adventure"),
                contentDhivehi = jsonObject.optString("contentDhivehi", "ކޯމަސް އަދި ވެލާ އުފާކުރެއެވެ."),
                contentEnglish = jsonObject.optString("contentEnglish", "The dolphins and turtles celebrate!"),
                question = jsonObject.optString("question", "What did our dynamic marine characters do?"),
                optionA = jsonObject.optString("optionA", "ކުޅުން (They played)"),
                optionB = jsonObject.optString("optionB", "ނިދުން (They slept)"),
                optionC = jsonObject.optString("optionC", "ފަތާލުން (They swam)"),
                correctAnswer = jsonObject.optString("correctAnswer", "C")
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error generating story", e)
            return@withContext null
        }
    }

    /**
     * AI Reading evaluation for spelling, pronunciation coaching, and sentence feedback
     */
    suspend fun evaluateReading(expectedText: String, userSpokenText: String): ReadingScore? = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (!isKeyConfigured()) {
            return@withContext null
        }

        val prompt = """
            A child is learning to speak and read the Dhivehi language.
            Expected Target Reading: "$expectedText"
            Child spoke/typed: "$userSpokenText"
            
            Evaluate the child's reading accuracy against the expected target.
            YOU MUST respond with a single JSON object containing EXACTLY these keys:
            {
              "accuracyScore": 85, (An integer from 0 to 100 representing spelling/sound target correctness)
              "feedbackDhivehi": " provide friendly, encouraging feedback in Dhivehi to make them smile ",
              "feedbackEnglish": " provide friendly, encouraging, actionable evaluation feedback in English ",
              "suggestions": ["suggestion 1", "suggestion 2"] (A string array of up to 3 English tips for improvement)
            }
            
            Return ONLY the valid, minified JSON object. Do not wrap in any markdown symbols or conversational fluff.
        """.trimIndent()

        try {
            val rawResponse = postRequest(apiKey, prompt) ?: return@withContext null
            val cleanedJson = cleanJsonString(rawResponse)
            val json = JSONObject(cleanedJson)
            
            val suggestionsList = mutableListOf<String>()
            val suggAr = json.optJSONArray("suggestions")
            if (suggAr != null) {
                for (i in 0 until suggAr.length()) {
                    suggestionsList.add(suggAr.getString(i))
                }
            }
            
            return@withContext ReadingScore(
                accuracyScore = json.optInt("accuracyScore", 80),
                feedbackDhivehi = json.optString("feedbackDhivehi", "ވަރަށް ރަނގަޅު! އިތުރަށް މަސައްކަތް ކޮށްލަމާ!"),
                feedbackEnglish = json.optString("feedbackEnglish", "Excellent reading! Keep it up!"),
                suggestions = if (suggestionsList.isEmpty()) listOf("Practice spelling this word daily", "Listen to our guided mascot pronunciation") else suggestionsList
            )
        } catch (e: Exception) {
            Log.e(TAG, "Error evaluating reading", e)
            return@withContext null
        }
    }

    private fun postRequest(apiKey: String, promptText: String): String? {
        val mediaType = "application/json; charset=utf-8".toMediaType()
        
        val requestJson = JSONObject()
        val contentsArray = JSONArray()
        val contentObject = JSONObject()
        val partsArray = JSONArray()
        val partText = JSONObject()
        
        partText.put("text", promptText)
        partsArray.put(partText)
        contentObject.put("parts", partsArray)
        contentsArray.put(contentObject)
        requestJson.put("contents", contentsArray)

        val requestBodyString = requestJson.toString()
        val request = Request.Builder()
            .url("$API_URL?key=$apiKey")
            .post(requestBodyString.toRequestBody(mediaType))
            .build()

        okHttpClient.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                Log.e(TAG, "Unsuccessful response: ${response.code} ${response.message}")
                return null
            }
            val bodyString = response.body?.string() ?: return null
            return extractTextFromResponse(bodyString)
        }
    }

    private fun extractTextFromResponse(responseBody: String): String? {
        return try {
            val json = JSONObject(responseBody)
            val candidates = json.optJSONArray("candidates") ?: return null
            val candidate = candidates.optJSONObject(0) ?: return null
            val content = candidate.optJSONObject("content") ?: return null
            val parts = content.optJSONArray("parts") ?: return null
            val part = parts.optJSONObject(0) ?: return null
            part.optString("text")
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing Gemini raw response: $responseBody", e)
            null
        }
    }

    private fun cleanJsonString(input: String): String {
        var str = input.trim()
        if (str.startsWith("```json")) {
            str = str.substring(7)
        } else if (str.startsWith("```")) {
            str = str.substring(3)
        }
        if (str.endsWith("```")) {
            str = str.substring(0, str.length - 3)
        }
        return str.trim()
    }
}
