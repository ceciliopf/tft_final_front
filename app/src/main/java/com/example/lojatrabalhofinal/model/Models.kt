package com.example.lojatrabalhofinal.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable // Certifique-se de que essa importação exista ou use o caminho completo abaixo

data class Product(
    val id: Int,
    val name: String,
    val price: Int,
    val thumb: String,
    val desc: String,
    @SerializedName("game_name") val gameName: String
) : java.io.Serializable


data class CartItem(
    val product: Product,
    var quantity: Int
)


data class PurchaseRequest(
    val products: List<PurchaseProductInput>
)

data class PurchaseProductInput(
    val id: Int,
    val quantity: Int
)


data class PurchaseResponse(
    val details: String,
    @SerializedName("pix_code") val pixCode: String,
    @SerializedName("pix_qr") val pixQrBase64: String,
    val total: Int
)