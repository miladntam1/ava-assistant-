package ir.ava.assistant.assistant.plugins.builtintimer

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.ava.assistant.assistant.nlu.ParsedIntent
import ir.ava.assistant.assistant.plugins.Plugin
import ir.ava.assistant.assistant.plugins.PluginResult
import javax.inject.Inject

class TimerPlugin @Inject constructor(
    @ApplicationContext private val context: Context
) : Plugin {
    override val id = "timer"
    override val name = "تایمر"
    override suspend fun canHandle(intent: ParsedIntent) = intent.intent == "timer"
    override suspend fun handle(intent: ParsedIntent): PluginResult {
        val min = intent.entities["minutes"]?.toIntOrNull()
            ?: return PluginResult.Error("زمان مشخص نیست")
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pi = PendingIntent.getBroadcast(
            context, 0, Intent(context, TimerReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        am.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + min * 60_000L, pi
        )
        return PluginResult.Success("تایمر $min دقیقه تنظیم شد")
    }
}
