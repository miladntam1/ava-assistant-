package ir.ava.assistant.assistant.plugins

sealed interface PluginResult {
    data class Success(val spoken: String) : PluginResult
    data class Ask(val question: String, val slot: String) : PluginResult
    data class Confirm(val message: String) : PluginResult
    data class Error(val message: String) : PluginResult
}
