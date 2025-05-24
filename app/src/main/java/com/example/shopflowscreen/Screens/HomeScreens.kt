package com.example.shopflowscreen.Screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopflowscreen.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import com.example.shopflowscreen.Products.Product
import com.example.shopflowscreen.Products.ProductCard
import com.example.shopflowscreen.Products.productList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val selectedCategory = remember { mutableStateOf("All") }
    val cartItems = remember { mutableStateListOf<Product>() }
    val favoriteItems = remember { mutableStateListOf<Product>() }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ShopFlow",
                        fontWeight = FontWeight.Black,
                        fontSize = 30.sp,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFF00BFA5), Color(0xFF0288D1))
                        )
                    )
                    .shadow(6.dp),
                actions = {
                    BadgedBox(
                        badge = {
                            if (favoriteItems.isNotEmpty()) {
                                Badge(
                                    modifier = Modifier.offset(x = (-8).dp, y = 8.dp)
                                ) { Text("${favoriteItems.size}") }
                            }
                        }
                    ) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        if (favoriteItems.isEmpty()) "No favorites" else "Favorites: ${favoriteItems.size} items"
                                    )
                                }
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        ) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = "Favorites",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Search opened")
                            }
                        },
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color.White.copy(alpha = 0.2f), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    BadgedBox(
                        badge = {
                            if (cartItems.isNotEmpty()) {
                                Badge(
                                    modifier = Modifier.offset(x = (-8).dp, y = 8.dp)
                                ) { Text("${cartItems.size}") }
                            }
                        }
                    ) {
                        IconButton(
                            onClick = {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar(
                                        if (cartItems.isEmpty()) "Cart is empty" else "Cart: ${cartItems.size} items"
                                    )
                                }
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .background(Color.White.copy(alpha = 0.2f), CircleShape)
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = "Cart",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF5F7FA))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF00BFA5), Color(0xFF0288D1))
                            )
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Explore Beauty Essentials",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                CategorySection(
                    selectedCategory = selectedCategory.value,
                    onCategorySelected = { selectedCategory.value = it }
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Trending Now",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF121212),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(productList.filter {
                        selectedCategory.value == "All" || it.title.contains(selectedCategory.value, ignoreCase = true)
                    }) { product ->
                        ProductCard(
                            product = product,
                            favoriteItems = favoriteItems,
                            onAddToCart = {
                                if (product !in cartItems) {
                                    cartItems.add(product)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("${product.title} added to cart")
                                    }
                                }
                            },
                            onCardClick = {
                                coroutineScope.launch {
                                    snackbarHostState.showSnackbar("Viewing ${product.title}")
                                }
                            },
                            onFavoriteClick = {
                                if (product in favoriteItems) {
                                    favoriteItems.remove(product)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("${product.title} removed from favorites")
                                    }
                                } else {
                                    favoriteItems.add(product)
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("${product.title} added to favorites")
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    )
}

