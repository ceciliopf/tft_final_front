package com.example.lojatrabalhofinal


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.lojatrabalhofinal.model.Product
import com.example.lojatrabalhofinal.ui.*
import com.example.lojatrabalhofinal.viewmodel.LojaViewModel
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewModel: LojaViewModel = viewModel()
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "list") {


                composable("list") {
                    ProductListScreen(
                        viewModel = viewModel,
                        onProductClick = { product ->

                            val json = Gson().toJson(product)

                            navController.currentBackStackEntry?.savedStateHandle?.set("product", product)
                            navController.navigate("detail")
                        },
                        onGoToCart = { navController.navigate("cart") }
                    )
                }


                composable("detail") {

                    val product = navController.previousBackStackEntry?.savedStateHandle?.get<Product>("product")

                    if (product != null) {
                        ProductDetailScreen(
                            product = product,
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }


                composable("cart") {
                    CartScreen(
                        viewModel = viewModel,
                        onCheckout = { navController.navigate("payment") },
                        onBack = { navController.popBackStack() }
                    )
                }


                composable("payment") {
                    PaymentScreen(
                        viewModel = viewModel,
                        onHome = {

                            navController.navigate("list") {
                                popUpTo("list") { inclusive = true }
                            }
                        }
                    )
                }
            }
        }
    }
}