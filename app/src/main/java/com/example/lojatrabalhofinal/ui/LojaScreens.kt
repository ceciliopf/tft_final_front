package com.example.lojatrabalhofinal.ui

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.lojatrabalhofinal.model.Product
import com.example.lojatrabalhofinal.network.RetrofitInstance
import com.example.lojatrabalhofinal.viewmodel.LojaViewModel


@Composable
fun ProductListScreen(
    viewModel: LojaViewModel,
    onProductClick: (Product) -> Unit,
    onGoToCart: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onGoToCart) {
                Text("🛒")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Text("Loja de Games", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(16.dp))

            if (viewModel.errorMessage.value.isNotEmpty()) {
                Text("Erro: ${viewModel.errorMessage.value}", color = Color.Red)
            }

            LazyColumn {
                items(viewModel.products) { product ->
                    ProductItem(product, onClick = { onProductClick(product) })
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {

            AsyncImage(
                model = RetrofitInstance.getThumbUrl(product.thumb),
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(product.name, style = MaterialTheme.typography.titleMedium)
                Text("R$ ${String.format("%.2f", product.price / 100.0)}", color = Color.Green)
            }
        }
    }
}


@Composable
fun ProductDetailScreen(
    product: Product,
    viewModel: LojaViewModel,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Button(onClick = onBack) { Text("Voltar") }

        Spacer(modifier = Modifier.height(16.dp))

        AsyncImage(
            model = RetrofitInstance.getThumbUrl(product.thumb),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().height(250.dp),
            contentScale = ContentScale.Fit
        )

        Text(product.name, style = MaterialTheme.typography.headlineLarge)
        Text(product.gameName, style = MaterialTheme.typography.labelLarge)
        Text("R$ ${String.format("%.2f", product.price / 100.0)}", style = MaterialTheme.typography.headlineMedium, color = Color.Green)

        Spacer(modifier = Modifier.height(16.dp))
        Text(product.desc)

        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { viewModel.addToCart(product); onBack() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adicionar ao Carrinho")
        }
    }
}


@Composable
fun CartScreen(
    viewModel: LojaViewModel,
    onCheckout: () -> Unit,
    onBack: () -> Unit
) {
    val total = viewModel.cartItems.sumOf { it.product.price * it.quantity } / 100.0

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Button(onClick = onBack) { Text("Voltar") }
        Text("Seu Carrinho", style = MaterialTheme.typography.headlineMedium)

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(viewModel.cartItems) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("${item.quantity}x ${item.product.name}")
                    Text("R$ ${String.format("%.2f", (item.product.price * item.quantity)/100.0)}")
                    Button(onClick = { viewModel.removeFromCart(item) }) { Text("X") }
                }
            }
        }

         HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
        Text("Total: R$ ${String.format("%.2f", total)}", style = MaterialTheme.typography.headlineSmall)

        Button(
            onClick = {
                viewModel.finalizePurchase()
                onCheckout()
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            enabled = viewModel.cartItems.isNotEmpty()
        ) {
            Text("Finalizar Compra (PIX)")
        }
    }
}


@Composable
fun PaymentScreen(
    viewModel: LojaViewModel,
    onHome: () -> Unit
) {
    val result = viewModel.purchaseResult.value

    Column(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        if (result == null) {
            CircularProgressIndicator()
            Text("Processando...")
        } else {
            Text("Pagamento Gerado!", style = MaterialTheme.typography.headlineMedium, color = Color.Blue)
            Spacer(modifier = Modifier.height(16.dp))

            // Decodifica Base64 para mostrar QR Code
            val imageBytes = Base64.decode(result.pixQrBase64, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "QR Code Pix",
                    modifier = Modifier.size(250.dp)
                )
            }

            Text("Copia e Cola:", style = MaterialTheme.typography.labelLarge)
            Text(result.pixCode, style = MaterialTheme.typography.bodySmall)

            Spacer(modifier = Modifier.height(16.dp))
            Text("Resumo:")
            Text(result.details)

            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = {
                viewModel.purchaseResult.value = null // Reseta
                onHome()
            }) {
                Text("Voltar ao Início")
            }
        }
    }
}