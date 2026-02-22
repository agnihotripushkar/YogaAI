package me.pushkaragnihotri.yogaai.features.common.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import timber.log.Timber
import java.util.Locale

class TextToSpeechManager(context: Context) {

    private var tts: TextToSpeech? = null
    private var isInitialized = false

    init {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = tts?.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Timber.e("TTS: Language not supported")
                } else {
                    isInitialized = true
                }
            } else {
                Timber.e("TTS: Initialization failed")
            }
        }
    }

    fun speak(text: String) {
        if (isInitialized && text.isNotEmpty()) {
            // QUEUE_FLUSH drops all pending entries in the playback queue (good for real-time feedback)
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
        }
    }

    fun stop() {
        tts?.stop()
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}
