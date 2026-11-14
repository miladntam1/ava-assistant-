package ir.ava.assistant.assistant.nlu

import javax.inject.Inject

class PersianIntentParser @Inject constructor() : IntentParser {
    override fun parse(text: String): ParsedIntent {
        val t = text.trim()

        Regex("""با?\s*(.+?)\s*تماس بگیر|تماس بگیر با?\s*(.+)""").find(t)?.let {
            val c = (it.groupValues[1].ifBlank { it.groupValues[2] }).trim()
            return ParsedIntent("call", mapOf("contact" to c), t)
        }
        Regex("""به\s*(.+?)\s*پیام بده(?: که)?\s*(.+)?""").find(t)?.let {
            val c = it.groupValues[1].trim()
            val m = it.groupValues.getOrNull(2)?.trim().orEmpty().ifBlank { "سلام" }
            return ParsedIntent("sms", mapOf("contact" to c, "message" to m), t)
        }
        Regex("""باز کن\s*(.+)""").find(t)?.let {
            return ParsedIntent("open_app", mapOf("app" to it.groupValues[1].trim()), t)
        }
        Regex("""بعد از?\s*(\d+)\s*دقیقه?|تایمر?\s*(\d+)\s*دقیقه?""").find(t)?.let {
            val min = (it.groupValues[1].ifBlank { it.groupValues[2] }).toIntOrNull()
            if (min != null) return ParsedIntent("timer", mapOf("minutes" to min.toString()), t)
        }
        Regex("""جستجو کن(?: برای)?\s*(.+)""").find(t)?.let {
            return ParsedIntent("search", mapOf("query" to it.groupValues[1].trim()), t)
        }
        if (t.contains("ساعت") || t.contains("تاریخ")) {
            return ParsedIntent("datetime", emptyMap(), t)
        }
        return ParsedIntent("chat", emptyMap(), t)
    }
}
