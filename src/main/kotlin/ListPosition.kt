import androidx.compose.foundation.layout.*
import androidx.compose.foundation.border
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ListPosition(number: String) {
    Box(
        modifier = Modifier
            .width(60.dp)
            .height(140.dp)
            .border(2.dp, Color.Black)
            .padding(8.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = number,
            style = MaterialTheme.typography.h4,
            color = Color.Black
        )
    }
}
