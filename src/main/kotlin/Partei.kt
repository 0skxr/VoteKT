import androidx.compose.foundation.layout.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun Partei(party: ElectionListItem, selectedParty: ElectionListItem?, onPartySelected: (ElectionListItem?) -> Unit) {
    val isSelected = selectedParty == party
    Row(
        modifier = Modifier.background(color = Color.White).width(910.dp).border(2.dp, SolidColor(Color.Black), shape = RoundedCornerShape(0.dp))
    ) {
        ListPosition(party.listPosition)
        Column(
            modifier = Modifier.background(color = Color.Cyan).width(800.dp)
        ) {
            Row(modifier = Modifier.background(color = Color.White).fillMaxWidth().height(40.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(party.shortName, style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(party.fullName, fontWeight = FontWeight.Bold)
                }
                Text("- " + party.electionList + " -", modifier = Modifier.align(Alignment.CenterVertically), fontWeight = FontWeight.Bold)
            }
            Row(modifier = Modifier.background(color = Color.White).fillMaxWidth().height(100.dp), verticalAlignment = Alignment.Bottom) {
                Column(
                    modifier = Modifier.background(color = Color.White).width(400.dp).padding(1.dp)
                ) {

                    party.candidates.take(5).forEach { candidate ->
                        Text(
                            if (candidate.occupation != null)
                                candidate.listPosition.toString() + ". " + candidate.name + ", " + candidate.occupation
                            else
                                candidate.listPosition.toString() + ". " +candidate.name
                        )
                    }
                }
                Column(
                    modifier = Modifier.background(color = Color.White).width(400.dp).padding(1.dp)
                ) {
                    party.candidates.drop(5).take(5).forEach { candidate ->
                        Text(
                            if (candidate.occupation != null)
                                candidate.listPosition.toString() + ". " + candidate.name + ", " + candidate.occupation
                            else
                                candidate.listPosition.toString() + ". " +candidate.name
                        )
                    }
                }
            }
        }
        PartySelection(isSelected, party) { selected ->
            onPartySelected(selected)
        }
    }
}