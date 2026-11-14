package ir.ava.assistant.assistant.plugins.builtinchats

import ir.ava.assistant.assistant.nlu.ParsedIntent
import ir.ava.assistant.assistant.plugins.Plugin
import ir.ava.assistant.assistant.plugins.PluginResult
import javax.inject.Inject

class ChatPlugin @Inject constructor() : Plugin {
    override val id = "chat"
    override val name = "گفتگو"
    override suspend fun canHandle(intent: ParsedIntent) = intent.intent == "chat"
    private val reps = listOf("در خدمتم", "بله؟", "چطور کمکتون کنم؟", "گوش می‌کنم")
    override suspend fun handle(intent: ParsedIntent) = PluginResult.Success(reps.random())
}
