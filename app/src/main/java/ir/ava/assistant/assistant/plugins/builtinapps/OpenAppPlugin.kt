package ir.ava.assistant.assistant.plugins.builtinapps

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.ava.assistant.assistant.nlu.ParsedIntent
import ir.ava.assistant.assistant.plugins.Plugin
import ir.ava.assistant.assistant.plugins.PluginResult
import javax.inject.Inject

class OpenAppPlugin @Inject constructor(
    @ApplicationContext private val context: Context
) : Plugin {
    override val id = "open_app"
    override val name = "باز کردن برنامه"
    private val map = mapOf(
        "واتساپ" to "com.whatsapp",
        "تلگرام" to "org.telegram.messenger",
        "اینستاگرام" to "com.instagram.android",
        "یوتیوب" to "com.google.android.youtube",
        "کروم" to "com.android.chrome",
        "تنظیمات" to "com.android.settings",
        "ماشین حساب" to "com.google.android.calculator",
        "مپ" to "com.google.android.apps.maps",
        "دوربین" to "com.android.camera"
    )
    override suspend fun canHandle(intent: ParsedIntent) = intent.intent == "open_app"
    override suspend fun handle(intent: ParsedIntent): PluginResult {
        val raw = intent.entities["app"].orEmpty()
        val pkg = map.entries.find { raw.contains(it.key) }?.value
            ?: return PluginResult.Error("برنامه پیدا نشد")
        val i = context.packageManager.getLaunchIntentForPackage(pkg)
        return if (i != null) {
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(i)
            PluginResult.Success("در حال باز کردن")
        } else PluginResult.Error("نصب نیست")
    }
}
