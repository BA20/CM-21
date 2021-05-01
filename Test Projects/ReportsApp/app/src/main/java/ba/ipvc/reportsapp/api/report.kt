package ba.ipvc.reportsapp.api

data class Report (
    val id: Int,
    val titulo: String,
    val descricao: String,
    val lat: Double,
    val lng: Double,
    val user_id: Int,
    val tipo_id: Int,
    val email: String,
    val descr: String

)
