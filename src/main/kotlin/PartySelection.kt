import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun DrawCircle(isChecked: Boolean) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val radius = size.minDimension / 2
        val center = Offset(size.width / 2, size.height / 2)

        drawCircle(
            color = Color.Black,
            radius = radius,
            center = center,
            alpha = 1f,
            style = Stroke(width = 1f)
        )

        if (isChecked) {
            drawLine(
                color = Color.Black,
                start = Offset(center.x - radius, center.y * 0.75f),
                end = Offset(center.x + radius, center.y * 1.25f),
                strokeWidth = 4f
            )
            drawLine(
                color = Color.Black,
                start = Offset(center.x - radius, center.y * 1.25f),
                end = Offset(center.x + radius, center.y * 0.75f),
                strokeWidth = 4f
            )
        }
    }
}


@Composable
fun PartySelection(isChecked: Boolean, party: ElectionListItem, onCheckedChange: (ElectionListItem?) -> Unit) {
    Box(
        modifier = Modifier
            .width(50.dp)
            .height(140.dp)
            .border(2.dp, Color.Black)
            .padding(8.dp)
            .clickable {
                onCheckedChange(if (isChecked) null else party)
            },
        contentAlignment = Alignment.Center
    ) {
        DrawCircle(isChecked)
    }
}
