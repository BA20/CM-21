package ba.ipvc.reportsapp.api


import retrofit2.Call
import retrofit2.http.*


interface EndPoints {

    @GET("reportsAll")
    fun getReports() : Call<List<Report>>

    @FormUrlEncoded
    @POST("user")
    fun login(@Field("username") first: String?, @Field("password") second: String?): Call<Login>


}