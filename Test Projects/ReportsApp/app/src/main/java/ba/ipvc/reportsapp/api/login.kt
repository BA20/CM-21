package ba.ipvc.reportsapp.api

import java.sql.SQLData

data class Login (
    val username: String,
    val password: String,
    val status: Boolean,
    val MSG: String,
    val id: Int

)