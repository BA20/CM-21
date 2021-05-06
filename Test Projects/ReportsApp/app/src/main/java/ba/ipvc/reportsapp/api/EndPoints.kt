package ba.ipvc.reportsapp.api


import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface EndPoints {

    @GET("reportsAll")
    fun getReports(): Call<List<Report>>

    @FormUrlEncoded
    @POST("user")
    fun login(@Field("username") first: String?, @Field("password") second: String?): Call<Login>

    @FormUrlEncoded
    @POST("userRegister")
    fun register(@Field("user") first: String?, @Field("pass") second: String?): Call<SignUp>

    @FormUrlEncoded
    @POST("deleteReport/{id}")
    fun deleteReport(@Path("id") id: String?): Call<outputReport>
    @FormUrlEncoded
    @POST("updateR")
    fun updateReport(@Field("id") first: String?, @Field("titulo") second: String?, @Field("descricao")last: String?): Call<outputReport>


    @Multipart
    @POST("createR")
    fun createR(
        @Part("titulo") titulo: RequestBody,
        @Part("descricao") descricao: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lng") lng: RequestBody,
        @Part imagem: MultipartBody.Part,
        @Part("user_id") user_id: RequestBody,
        @Part("tipo") tipo: RequestBody

    ): Call<outputReport>
}