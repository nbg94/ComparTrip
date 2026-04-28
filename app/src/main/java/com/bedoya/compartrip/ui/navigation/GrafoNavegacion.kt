package com.bedoya.compartrip.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bedoya.compartrip.ui.screens.chat.PantallaChat
import com.bedoya.compartrip.ui.screens.completarperfil.PantallaCompletarPerfil
import com.bedoya.compartrip.ui.screens.detalle.PantallaDetalleViaje
import com.bedoya.compartrip.ui.screens.favoritos.PantallaFavoritos
import com.bedoya.compartrip.ui.screens.home.PantallaHome
import com.bedoya.compartrip.ui.screens.login.PantallaLogin
import com.bedoya.compartrip.ui.screens.mensajes.PantallaMensajes
import com.bedoya.compartrip.ui.screens.misviajes.PantallaMisViajes
import com.bedoya.compartrip.ui.screens.perfil.PantallaPerfil
import com.bedoya.compartrip.ui.screens.publicar.PantallaPublicarViaje

@Composable
fun GrafoNavegacion(
    controladorNav: NavHostController = rememberNavController()
) {
    Scaffold(
        bottomBar = {
            BarraNavegacionInferior(controladorNav)
        }
    ) { paddingInterno ->
        NavHost(
            navController = controladorNav,
            startDestination = Destinos.Login.ruta,
            modifier = Modifier.padding(paddingInterno)
        ) {
            composable(Destinos.Login.ruta) {
                PantallaLogin(
                    alIniciarSesion = { esNuevo ->
                        if (esNuevo) {
                            controladorNav.navigate(Destinos.CompletarPerfil.ruta) {
                                popUpTo(Destinos.Login.ruta) { inclusive = true }
                            }
                        } else {
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
                    }
                )
            }

            composable(Destinos.Favoritos.ruta) {
                PantallaFavoritos()
            }

            composable(Destinos.MisViajes.ruta) {
                PantallaMisViajes(
                    alPulsarViaje = { idViaje ->
                        controladorNav.navigate(Destinos.DetalleViaje.crearRuta(idViaje))
                    }
                )
            }

            composable(Destinos.Mensajes.ruta) {
                PantallaMensajes(
                    alPulsarConversacion = { idViaje, idReceptor ->
                        controladorNav.navigate(Destinos.Chat.crearRuta(idViaje, idReceptor))
                    }
                )
            }

            composable(Destinos.Perfil.ruta) {
                PantallaPerfil(
                    alCerrarSesion = {
                        controladorNav.navigate(Destinos.Login.ruta) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable(
                route = Destinos.DetalleViaje.ruta,
                arguments = listOf(
                    navArgument("idViaje") { type = NavType.IntType }
                )
            ) { entradaPila ->
                val idViaje = entradaPila.arguments?.getInt("idViaje") ?: 0
                PantallaDetalleViaje(
                    idViaje = idViaje,
                    alVolver = { controladorNav.popBackStack() },
                    alContactar = { idPublicador ->
                        controladorNav.navigate(
                            Destinos.Chat.crearRuta(idViaje, idPublicador)
                        )
                    }
                )
            }

            composable(Destinos.PublicarViaje.ruta) {
                PantallaPublicarViaje(
                    alVolver = { controladorNav.popBackStack() },
                    alPublicar = { controladorNav.popBackStack() }
                )
            }

            composable(
                route = Destinos.Chat.ruta,
                arguments = listOf(
                    navArgument("idViaje") { type = NavType.IntType },
                    navArgument("idReceptor") { type = NavType.StringType }
                )
            ) { entrada ->
                val idViaje = entrada.arguments?.getInt("idViaje") ?: 0
                val idReceptor = entrada.arguments?.getString("idReceptor") ?: ""
                PantallaChat(
                    idViaje = idViaje,
                    idReceptor = idReceptor,
                    alVolver = { controladorNav.popBackStack() }
                )
            }
        }
    }
}