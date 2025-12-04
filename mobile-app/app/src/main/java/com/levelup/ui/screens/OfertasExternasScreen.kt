package com.levelup.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.levelup.ui.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfertasExternasScreen(onBack: () -> Unit) {
    val vm: AppViewModel = viewModel(LocalContext.current as ComponentActivity)

    val ofertas by vm.ofertasExternas.collectAsState()
    val cargando by vm.cargandoOfertasExternas.collectAsState()
    val error by vm.errorOfertasExternas.collectAsState()

    // Al entrar a la pantalla, carga las ofertas
    LaunchedEffect(Unit) {
        vm.cargarOfertasExternas()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Ofertas Gamer") },
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
                .padding(12.dp)
        ) {

            if (cargando) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    Text("Cargando ofertas...")
                }
            }

            error?.let { msg ->
                if (msg.isNotBlank()) {
                    Text(
                        text = msg,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(ofertas) { deal ->
                    Card(Modifier.fillMaxWidth()) {
                        Row(
                            Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(deal.imagen),
                                contentDescription = null,
                                modifier = Modifier.size(64.dp)
                            )

                            Column(
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    deal.titulo,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Antes: US$ ${deal.precioNormal}")
                                Text("Ahora: US$ ${deal.precioOferta}")
                                Text("Rating oferta: ${deal.rating}")
                            }
                        }
                    }
                }
            }
        }
    }
}
