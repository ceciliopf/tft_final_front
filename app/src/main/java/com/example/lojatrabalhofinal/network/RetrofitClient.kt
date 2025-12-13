package com.example.lojatrabalhofinal.network

import com.example.lojatrabalhofinal.model.Product
import com.example.lojatrabalhofinal.model.PurchaseRequest
import com.example.lojatrabalhofinal.model.PurchaseResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {
    @GET("product")
    suspend fun getProducts(@Header("Authorization") token: String): List<Product>

    @POST("product/purchase")
    suspend fun buyProducts(
        @Header("Authorization") token: String,
        @Body request: PurchaseRequest
    ): PurchaseResponse
}


object RetrofitInstance {
    // CERTIFIQUE-SE QUE TERMINA COM /
    private const val BASE_URL = "https://ceciliopfelipe.pythonanywhere.com/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    // A barra é crucial aqui também
    fun getThumbUrl(uuid: String): String {
        return "${BASE_URL}product/thumb/$uuid"
    }
}