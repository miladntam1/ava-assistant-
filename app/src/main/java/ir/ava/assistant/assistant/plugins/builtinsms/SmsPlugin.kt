package ir.ava.assistant.assistant.plugins.builtinsms

import android.telephony.SmsManager
import dagger.hilt.android.qualifiers.ApplicationContext
import android.content.Context
import ir.ava.assistant.assistant.core.ContactResolver
import ir.ava.assistant.assistant.nlu.ParsedIntent
import ir.ava.assistant.assistant.plugins.Plugin
import ir.ava.assistant.assistant.plugins.PluginResult
import javax.inject.Inject

class SmsPlugin @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contacts: ContactResolver
) : Plugin {
    override val id = "sms"
    override val name = "پیامک"
    override suspend fun canHandle(intent: ParsedIntent) = intent.intent == "sms"
    override suspend fun handle(intent: ParsedIntent): PluginResult {
        val c = intent.entities["contact"].orEmpty()
        val m = intent.entities["message"].orEmpty()
        if (c.isBlank()) return PluginResult.Ask("به چه کسی پیام بدم؟", "contact")
        if (m.isBlank()) return PluginResult.Ask("متن پیام چی باشه؟", "message")
        val num = contacts.findNumber(c) ?: return PluginResult.Error("مخاطب پیدا نشد")
        if (intent.entities["confirmed"] != "true") return PluginResult.Confirm("پیام به $c بفرستم؟")
        return try {
            SmsManager.getDefault().sendTextMessage(num, null, m, null, null)
            PluginResult.Success("پیام به $c ارسال شد")
        } catch (e: Exception) {
            PluginResult.Error("خطا در ارسال")
        }
    }
}
