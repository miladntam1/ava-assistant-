package ir.ava.assistant

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import ir.ava.assistant.ui.AssistantScreen
import ir.ava.assistant.ui.theme.AvaTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val permissions = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.SEND_SMS,
        Manifest.permission.READ_CONTACTS
    )
    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (permissions.any {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }) {
            launcher.launch(permissions)
        }
        setContent {
            AvaTheme {
                val vm: AssistantViewModel = hiltViewModel()
                val state by vm.state.collectAsState()
                AssistantScreen(state = state, onMicClick = { vm.onMicClick() })
            }
        }
    }
}
