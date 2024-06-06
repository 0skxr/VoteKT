import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias ElectionList = List<ElectionListItem>

@Serializable
data class ElectionListItem(
    val candidates: List<Candidate>,
    @SerialName("election list")
    val electionList: String,
    @SerialName("full name")
    val fullName: String,
    @SerialName("list_position")
    val listPosition: String,
    @SerialName("short name")
    val shortName: String
) : Comparable<ElectionListItem> {
    override fun compareTo(other: ElectionListItem): Int {
        return this.listPosition.toInt().compareTo(other.listPosition.toInt())
    }

    @Serializable
    data class Candidate(
        @SerialName("list position")
        val listPosition: Int,
        val name: String,
        val occupation: String?
    )
}

fun ElectionList.sortByListPosition(): ElectionList {
    return this.sorted()
}