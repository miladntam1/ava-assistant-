package ir.ava.assistant.assistant.nlu

interface IntentParser {
    fun parse(text: String): ParsedIntent
}
