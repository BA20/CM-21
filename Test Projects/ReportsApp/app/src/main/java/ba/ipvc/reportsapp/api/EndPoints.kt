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

    ): Call<Report>
}