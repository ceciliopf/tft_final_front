# LojaTrabalhoFinal - App Android

Aplicação móvel Android para a "Loja de Games", desenvolvida com **Jetpack Compose**. O app permite navegar por uma lista de jogos, gerenciar um carrinho de compras e realizar pagamentos via PIX.

## Tecnologias Utilizadas
* **Kotlin**: Linguagem principal.
* **Jetpack Compose**: UI moderna e declarativa.
* **Retrofit**: Cliente HTTP para consumo da API REST.
* **Coil**: Carregamento de imagens de forma assíncrona.
* **Navigation Compose**: Navegação entre telas (Lista, Detalhe, Carrinho e Pagamento).

## Estrutura do Projeto
* **UI**: Telas construídas com Material Design 3 (Listagem, Detalhes, Carrinho e Pagamento).
* **ViewModel**: `LojaViewModel` gerencia o estado da aplicação e chamadas à API.
* **Network**: `RetrofitClient` configurado para comunicar com o servidor hospedado no PythonAnywhere.

## Funcionalidades
* **Catálogo**: Exibição de produtos com nome, preço e descrição.
* **Carrinho**: Adição/remoção de itens e cálculo de total em tempo real.
* **Pagamento PIX**: Recebe os dados do backend e exibe o QR Code dinâmico para o usuário.

## Requisitos
* **Android SDK**: Compilação para SDK 36, compatível desde o SDK 24.
* **Permissões**: Necessário acesso à Internet para carregar dados e imagens.

## Configuração
O aplicativo consome a API no endereço:
`https://ceciliopfelipe.pythonanywhere.com/`.
O token de autenticação está configurado diretamente no `LojaViewModel`. 
