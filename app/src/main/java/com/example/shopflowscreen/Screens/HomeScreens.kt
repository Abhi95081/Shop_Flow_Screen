package com.example.shopflowscreen.Screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.draw.scale
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    // State for selected category, cart, and favorites
    var selectedCategory by remember { mutableStateOf("All") }
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
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 28.sp,
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
                            colors = listOf(Color(0xFF8E24AA), Color(0xFF0288D1))
                        )
                    )
                    .shadow(4.dp),
                actions = {
                    // Heart Icon (Favorites)
                    var heartScale by remember { mutableStateOf(1f) }
                    val animatedHeartScale by animateFloatAsState(
                        targetValue = heartScale,
                        label = "Heart Scale Animation"
                    )
                    BadgedBox(
                        badge = {
                            if (favoriteItems.isNotEmpty()) {
                                Badge { Text("${favoriteItems.size}") }
                            }
                        }
                    ) {
                        IconButton(
                            onClick = {
                                heartScale = 0.8f
                                coroutineScope.launch {
                                    val message = if (favoriteItems.isEmpty()) "Favorites is empty" else "Favorites: ${favoriteItems.joinToString { it.title }}"
                                    snackbarHostState.showSnackbar(message)
                                }
                                heartScale = 1f
                            },
                            modifier = Modifier.scale(animatedHeartScale)
                        ) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = "Favorites",
                                tint = Color.White
                            )
                        }
                    }
                    // Search Icon
                    var searchScale by remember { mutableStateOf(1f) }
                    val animatedSearchScale by animateFloatAsState(
                        targetValue = searchScale,
                        label = "Search Scale Animation"
                    )
                    IconButton(
                        onClick = {
                            searchScale = 0.8f
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Search clicked!")
                            }
                            searchScale = 1f
                        },
                        modifier = Modifier.scale(animatedSearchScale)
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = Color.White
                        )
                    }
                    // Cart Icon
                    var cartScale by remember { mutableStateOf(1f) }
                    val animatedCartScale by animateFloatAsState(
                        targetValue = cartScale,
                        label = "Cart Scale Animation"
                    )
                    BadgedBox(
                        badge = {
                            if (cartItems.isNotEmpty()) {
                                Badge { Text("${cartItems.size}") }
                            }
                        }
                    ) {
                        IconButton(
                            onClick = {
                                cartScale = 0.8f
                                coroutineScope.launch {
                                    val message = if (cartItems.isEmpty()) "Cart is empty" else "Cart: ${cartItems.joinToString { it.title }}"
                                    snackbarHostState.showSnackbar(message)
                                }
                                cartScale = 1f
                            },
                            modifier = Modifier.scale(animatedCartScale)
                        ) {
                            Icon(
                                Icons.Default.ShoppingCart,
                                contentDescription = "Cart",
                                tint = Color.White
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
                    .background(Color(0xFFE8ECEF))
            ) {
                // Hero Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF0288D1), Color(0xFF7C4DFF))
                            )
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Discover Your Beauty Essentials",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                CategorySection(
                    selectedCategory = selectedCategory,
                    onCategorySelected = { selectedCategory = it }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Trending Now",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF121212),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(productList.filter {
                        selectedCategory == "All" || it.title.contains(selectedCategory, ignoreCase = true)
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

@Composable
fun CategorySection(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val categories = listOf("All", "Skin", "Hair", "Body", "Makeup")

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                category = category,
                isSelected = category == selectedCategory,
                onClick = { onCategorySelected(category) }
            )
        }
    }
}

@Composable
fun FilterChip(category: String, isSelected: Boolean, onClick: () -> Unit) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF0288D1) else Color(0xFF0288D1).copy(alpha = 0.15f),
        label = "Chip Background Animation"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color(0xFF0288D1),
        label = "Chip Text Animation"
    )
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        label = "Chip Scale Animation"
    )

    Surface(
        shape = RoundedCornerShape(28.dp),
        modifier = Modifier
            .scale(scale)
            .clickable(onClick = onClick)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(28.dp)
            )
            .shadow(6.dp, RoundedCornerShape(28.dp))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_category), // Assumes a placeholder drawable
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = category,
                color = textColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    favoriteItems: MutableList<Product>,
    onAddToCart: () -> Unit,
    onCardClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.98f else 1f,
        label = "Card Scale Animation"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .scale(scale)
            .shadow(10.dp, RoundedCornerShape(16.dp))
            .clickable { isPressed = true; onCardClick(); isPressed = false },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFFFFFFF), Color(0xFFF0F4F8))
                    )
                )
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFE6ECEF))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.background),
                    contentDescription = null,
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = product.title,
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxHeight()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = product.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF121212)
                        )
                        Text(
                            text = "₹${product.price}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF4A4A4A)
                        )
                        Row(
                            modifier = Modifier.padding(top = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_star),
                                contentDescription = null,
                                tint = Color(0xFFFFCA28),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "4.5",
                                fontSize = 14.sp,
                                color = Color(0xFF4A4A4A),
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                    IconButton(
                        onClick = { onFavoriteClick() },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            Icons.Default.Favorite,
                            contentDescription = "Favorite",
                            tint = if (product in favoriteItems) Color.Red else Color.Gray
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { onAddToCart() },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0288D1)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .shadow(6.dp, RoundedCornerShape(12.dp))
                ) {
                    Text(
                        text = "Add to Cart",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

data class Product(
    val id: Int,
    val title: String,
    val price: Int,
    val imageRes: Int
)

val productList = listOf(
    Product(1, "Sup Skin", 2999, R.drawable.productimage),
    Product(2, "Sup Lotion", 4999, R.drawable.categorysample),
    Product(3, "Sup Cream", 3499, R.drawable.productimage),
    Product(4, "Sup Gel", 3999, R.drawable.categorysample),
    Product(5, "Sup Serum", 5499, R.drawable.productimage),
    Product(6, "Sup Balm", 2799, R.drawable.categorysample),
    Product(7, "Sup Mask", 4599, R.drawable.productimage),
    Product(8, "Sup Oil", 5199, R.drawable.categorysample),
    Product(9, "Sup Scrub", 3299, R.drawable.productimage),
    Product(10, "Sup Mist", 3899, R.drawable.categorysample),
    Product(11, "Sup Toner", 4699, R.drawable.productimage),
    Product(12, "Sup Cleanser", 4199, R.drawable.categorysample)
)