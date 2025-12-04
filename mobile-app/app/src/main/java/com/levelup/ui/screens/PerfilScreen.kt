package com.levelup.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.levelup.ui.viewmodel.AppViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import java.text.SimpleDateFormat
import java.util.Date
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(onBack: () -> Unit) {
    val vm: AppViewModel = viewModel(LocalContext.current as ComponentActivity)



    // Flujos desde el ViewModel (DataStore)
    val nombreUsuario by vm.nombreUsuario.collectAsState()
    val edadUsuario by vm.edadUsuario.collectAsState()
    val emailUsuario by vm.emailUsuario.collectAsState()
    val direccionUsuario by vm.direccionUsuario.collectAsState()
    val preferenciasUsuario by vm.preferenciasUsuario.collectAsState()

    // Estados editables en la UI
    var editarNombre by remember(nombreUsuario) { mutableStateOf(nombreUsuario) }
    var editarEdad by remember(edadUsuario) { mutableStateOf(edadUsuario) }
    var editarEmail by remember(emailUsuario) { mutableStateOf(emailUsuario) }
    var editarDireccion by remember(direccionUsuario) { mutableStateOf(direccionUsuario) }
    var editarPreferencias by remember(preferenciasUsuario) { mutableStateOf(preferenciasUsuario) }

    // Foto de perfil (solo local)
    var photo by remember { mutableStateOf<Bitmap?>(null) }

    // Estados de backend
    val cargando by vm.cargandoUsuario.collectAsState()
    val errorBackend by vm.errorUsuario.collectAsState()

    // Para mostrar mensaje de "Guardado" cuando todo va bien
    var mostrarGuardadoOk by remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bmp ->
        photo = bmp
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Perfil Usuario") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { inner ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .verticalScroll(scrollState)
                .imePadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Foto de perfil (solo local, no se manda al backend)
            photo?.let {
                Image(
                    bitmap = it.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
            }
            Button(onClick = { cameraLauncher.launch(null) }) {
                Text("Tomar foto")
            }

            // Campos de edición
            OutlinedTextField(
                value = editarNombre,
                onValueChange = { editarNombre = it },
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = editarEdad,
                onValueChange = { editarEdad = it },
                label = { Text("Edad") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = editarEmail,
                onValueChange = { editarEmail = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )


            OutlinedTextField(
                value = editarDireccion,
                onValueChange = { editarDireccion = it },
                label = { Text("Dirección") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = editarPreferencias,
                onValueChange = { editarPreferencias = it },
                label = { Text("Preferencias (ej: PC Gamer, Consolas, Accesorios)") },
                singleLine = false,
                modifier = Modifier.fillMaxWidth()
            )


            Button(
                onClick = {
                    mostrarGuardadoOk = false
                    vm.guardarPerfil(
                        nombre = editarNombre,
                        edad = editarEdad,
                        email = editarEmail,
                        direccion = editarDireccion,
                        preferencias = editarPreferencias
                    )

                    mostrarGuardadoOk = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
            }


            if (cargando) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp))
                    Text("Sincronizando con servidor...")
                }
            }

            errorBackend?.let { msg ->
                if (msg.isNotBlank()) {
                    Text(
                        text = msg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            if (!cargando && errorBackend.isNullOrBlank() && mostrarGuardadoOk) {
                Text(
                    text = "Cambios guardados correctamente.",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Divider(modifier = Modifier.padding(vertical = 12.dp))

            // Historial de compras
            HistorialDeCompras(vm)
        }
    }
}

@Composable
fun HistorialDeCompras(vm: AppViewModel = viewModel(LocalContext.current as ComponentActivity)) {
    val compras by vm.comprasUsuario.collectAsState()

    LaunchedEffect(Unit) {
        vm.cargarHistorialCompras()
    }

    Spacer(Modifier.height(16.dp))
    Text("Historial de compras", style = MaterialTheme.typography.titleLarge)

    if (compras.isEmpty()) {
        Text("Sin compras registradas aún.")
    } else {
        compras.forEach { compra ->
            Text("• ${compra.fecha} — Total ${compra.total} — Detalle: ${compra.detalle}")
        }
    }
}
