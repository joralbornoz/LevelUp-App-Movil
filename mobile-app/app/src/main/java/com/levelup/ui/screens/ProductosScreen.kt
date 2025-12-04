package com.levelup.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.levelup.navigation.NavRoutes
import com.levelup.data.Productos
import com.levelup.ui.viewmodel.AppViewModel
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductosScreen(
    onBack: () -> Unit,
    onNavigate: (String) -> Unit
) {
    val vm: AppViewModel = viewModel(LocalContext.current as ComponentActivity)


    val carrito by vm.carrito.collectAsState()
    val cantidad = carrito.size

    // NUEVO: estados de productos desde el ViewModel
    val productos by vm.productos.collectAsState()
    val cargando by vm.cargandoProductos.collectAsState()
    val error by vm.errorProductos.collectAsState()


    LaunchedEffect(Unit) {
        if (productos.isEmpty()) {
            vm.cargarProductosDesdeApi()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Tienda LevelUp") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(onClick = { onNavigate(NavRoutes.Carrito) }) {
                Text("Ir al carrito ($cantidad)")
            }

            when {
                cargando -> {
                    // Loading
                    Box(
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }
                }

                error != null -> {
                    // Error al llamar a la API
                    Text(
                        text = "Error al cargar productos: $error",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {

                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(productos) { p: Productos ->
                            Card(Modifier.fillMaxWidth()) {
                                Column(
                                    Modifier.padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        p.nombre,
                                        style = MaterialTheme.typography.titleLarge
                                    )
                                    Text(
                                        "CLP ${p.precio}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Text(
                                        text = "Stock ${p.stock}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        p.descripcion,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Button(
                                        onClick = { vm.agregarEnCarrito(p.codigo) },
                                        modifier = Modifier.padding(top = 8.dp)
                                    ) {
                                        Text("Agregar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
