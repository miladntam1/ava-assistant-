package ir.ava.assistant

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ir.ava.assistant.assistant.core.AssistantOrchestrator
import javax.inject.Inject

@HiltViewModel
class AssistantViewModel @Inject constructor(
    private val orchestrator: AssistantOrchestrator
) : ViewModel() {
    val state = orchestrator.state
    fun onMicClick() = orchestrator.startListening()
    override fun onCleared() {
        super.onCleared()
        orchestrator.stop()
    }
}
