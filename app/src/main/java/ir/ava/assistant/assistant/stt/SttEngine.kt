package ir.ava.assistant.assistant.stt

interface SttEngine {
    suspend fun listenOnce(): String
    fun stop()
}
