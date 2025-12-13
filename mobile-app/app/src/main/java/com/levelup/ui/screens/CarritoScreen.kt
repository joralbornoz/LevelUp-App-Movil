package com.levelup.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.levelup.data.DataProductos
import com.levelup.ui.viewmodel.AppViewModel
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(onBack: () -> Unit) {


    val vm: AppViewModel = viewModel(LocalContext.current as ComponentActivity)
    val compraExitosa by vm.compraExitosa.collectAsState()
    val codigosCarrito by vm.carrito.collectAsState()
    val productosApi by vm.productos.collectAsState()
    val mensajeCarrito by vm.mensajeCarrito.collectAsState()
    val cargandoProductos by vm.cargandoProductos.collectAsState()
    val errorProductos by vm.errorProductos.collectAsState()



    LaunchedEffect(Unit) {
        if (productosApi.isEmpty()) {
            vm.cargarProductosDesdeApi()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Carrito") },
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
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Mensajes de stock / carrito
            mensajeCarrito?.let { msg ->
                Text(
                    text = msg,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (cargandoProductos) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    Text("Cargando productos...")
                }
            }

            errorProductos?.let { msg ->
                if (msg.isNotBlank()) {
                    Text(
                        text = msg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (codigosCarrito.isEmpty()) {
                Text("Tu carrito está vacío.")
            } else {
                // Mostrar cada producto del carrito
                codigosCarrito.forEach { codigo ->
                    val p = productosApi.firstOrNull { it.codigo == codigo }
                        ?: DataProductos.firstOrNull { it.codigo == codigo }

                    if (p != null) {
                        Text("${p.nombre} — CLP ${p.precio}")
                    } else {
                        Text("Producto ($codigo) ya no está disponible")
                    }
                }

                Divider()

                val subtotal = vm.total()
                val totalConIva = (subtotal * 1.19).toInt()

                Text(
                    text = "Total: CLP $subtotal",
                    style = MaterialTheme.typography.titleLarge
                )

                Text(
                    text = "Total + IVA (19%): CLP $totalConIva",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { vm.limpiarCarrito() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Vaciar carrito")
                    }

                    Button(
                        onClick = {
                            vm.pagarYGuardarOrden {

                            }
                        },
                        enabled = codigosCarrito.isNotEmpty(),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Pagar")
                    }
                }
            }
        }
    }

    // Mostrar el popup cuando la compra se haya completado
    if (compraExitosa) {
        AlertDialog(
            onDismissRequest = {
                vm.limpiarCompraExitosa() // 👈 IMPORTANTE
            },
            title = { Text("Compra realizada") },
            text = { Text("¡Tu compra se realizó correctamente!") },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.limpiarCompraExitosa() // 👈 AQUÍ TAMBIÉN
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}



