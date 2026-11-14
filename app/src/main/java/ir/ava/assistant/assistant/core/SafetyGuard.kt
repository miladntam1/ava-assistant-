package ir.ava.assistant.assistant.core

import ir.ava.assistant.assistant.tts.TtsEngine
import javax.inject.Inject

class SafetyGuard @Inject constructor(
    private val tts: TtsEngine
) {
    suspend fun speakSafe(text: String) {
        val clean = text
            .replace(Regex("""\b09\d{9}\b"""), "شماره تماس")
            .replace(Regex("""\b\d{10}\b"""), "کد")
        tts.speak(clean)
    }
}
