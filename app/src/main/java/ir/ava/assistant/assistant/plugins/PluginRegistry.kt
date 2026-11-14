package ir.ava.assistant.assistant.plugins

import ir.ava.assistant.assistant.nlu.ParsedIntent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PluginRegistry @Inject constructor(
    private val plugins: Set<@JvmSuppressWildcards Plugin>
) {
    suspend fun route(intent: ParsedIntent): PluginResult {
        val p = plugins.firstOrNull { it.canHandle(intent) }
            ?: return PluginResult.Error("این دستور را نمی‌فهمم")
        return p.handle(intent)
    }
}
