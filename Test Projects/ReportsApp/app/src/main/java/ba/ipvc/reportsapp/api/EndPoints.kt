package ba.ipvc.reportsapp.api


import  retrofit2.Call
import retrofit2.http.*


interface EndPoints {

    @GET("/reportsAll/")
    fun getReports() : Call<List<Report>>



}