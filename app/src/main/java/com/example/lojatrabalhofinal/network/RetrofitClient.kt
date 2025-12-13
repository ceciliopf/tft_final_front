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

    @POST("buy")
    suspend fun buyProducts(
        @Header("Authorization") token: String,
        @Body request: PurchaseRequest
    ): PurchaseResponse
}


object RetrofitInstance {
    private const val BASE_URL = "http://192.168.1.109:5000/"

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    fun getThumbUrl(uuid: String): String {
        return "${BASE_URL}product/thumb/$uuid"
    }
}