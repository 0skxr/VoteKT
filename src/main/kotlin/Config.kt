import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val password: String,
    val votesPerPerson: Int
)