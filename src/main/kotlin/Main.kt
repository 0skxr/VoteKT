import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Hilfsfunktion für den aktuellen Zeitstempel
fun getCurrentTimestamp(): String {
    val current = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return current.format(formatter)
}

// CSV-Schreibfunktion (Typ 1 = Erststimme, Typ 2 = Zweitstimme)
fun writeToCsv(votedParty: VotedParty, type: Int) {
    val fileName = "voted_parties.csv"
    val file = File(fileName)
    val exists = file.exists()
    val header = listOf("Party", "Timestamp", "Type")
    val row = listOf(votedParty.party, votedParty.timestamp, type)

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

// Enum zur Steuerung der Voting-Phasen
enum class VotingPhase {
    ERST_STIMME,
    ZWEIT_STIMME
}

fun main() = application {
    // Daten laden
    val electionList = Json.decodeFromString<ElectionList>(readFileDirectlyAsText("election_list.json"))
    val sortedElectionList = electionList.sortByListPosition()

    val erstContainer = Json.decodeFromString<ErstStimmeContainer>(readFileDirectlyAsText("erstStimme.json"))
    val kandidatenListe = erstContainer.kandidaten.sortByListPosition()

    val config = Json.decodeFromString<Config>(readFileDirectlyAsText("config.json"))
    val password = config.password
    val votesPerPerson = config.votesPerPerson

    // Zustände
    var verificationCode by remember { mutableStateOf("") }
    var showVerification by remember { mutableStateOf(true) }
    var currentPhase by remember { mutableStateOf(VotingPhase.ERST_STIMME) }
    var selectedCandidate by remember { mutableStateOf<ErstStimmeItem?>(null) }
    var selectedParty by remember { mutableStateOf<ElectionListItem?>(null) }
    var votesCast by remember { mutableStateOf(0) }
    var windowTitle by remember { mutableStateOf("Europawahl") }

    Window(
        title = windowTitle,
        resizable = true,
        onCloseRequest = ::exitApplication
    ) {
        // Haupt-Layout: zentrierter Box-Container
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Verifizierungsschirm (unverändert wie in main.old.kt)
            if (showVerification) {
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
                            if (verificationCode == password) {
                                showVerification = false
                                currentPhase = VotingPhase.ERST_STIMME
                                votesCast = 0
                            }
                            verificationCode = ""
                        },
                        modifier = Modifier.padding(top = 16.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
                    ) {
                        Text("Verify", color = Color.White)
                    }
                }
            } else {
                // Voting-UI (Design à la main.old.kt)
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header mit Titeln und verbleibenden Stimmen
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
                            text = "Sie haben ${votesPerPerson - votesCast} Stimme(n) verbleibend",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                    // Listeninhalt je nach Voting-Phase
                    when (currentPhase) {
                        VotingPhase.ERST_STIMME -> {
                            items(kandidatenListe) { candidate ->
                                CandidateItem(
                                    candidate = candidate,
                                    isSelected = candidate == selectedCandidate,
                                    onSelect = { selectedCandidate = it }
                                )
                            }
                        }
                        VotingPhase.ZWEIT_STIMME -> {
                            items(sortedElectionList) { party ->
                                Partei(
                                    party = party,
                                    selectedParty = selectedParty,
                                    onPartySelected = { selectedParty = it }
                                )
                            }
                        }
                    }
                    // Etwas Platz am unteren Rand, damit die Liste nicht vom Button überdeckt wird
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
        // Bestätigungsschaltfläche (gestylt wie im alten Design)
        if (!showVerification) {
            ConfirmVote(
                onConfirm = {
                    if (currentPhase == VotingPhase.ERST_STIMME && selectedCandidate != null) {
                        val timestamp = getCurrentTimestamp()
                        writeToCsv(VotedParty(selectedCandidate!!.name, timestamp), 1)
                        votesCast++
                        currentPhase = VotingPhase.ZWEIT_STIMME
                        selectedCandidate = null
                    } else if (currentPhase == VotingPhase.ZWEIT_STIMME && selectedParty != null) {
                        val timestamp = getCurrentTimestamp()
                        writeToCsv(VotedParty(selectedParty!!.shortName, timestamp), 2)
                        votesCast++
                        selectedParty = null
                        // Nach beiden Stimmen (oder wenn die abgegebenen Stimmen den erlaubten Wert erreichen) wieder zurück zur Verifizierung
                        if (votesCast >= votesPerPerson) {
                            showVerification = true
                            currentPhase = VotingPhase.ERST_STIMME
                            votesCast = 0
                        } else {
                            currentPhase = VotingPhase.ERST_STIMME
                        }
                    }
                }
            )
        }
    }
}

// ConfirmVote-Composable – platziert den Bestätigen-Button unten, gestylt wie in main.old.kt
@Composable
fun ConfirmVote(onConfirm: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Button(
            onClick = onConfirm,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black)
        ) {
            Text("Bestätigen", color = Color.White)
        }
    }
}
