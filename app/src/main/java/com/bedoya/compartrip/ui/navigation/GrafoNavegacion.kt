package com.bedoya.compartrip.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bedoya.compartrip.ui.screens.completarperfil.PantallaCompletarPerfil
import com.bedoya.compartrip.ui.screens.home.PantallaHome
import com.bedoya.compartrip.ui.screens.login.PantallaLogin
import com.bedoya.compartrip.ui.screens.publicar.PantallaPublicarViaje
import com.bedoya.compartrip.ui.screens.detalle.PantallaDetalleViaje
import com.bedoya.compartrip.ui.screens.perfil.PantallaPerfil

@Composable
fun GrafoNavegacion(
    controladorNav: NavHostController = rememberNavController()
) {
    NavHost(
        navController = controladorNav,
        startDestination = Destinos.Login.ruta  // pantalla inicial
    ) {
        composable(Destinos.Login.ruta) {
            PantallaLogin(
                alIniciarSesion = { esNuevo ->
                    if (esNuevo) {
                        // Usuario nuevo → completar perfil primero
                        controladorNav.navigate(Destinos.CompletarPerfil.ruta) {
                            popUpTo(Destinos.Login.ruta) { inclusive = true }
                        }
                    } else {
                        // Usuario existente → Home directamente
                        controladorNav.navigate(Destinos.Home.ruta) {
                            popUpTo(Destinos.Login.ruta) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(Destinos.CompletarPerfil.ruta) {
            PantallaCompletarPerfil(
                alTerminar = {
                    controladorNav.navigate(Destinos.Home.ruta) {
                        popUpTo(Destinos.Login.ruta) { inclusive = true }
                    }
                }
            )
        }

        composable(Destinos.Home.ruta) {
            PantallaHome(
                alPulsarPublicar = {
                    controladorNav.navigate(Destinos.PublicarViaje.ruta)
                },
                alPulsarViaje = { idViaje ->
                    controladorNav.navigate(Destinos.DetalleViaje.crearRuta(idViaje))
                },
                alPulsarPerfil = {
                    controladorNav.navigate(Destinos.Perfil.ruta)
                }
            )
        }

        composable(
            route = Destinos.DetalleViaje.ruta,
            arguments = listOf(
                navArgument("idViaje") { type = NavType.IntType }
                // declara que esta ruta espera un argumento entero llamado "idViaje"
            )
        ) { entradaPila ->
            val idViaje = entradaPila.arguments?.getInt("idViaje") ?: 0
            PantallaDetalleViaje(
                idViaje = idViaje,
                alVolver = { controladorNav.popBackStack() }
                // popBackStack → vuelve a la pantalla anterior
            )
        }

        composable(Destinos.PublicarViaje.ruta) {
            PantallaPublicarViaje(
                alVolver = { controladorNav.popBackStack() },
                alPublicar = { controladorNav.popBackStack() }
            )
        }

        composable(Destinos.Perfil.ruta) {
            PantallaPerfil(
                alVolver = { controladorNav.popBackStack() },
                alCerrarSesion = {
                    controladorNav.navigate(Destinos.Login.ruta) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
