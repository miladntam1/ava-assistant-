package ir.ava.assistant.assistant.plugins.builtinsearch

import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.ava.assistant.assistant.nlu.ParsedIntent
import ir.ava.assistant.assistant.plugins.Plugin
import ir.ava.assistant.assistant.plugins.PluginResult
import javax.inject.Inject

class SearchPlugin @Inject constructor(
    @ApplicationContext private val context: Context
) : Plugin {
    override val id = "search"
    override val name = "جستجو"
    override suspend fun canHandle(intent: ParsedIntent) = intent.intent == "search"
    override suspend fun handle(intent: ParsedIntent): PluginResult {
        val q = intent.entities["query"].orEmpty()
        if (q.isBlank()) return PluginResult.Ask("چه چیزی را جستجو کنم؟", "query")
        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com/search?q=${Uri.encode(q)}")).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        return PluginResult.Success("در حال جستجوی $q")
    }
}
