package com.levelup.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.levelup.ui.theme.*
import com.levelup.ui.viewmodel.AppViewModel
import com.levelup.navigation.NavRoutes
import com.levelup.ui.components.BotonMenu
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(onNavigate: (String) -> Unit) {
    val vm: AppViewModel = viewModel(LocalContext.current as ComponentActivity)
    val name by vm.nombreUsuario.collectAsState()
    val email by vm.emailUsuario.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("LevelUp — Menú") },
                actions = {
                    IconButton(onClick = { vm.logout() }) {
                        Icon(Icons.Default.Logout, contentDescription = "Salir")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = CardBlack, contentColor = TextPrimary),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(

                        "Bienvenido, $name ",
                        style = MaterialTheme.typography.titleLarge,
                        color = SecondaryBlue
                    )
                    Text(
                        email,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            BotonMenu("Productos / Tienda", Icons.Filled.Store) { onNavigate(NavRoutes.Productos) }
            BotonMenu("Carrito", Icons.Filled.ShoppingCart) { onNavigate(NavRoutes.Carrito) }
            BotonMenu("Perfil", Icons.Filled.Person) { onNavigate(NavRoutes.Perfil) }
            BotonMenu("Sucursales", Icons.Filled.LocationOn) { onNavigate(NavRoutes.Sucursales) }
            BotonMenu("Ofertas Externas", Icons.Filled.AttachMoney) { onNavigate(NavRoutes.OfertasExternas) }

        }
    }
}

