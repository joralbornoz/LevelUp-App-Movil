package com.levelup.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.collectAsState
import com.levelup.ui.screens.*
import com.levelup.ui.viewmodel.AppViewModel

@Composable
fun AppNav() {
    val nav = rememberNavController()
    val vm: AppViewModel = viewModel()
    val registrado by vm.estaLogueado.collectAsState(initial = false)

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

    }
}
