package com.example.closetfit.view

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.closetfit.R
import coil.compose.rememberAsyncImagePainter
import com.example.closetfit.model.Producto
import com.example.closetfit.ui.theme.ClosetFitTheme
import com.example.closetfit.ui.theme.colorPrimario
import com.example.closetfit.ui.theme.colorSecundario
import com.example.closetfit.viewmodel.ProductoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    productoViewModel: ProductoViewModel = viewModel()
) {
    val context = LocalContext.current

    val productos by productoViewModel.allProductos.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        productoViewModel.cargarProductos(context)
    }
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) },
        containerColor = colorPrimario
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item { TopSection(navController) }
            item { CategoriesSection() }

            items(productos) { producto ->
                ProductoCard(producto = producto, onClick = {
                    navController.navigate("detalle_producto/${producto.id}")
                })
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSection(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = colorSecundario.copy(alpha = 0.5f),
                unfocusedContainerColor = colorSecundario.copy(alpha = 0.5f),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        TextButton(onClick = { navController.navigate("perfil") }) {
            Icon(Icons.Default.Person, contentDescription = "Perfil", tint = Color(0xFFD8D78F))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Mi perfil", color = Color.Black)
        }
    }
}

@Composable
fun CategoriesSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colorSecundario)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Categorias", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            CategoryItem(imageRes = R.drawable.polera_negra, text = "Poleras")
            CategoryItem(imageRes = R.drawable.pantalon_jean, text = "Pantalones")
            CategoryItem(imageRes = R.drawable.shorts_deportivos, text = "Shorts")
        }
    }
}

@Composable
fun CategoryItem(@DrawableRes imageRes: Int, text: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = text,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = text, fontSize = 14.sp, textAlign = TextAlign.Center, color = Color.White)
    }
}

@Composable
fun ProductoCard(producto: Producto, onClick: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()

        .clickable { onClick() }
        .padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorSecundario,
            contentColor = Color.White
        )
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            val painter = rememberAsyncImagePainter(producto.imagen)
            Image(painter = painter, contentDescription = producto.nombre, modifier = Modifier.size(80.dp), contentScale = ContentScale.Crop)
            Spacer(modifier = Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = "Talla: ${producto.talla}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "$${producto.precio}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(containerColor = colorSecundario) {
        NavigationBarItem(
            selected = true,
            onClick = { navController.navigate("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate("carrito") },
            icon = {
                BadgedBox(badge = { Badge { Text("") } }) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                }
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun HomeScreenPreview() {
    ClosetFitTheme {
        HomeScreen(rememberNavController())
    }
}
