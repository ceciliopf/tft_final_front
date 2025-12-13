package com.example.lojatrabalhofinal.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lojatrabalhofinal.model.*
import com.example.lojatrabalhofinal.network.RetrofitInstance
import kotlinx.coroutines.launch

class LojaViewModel : ViewModel() {

    private val token = "Bearer SEU_TOKEN_AQUI"

    var products = mutableStateListOf<Product>()
    var cartItems = mutableStateListOf<CartItem>()
    var purchaseResult = mutableStateOf<PurchaseResponse?>(null)
    var errorMessage = mutableStateOf("")


    init {
        fetchProducts()
    }

    fun fetchProducts() {
        viewModelScope.launch {
            try {
                val lista = RetrofitInstance.api.getProducts(token)
                products.clear()
                products.addAll(lista)
            } catch (e: Exception) {
                errorMessage.value = "Erro ao carregar: ${e.message}"
            }
        }
    }

    fun addToCart(product: Product) {
        val existing = cartItems.find { it.product.id == product.id }
        if (existing != null) {

            val index = cartItems.indexOf(existing)
            cartItems[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            cartItems.add(CartItem(product, 1))
        }
    }

    fun removeFromCart(item: CartItem) {
        cartItems.remove(item)
    }

    fun finalizePurchase() {
        viewModelScope.launch {
            try {
                val inputs = cartItems.map { PurchaseProductInput(it.product.id, it.quantity) }
                val request = PurchaseRequest(inputs)
                val response = RetrofitInstance.api.buyProducts(token, request)
                purchaseResult.value = response
                cartItems.clear() //
            } catch (e: Exception) {
                errorMessage.value = "Erro na compra: ${e.message}"
            }
        }
    }
}