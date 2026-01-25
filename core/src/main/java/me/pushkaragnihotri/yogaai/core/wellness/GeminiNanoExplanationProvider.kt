package me.pushkaragnihotri.yogaai.core.wellness

import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.generationConfig
import me.pushkaragnihotri.yogaai.core.model.RiskLevel
import timber.log.Timber

class GeminiNanoExplanationProvider : WellnessExplanationGenerator {

    // Using "gemini-nano" for on-device inference where supported
    // For standard SDK, it might require an API key unless using AICore specifically
    private val generativeModel = GenerativeModel(
        modelName = "gemini-nano",
        apiKey = "LOCAL_ONLY", // On-device model often doesn't need a real key or uses AICore
        generationConfig = generationConfig {
            temperature = 0.7f
            topK = 32
            topP = 1f
            maxOutputTokens = 100
        }
    )

    override suspend fun generateExplanation(
        riskLevel: RiskLevel,
        contributingSignals: List<String>,
        metricsSummary: String
    ): String {
        val prompt = """
            You are a helpful Yoga and Wellness assistant. 
            Based on the following data, provide a concise (1-2 sentences) and encouraging explanation for the user's wellness risk.
            
            Risk Level: ${riskLevel.name}
            Key Factors: ${contributingSignals.joinToString(", ")}
            Data Summary: $metricsSummary
            
            Explanation:
        """.trimIndent()

        return try {
            val response = generativeModel.generateContent(prompt)
            response.text ?: "Your wellness looks ${riskLevel.name.lowercase()} based on your activity and sleep."
        } catch (e: Exception) {
            Timber.e(e, "Error generating explanation with Gemini Nano")
            "Unable to generate dynamic explanation. Risk remains ${riskLevel.name.lowercase()}."
        }
    }
}
