
package ir.ava.assistant.assistant.nlu

data class ParsedIntent(
    val intent: String,
    val entities: Map<String, String>,
    val raw: String
)
