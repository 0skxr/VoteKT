import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter

data class VotedParty(val party: String, val timestamp: String)

fun readFileDirectlyAsText(fileName: String): String
        = File(fileName).readText(Charsets.UTF_8)

@Composable
fun NumberBox(number: String) {
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


@Composable
fun CustomCheckBox(isChecked: Boolean, party: ElectionListItem, onCheckedChange: () -> Unit) {
    Box(
        modifier = Modifier
            .width(50.dp)
            .height(140.dp)
            .border(2.dp, Color.Black)
            .padding(8.dp)
            .clickable {
                onCheckedChange()
            },
        contentAlignment = Alignment.Center
    ) {
        DrawCircle(isChecked)
    }
}

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
fun partei(party: ElectionListItem, selectedParty: ElectionListItem?, onPartySelected: (ElectionListItem) -> Unit) {
    val isSelected = selectedParty == party
    Row(
        modifier = Modifier.background(color = Color.White).width(910.dp).border(2.dp, SolidColor(Color.Black), shape = RoundedCornerShape(0.dp))
    ) {
        NumberBox(party.listPosition)
        Column(
            modifier = Modifier.background(color = Color.Cyan).width(800.dp)
        ) {
            Row(modifier = Modifier.background(color = Color.White).fillMaxWidth().height(40.dp), Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
                Text(party.shortName, style = MaterialTheme.typography.h5)
                Text(party.fullName)
                Text(party.electionList)
            }
            Row(modifier = Modifier.background(color = Color.White).fillMaxWidth().height(100.dp), verticalAlignment = Alignment.Bottom) {
                Column(
                    modifier = Modifier.background(color = Color.White).width(400.dp).padding(1.dp)
                ) {
                    party.candidates.take(5).forEach { candidate ->
                        Text(candidate.name)
                    }
                }
                Column(
                    modifier = Modifier.background(color = Color.White).width(400.dp).padding(1.dp)
                ) {
                    party.candidates.drop(5).take(5).forEach { candidate ->
                        Text(candidate.name)
                    }
                }
            }
        }
        CustomCheckBox(isSelected, party) { onPartySelected(party) }
    }
}

@Composable
fun Example(onClick: (String) -> Unit) {
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



@OptIn(ExperimentalLayoutApi::class)
fun main() = application {
    val electionList = Json.decodeFromString<ElectionList>(readFileDirectlyAsText("election_list_new.json"))
    val sortedElectionList = electionList.sortByListPosition()
    var selectedParty by remember { mutableStateOf<ElectionListItem?>(null) }
    var verificationCode by remember { mutableStateOf("") }
    var showVerification by remember { mutableStateOf(true) }

    Window(onCloseRequest = ::exitApplication) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            if (!showVerification) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Text(
                            text = "Stimmzettel",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold
                        )
                    Text(
                        text = "für die Wahl der Abgeordneten des Europäischen Parlaments am 9.Juni 2024",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "im Land Nordrhein-Westfalen",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Sie haben 1 Stimme",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )}
                    items(sortedElectionList.size) { index ->
                        partei(
                            sortedElectionList.get(index),
                            selectedParty,
                            onPartySelected = { selectedParty = it }
                        )
                    }
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    TextField(
                        value = verificationCode,
                        onValueChange = { verificationCode = it },
                        label = { Text("Enter verification code") }
                    )
                    Button(
                        onClick = {
                            selectedParty = null
                            if(verificationCode == "11235813") {
                                showVerification = false
                            }
                            verificationCode = ""

                        },
                        modifier = Modifier.padding(top = 16.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                    ) {
                        Text("Verify", color = Color.White)

                    }
                }
            }
        }
        if(!showVerification){
        Example { timestamp ->
            val selectedParty = selectedParty?.shortName ?: ""
            val votedParty = VotedParty(selectedParty, timestamp)
            writeToCsv(votedParty)
            showVerification = true
        }}
    }
}



fun writeToCsv(votedParty: VotedParty) {
    val fileName = "voted_parties.csv"
    val file = File(fileName)
    val exists = file.exists()
    val header = listOf("Party",  "Timestamp")
    val row = listOf(votedParty.party, votedParty.timestamp)

    if (!exists) {
        csvWriter().open(fileName, append = false) {
            writeRow(header)
            writeRow(row)
        }
    } else {
        csvWriter().open(fileName, append = true) {
            writeRow(row)
        }
    }
}