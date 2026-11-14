package ir.ava.assistant.assistant.tts

interface TtsEngine {
    suspend fun speak(text: String)
    fun stop()
    fun shutdown()
}
