package com.levelup.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.levelup.ui.viewmodel.AppViewModel
import androidx.compose.runtime.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(onBack: () -> Unit) {

    val vm: AppViewModel = viewModel(LocalContext.current as ComponentActivity)

    val usuarios by vm.adminUsuarios.collectAsState()
    val cargando by vm.adminCargando.collectAsState()
    val error by vm.adminError.collectAsState()

    var usuarioEditar by remember { mutableStateOf<com.levelup.data.model.Usuario?>(null) }
    var usuarioEliminar by remember { mutableStateOf<com.levelup.data.model.Usuario?>(null) }
    var mostrarDialogExito by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        vm.cargarUsuariosAdmin()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ADMIN - Usuarios") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { vm.cargarUsuariosAdmin() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refrescar")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier.padding(inner).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (cargando) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    CircularProgressIndicator(modifier = Modifier.size(18.dp))
                    Text("Cargando usuarios...")
                }
            }

            error?.let { msg ->
                if (msg.isNotBlank()) Text(msg, color = MaterialTheme.colorScheme.error)
            }

            if (!cargando && usuarios.isEmpty()) {
                Text("No hay usuarios para mostrar.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(usuarios.size) { idx ->
                        val u = usuarios[idx]
                        Card(Modifier.fillMaxWidth()) {
                            Column(Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(u.nombre, style = MaterialTheme.typography.titleMedium)
                                Text(u.email, style = MaterialTheme.typography.bodyMedium)
                                Text("Rol: ${u.rol}", style = MaterialTheme.typography.bodySmall)

                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    OutlinedButton(onClick = { usuarioEditar = u }) {
                                        Text("Editar")
                                    }
                                    Button(
                                        onClick = { usuarioEliminar = u },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                                    ) {
                                        Text("Eliminar")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // -------- Dialog Editar --------
    usuarioEditar?.let { u ->
        var nombre by remember { mutableStateOf(u.nombre) }
        var email by remember { mutableStateOf(u.email) }
        var password by remember { mutableStateOf(u.password) }


        AlertDialog(
            onDismissRequest = { usuarioEditar = null },
            title = { Text("Editar usuario") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = nombre,
                        onValueChange = { nombre = it },
                        label = { Text("Nombre") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth()
                    )

                }
            },
            confirmButton = {
                TextButton(onClick = {
                    vm.actualizarUsuarioAdmin(
                        u.copy(nombre = nombre, email = email, password = password)
                    )
                    usuarioEditar = null
                    mostrarDialogExito = true   // 👈 MOSTRAR POPUP
                }) {
                    Text("Guardar")
                }
            }

        )
    }
    if (mostrarDialogExito) {
        AlertDialog(
            onDismissRequest = { /* no se cierra tocando fuera */ },
            title = { Text("Cambios guardados") },
            text = { Text("Los datos se actualizaron correctamente.") },
            confirmButton = {
                TextButton(onClick = {
                    mostrarDialogExito = false
                }) {
                    Text("Aceptar")
                }
            }
        )
    }


    // -------- Dialog Eliminar --------
    usuarioEliminar?.let { u ->
        AlertDialog(
            onDismissRequest = { usuarioEliminar = null },
            title = { Text("Eliminar usuario") },
            text = { Text("¿Seguro que quieres eliminar a ${u.email}?") },
            confirmButton = {
                TextButton(onClick = {
                    vm.eliminarUsuarioAdmin(u.id)
                    usuarioEliminar = null
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { usuarioEliminar = null }) { Text("Cancelar") }
            }
        )
    }
}
