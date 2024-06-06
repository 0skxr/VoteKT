class electionList : ArrayList<electionListItem>(){
    data class electionListItem(
        val candidates: List<Candidate>,
        val election list: String, // Gemeinsame Liste für alle Länder
        val full name: String, // Die PARTEI
        val listPosition: String, // 7
        val short name: String // Die PARTEI
    ) {
        data class Candidate(
            val list position: Int, // 1
            val name: String, // Martin Sonneborn
            val occupation: String? // MdEP
        )
    }
}