import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter

fun main() = application {
    val electionList = Json.decodeFromString<ElectionList>(readFileDirectlyAsText("election_list.json"))
    val sortedElectionList = electionList.sortByListPosition()
    var selectedParty by remember { mutableStateOf<ElectionListItem?>(null) }
    var verificationCode by remember { mutableStateOf("") }
    var showVerification by remember { mutableStateOf(true) }

    val Config = Json.decodeFromString<Config>(readFileDirectlyAsText("config.json"))
    val password = Config.password

    var windowTitle by remember { mutableStateOf("Europawahl") }

    Window(title = windowTitle,
        resizable = true,
        onCloseRequest = ::exitApplication ,
    ) {

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
                        )
                    }
                    items(sortedElectionList.size) { index ->
                        Partei(
                            sortedElectionList[index],
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
                            if (verificationCode == password) {
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
        if (!showVerification) {
            ConfirmVote { timestamp ->
                val selectedPartyName = selectedParty?.shortName ?: ""
                val votedParty = VotedParty(selectedPartyName, timestamp)
                writeToCsv(votedParty)
                selectedParty = null // Reset the selected party
                showVerification = true
            }
        }
    }
}

fun writeToCsv(votedParty: VotedParty) {
    val fileName = "voted_parties.csv"
    val file = File(fileName)
    val exists = file.exists()
    val header = listOf("Party", "Timestamp")
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
