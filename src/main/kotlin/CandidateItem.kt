import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CandidateItem(
    candidate: ErstStimmeItem,
    isSelected: Boolean,
    onSelect: (ErstStimmeItem) -> Unit
) {
    Card(
        border = if (isSelected) BorderStroke(2.dp, Color.Blue) else null,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onSelect(candidate) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nummer: ${candidate.nummer}", fontSize = 20.sp)
            Text(text = "Name: ${candidate.name}", fontSize = 20.sp)
            Text(text = "Partei: ${candidate.partei}", fontSize = 20.sp)
            Text(text = "Beruf: ${candidate.beruf}", fontSize = 16.sp)
            Text(text = "Stadt: ${candidate.stadt}", fontSize = 16.sp)
        }
    }
}