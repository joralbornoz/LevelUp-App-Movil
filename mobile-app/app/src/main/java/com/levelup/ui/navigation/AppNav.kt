package com.levelup.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.levelup.ui.screens.*
import com.levelup.ui.viewmodel.AppViewModel

@Composable
fun AppNav() {
    val nav = rememberNavController()
    val vm: AppViewModel = viewModel(LocalContext.current as ComponentActivity)

    val registrado by vm.estaLogueado.collectAsState(initial = false)
    val usuarioBackend by vm.usuarioBackend.collectAsState()

    val inicio = if (registrado) NavRoutes.Menu else NavRoutes.Login

    NavHost(navController = nav, startDestination = inicio) {

        composable(NavRoutes.Login) {
            LoginScreen(
                onBack = { },
                onLoginSuccess = {
                    nav.navigate(NavRoutes.Menu) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(NavRoutes.Menu) {
            MenuScreen(onNavigate = { nav.navigate(it) })
        }

        composable(NavRoutes.Productos) {
            ProductosScreen(
                onBack = { nav.popBackStack() },
                onNavigate = { nav.navigate(it) }
            )
        }

        composable(NavRoutes.Carrito) {
            CarritoScreen(onBack = { nav.popBackStack() })
        }

        composable(NavRoutes.Perfil) {
            ProfileScreen(onBack = { nav.popBackStack() })
        }

        composable(NavRoutes.Sucursales) {
            SucursalesScreen(onBack = { nav.popBackStack() })
        }

        composable(NavRoutes.OfertasExternas) {
            OfertasExternasScreen(onBack = { nav.popBackStack() })
        }

        // 🔐 RUTA ADMIN PROTEGIDA
        composable(NavRoutes.Admin) {
            if (usuarioBackend?.rol == "ADMIN") {
                AdminScreen(onBack = { nav.popBackStack() })
            } else {
                // Si alguien intenta entrar sin ser admin
                LaunchedEffect(Unit) {
                    nav.popBackStack()
                }
            }
        }
    }
}
