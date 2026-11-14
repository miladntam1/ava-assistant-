package ir.ava.assistant.assistant.plugins.builtincalls

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import ir.ava.assistant.assistant.core.ContactResolver
import ir.ava.assistant.assistant.nlu.ParsedIntent
import ir.ava.assistant.assistant.plugins.Plugin
import ir.ava.assistant.assistant.plugins.PluginResult
import javax.inject.Inject

class CallPlugin @Inject constructor(
    @ApplicationContext private val context: Context,
    private val contacts: ContactResolver
) : Plugin {
    override val id = "call"
    override val name = "تماس"
    override suspend fun canHandle(intent: ParsedIntent) = intent.intent == "call"
    override suspend fun handle(intent: ParsedIntent): PluginResult {
        val name = intent.entities["contact"].orEmpty()
        if (name.isBlank()) return PluginResult.Ask("با چه کسی تماس بگیرم؟", "contact")
        val number = contacts.findNumber(name) ?: return PluginResult.Error("مخاطب پیدا نشد")
        if (intent.entities["confirmed"] != "true") return PluginResult.Confirm("با $name تماس بگیرم؟")
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
            return PluginResult.Error("اجازه تماس ندارم")
        context.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$number")).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        })
        return PluginResult.Success("در حال تماس با $name")
    }
}
