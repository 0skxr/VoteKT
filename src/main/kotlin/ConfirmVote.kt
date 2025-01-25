import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ConfirmVote(onClick: (String) -> Unit) {
    val customBackgroundColor = Color.Black // Replace with your desired color
    val customContentColor = Color.White // Replace with your desired content color

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomEnd
    ) {
        ExtendedFloatingActionButton(
            onClick = {
                val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                onClick(timestamp)
            },
            icon = { Icon(Icons.Filled.Check, contentDescription = "Wahl Bestätigen.") },
            text = { Text(text = "Wahl Bestätigen") },
            backgroundColor = customBackgroundColor,
            contentColor = customContentColor,
            modifier = Modifier.padding(16.dp)
        )
    }
}
