package ir.ava.assistant.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.ava.assistant.assistant.core.AssistantState

@Composable
fun AssistantScreen(state: AssistantState, onMicClick: () -> Unit) {
    val color by animateColorAsState(
        when (state) {
            AssistantState.Idle -> Color(0xFF5AC8FA)
            AssistantState.Listening -> Color(0xFF30D158)
            AssistantState.Thinking -> Color(0xFFFFD60A)
            AssistantState.Speaking -> Color(0xFFBF5AF2)
        }, label = "c"
    )
    val scale by animateFloatAsState(
        if (state == AssistantState.Listening) 1.25f else if (state == AssistantState.Speaking) 1.12f else 1f,
        spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = "s"
    )
    val pulse by rememberInfiniteTransition(label = "p").animateFloat(
        1f, 1.18f, infiniteRepeatable(tween(1000, easing = EaseInOutSine), RepeatMode.Reverse), label = "p"
    )
    val ps = if (state == AssistantState.Listening) pulse else 1f

    Box(Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                Modifier.size((240 * scale * ps).dp).background(color.copy(0.14f), CircleShape).blur(48.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier.size((150 * scale).dp).background(
                        Brush.radialGradient(listOf(color.copy(0.85f), color.copy(0.2f))), CircleShape
                    )
                )
            }
            Spacer(Modifier.height(36.dp))
            Text(
                when (state) {
                    AssistantState.Idle -> "سلام، من آوا هستم"
                    AssistantState.Listening -> "دارم گوش می‌کنم..."
                    AssistantState.Thinking -> "یه لحظه..."
                    AssistantState.Speaking -> "پاسخ می‌دهم..."
                }, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(28.dp))
            Button(onClick = onMicClick, shape = CircleShape, colors = ButtonDefaults.buttonColors(
                containerColor = color.copy(0.2f), contentColor = Color.White
            ), modifier = Modifier.size(84.dp)) {
                Icon(
                    if (state == AssistantState.Listening) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = null, modifier = Modifier.size(36.dp)
                )
            }
        }
    }
}
