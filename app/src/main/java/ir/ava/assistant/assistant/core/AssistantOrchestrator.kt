package ir.ava.assistant.assistant.core

import ir.ava.assistant.assistant.nlu.IntentParser
import ir.ava.assistant.assistant.nlu.ParsedIntent
import ir.ava.assistant.assistant.plugins.PluginRegistry
import ir.ava.assistant.assistant.plugins.PluginResult
import ir.ava.assistant.assistant.stt.SttEngine
import ir.ava.assistant.assistant.tts.TtsEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssistantOrchestrator @Inject constructor(
    private val stt: SttEngine,
    private val tts: TtsEngine,
    private val parser: IntentParser,
    private val registry: PluginRegistry,
    private val safety: SafetyGuard
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _state = MutableStateFlow(AssistantState.Idle)
    val state: StateFlow<AssistantState> = _state

    fun startListening() {
        if (_state.value == AssistantState.Listening) return
        scope.launch {
            _state.value = AssistantState.Listening
            val text = stt.listenOnce()
            if (text.isBlank()) {
                _state.value = AssistantState.Idle
                return@launch
            }
            _state.value = AssistantState.Thinking
            val parsed = parser.parse(text)
            when (val result = registry.route(parsed)) {
                is PluginResult.Success -> {
                    _state.value = AssistantState.Speaking
                    safety.speakSafe(result.spoken)
                }
                is PluginResult.Ask -> {
                    _state.value = AssistantState.Speaking
                    safety.speakSafe(result.question)
                    _state.value = AssistantState.Listening
                    val answer = stt.listenOnce()
                    val enriched = parsed.copy(entities = parsed.entities + (result.slot to answer))
                    val r2 = registry.route(enriched)
                    if (r2 is PluginResult.Success) {
                        _state.value = AssistantState.Speaking
                        safety.speakSafe(r2.spoken)
                    }
                }
                is PluginResult.Confirm -> {
                    _state.value = AssistantState.Speaking
                    safety.speakSafe("برای انجام بگو بله")
                    _state.value = AssistantState.Listening
                    val conf = stt.listenOnce()
                    if (conf.contains("بله") || conf.contains("آره")) {
                        val r3 = registry.route(parsed.copy(entities = parsed.entities + ("confirmed" to "true")))
                        if (r3 is PluginResult.Success) {
                            _state.value = AssistantState.Speaking
                            safety.speakSafe(r3.spoken)
                        }
                    }
                }
                is PluginResult.Error -> {
                    _state.value = AssistantState.Speaking
                    safety.speakSafe(result.message)
                }
            }
            _state.value = AssistantState.Idle
        }
    }

    fun stop() {
        stt.stop()
        tts.stop()
    }
}
