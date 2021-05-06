package ba.ipvc.reportsapp.api

data class Report(
    val id: Int,
    val titulo: String,
    val descricao: String,
    val lat: Double,
    val lng: Double,
    val user_id: Int,
    val tipo: String,
    val status: Boolean,
    val MSG: String,
    val imagem: String


)
