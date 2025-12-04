package com.levelup.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.levelup.ui.viewmodel.AppViewModel
import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(onBack: () -> Unit, onLoginSuccess: () -> Unit) {
    val vm: AppViewModel = viewModel(LocalContext.current as ComponentActivity)


    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }

    var errorLocal by remember { mutableStateOf<String?>(null) }
    var modoLogin by remember { mutableStateOf(true) } // true = Ingresar, false = Registrar

    // Estados desde el ViewModel
    val estaLogueado by vm.estaLogueado.collectAsState(initial = false)
    val errorBackend by vm.errorUsuario.collectAsState(initial = null)
    val cargando by vm.cargandoUsuario.collectAsState(initial = false)


    LaunchedEffect(key1 = estaLogueado) {
        if (estaLogueado) {
            onLoginSuccess()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (modoLogin) "Login" else "Registrarse") }
            )
        }
    ) { inner ->
        Column(
            Modifier
                .padding(inner)
                .padding(30.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {


            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { modoLogin = true },
                    enabled = !modoLogin
                ) { Text("Ingresar") }

                Button(
                    onClick = { modoLogin = false },
                    enabled = modoLogin
                ) { Text("Registrar") }
            }

            if (!modoLogin) {

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre Usuario") },
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true
            )

            OutlinedTextField(
                value = pass,
                onValueChange = { pass = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation()
            )

            Button(
                onClick = {

                    errorLocal = when {
                        !modoLogin && nombre.isBlank() ->
                            "Nombre requerido para registrarse"
                        email.isBlank() || !email.contains("@") ->
                            "Email inválido"
                        pass.length < 6 ->
                            "La contraseña debe tener al menos 6 caracteres"
                        else -> null
                    }

                    if (errorLocal == null) {
                        if (modoLogin) {
                            // Modo INGRESAR
                            vm.loginUsuario(email, pass)
                        } else {
                            // Modo REGISTRAR
                            vm.registrarUsuario(nombre, email, pass)
                        }
                    }
                }
            ) {
                Text(if (modoLogin) "Ingresar" else "Registrarse")
            }

            if (cargando) {
                CircularProgressIndicator()
            }


            errorLocal?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }


            errorBackend?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
