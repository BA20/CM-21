package ba.ipvc.reportsapp.api
 data class User(
         val id:Int,
         val email:String,
         val password:String

 )

data class report(
        val id:Int,
        val titulo:String,
        val descricao:String,
        val lat:String,
        val lng:String,
        val user_id:Int,
        val tipo_id:Int
)

data class tipo(
        val id:Int,
        val descr:String
)