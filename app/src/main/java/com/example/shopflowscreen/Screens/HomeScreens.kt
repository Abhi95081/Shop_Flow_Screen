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
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.vector.ImageVector
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
import kotlinx.coroutines.delay
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
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color(0xFF00BFA5),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(12.dp)
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "ShopFlow",
                        fontWeight = FontWeight.Black,
                        fontSize = 26.sp,
                        color = Color.White,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                modifier = Modifier
                    .background(
                        brush = Brush.horizontalGradient(
                            listOf(Color(0xFF00BFA5), Color(0xFF0288D1))
                        )
                    )
                    .shadow(4.dp)
                    .padding(horizontal = 4.dp),
                actions = {
                    IconWithBadge(
                        count = favoriteItems.size,
                        icon = Icons.Default.Favorite,
                        contentDescription = "Favorites",
                        onClick = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    if (favoriteItems.isEmpty()) "No favorites" else "Favorites: ${favoriteItems.size} items"
                                )
                            }
                        }
                    )
                    IconWithBadge(
                        count = 0,
                        icon = Icons.Default.Search,
                        contentDescription = "Search",
                        onClick = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Search opened")
                            }
                        }
                    )
                    IconWithBadge(
                        count = cartItems.size,
                        icon = Icons.Default.ShoppingCart,
                        contentDescription = "Cart",
                        onClick = {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    if (cartItems.isEmpty()) "Cart is empty" else "Cart: ${cartItems.size} items"
                                )
                            }
                        }
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF8FAFC))
            ) {
                AutoScrollingProductBanner(
                    productList = productList.take(6)
                )

                Spacer(modifier = Modifier.height(16.dp))

                CategorySection(
                    selectedCategory = selectedCategory.value,
                    onCategorySelected = { selectedCategory.value = it }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "✨ Trending Now",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    ),
                    modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
                )

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

@Composable
fun AutoScrollingProductBanner(productList: List<Product>) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        while (true) {
            delay(2500L) // Change delay as needed
            val nextIndex = (listState.firstVisibleItemIndex + 1) % productList.size
            coroutineScope.launch {
                listState.animateScrollToItem(nextIndex)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFF00BFA5), Color(0xFF0288D1))
                )
            )
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        LazyRow(
            state = listState,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(productList) { product ->
                Image(
                    painter = painterResource(id = product.imageRes),
                    contentDescription = product.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            // Handle click if needed
                        }
                )
            }
        }
    }
}

@Composable
fun IconWithBadge(
    count: Int,
    icon: ImageVector,
    contentDescription: String,
    onClick: () -> Unit
) {
    BadgedBox(
        badge = {
            if (count > 0) {
                Badge(
                    modifier = Modifier.offset(x = (-8).dp, y = 8.dp)
                ) {
                    Text("$count")
                }
            }
        }
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = CircleShape
                )
                .shadow(4.dp, CircleShape)
        ) {
            Icon(
                icon,
                contentDescription = contentDescription,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

