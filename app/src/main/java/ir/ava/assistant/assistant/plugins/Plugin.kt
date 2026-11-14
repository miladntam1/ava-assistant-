package ir.ava.assistant.assistant.plugins

import ir.ava.assistant.assistant.nlu.ParsedIntent

interface Plugin {
    val id: String
    val name: String
    suspend fun canHandle(intent: ParsedIntent): Boolean
    suspend fun handle(intent: ParsedIntent): PluginResult
}
