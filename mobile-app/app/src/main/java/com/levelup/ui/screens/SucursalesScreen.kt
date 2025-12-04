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
import com.levelup.ui.components.SucursalCard
import com.levelup.ui.viewmodel.AppViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SucursalesScreen(
    onBack: () -> Unit,
    vm: AppViewModel = viewModel()
) {
    // Estados del ViewModel
    val sucursales by vm.sucursales.collectAsState()
    val cargando by vm.cargandoSuc.collectAsState()
    val error by vm.errorSuc.collectAsState()

   LaunchedEffect(Unit) {
        vm.cargarSucursalesDesdeApi()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Sucursales LevelUp") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            when {
                cargando -> {
                    // Loading Spinner
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                error != null -> {
                    // Error de API
                    Text(
                        text = "Error al cargar sucursales: $error",
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {
                    // Lista desde el microservicio
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(sucursales) { sucursal ->
                            SucursalCard(sucursal = sucursal)
                        }
                    }
                }
            }
        }
    }
}
