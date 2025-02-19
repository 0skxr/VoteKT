import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

typealias ErstStimme = List<ErstStimmeItem>

@Serializable
data class ErstStimmeContainer(
    val kandidaten: ErstStimme
)

@Serializable
data class ErstStimmeItem(
    @SerialName("nummer")
    val nummer: Int,
    @SerialName("name")
    val name: String,
    @SerialName("partei")
    val partei: String,
    @SerialName("partei_vollst")
    val partei_vollst: String,
    @SerialName("beruf")
    val beruf: String,
    @SerialName("stadt")
    val stadt: String,

    ) : Comparable<ErstStimmeItem> {
    override fun compareTo(other: ErstStimmeItem): Int {
        return this.nummer.compareTo(other.nummer)
    }
}

fun ErstStimme.sortByListPosition(): ErstStimme {
    return this.sorted()
}