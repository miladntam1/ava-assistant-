package ir.ava.assistant.assistant.plugins.builtindatetime

import ir.ava.assistant.assistant.nlu.ParsedIntent
import ir.ava.assistant.assistant.plugins.Plugin
import ir.ava.assistant.assistant.plugins.PluginResult
import java.util.Calendar
import javax.inject.Inject

class DatetimePlugin @Inject constructor() : Plugin {
    override val id = "datetime"
    override val name = "زمان"
    override suspend fun canHandle(intent: ParsedIntent) = intent.intent == "datetime"
    override suspend fun handle(intent: ParsedIntent): PluginResult {
        val n = Calendar.getInstance()
        val t = String.format("%02d:%02d", n.get(Calendar.HOUR_OF_DAY), n.get(Calendar.MINUTE))
        val d = "${n.get(Calendar.YEAR)}/${n.get(Calendar.MONTH) + 1}/${n.get(Calendar.DAY_OF_MONTH)}"
        val txt = when {
            intent.raw.contains("ساعت") -> "الان ساعت $t است"
            intent.raw.contains("تاریخ") -> "امروز $d است"
            else -> "ساعت $t و تاریخ $d"
        }
        return PluginResult.Success(txt)
    }
}
