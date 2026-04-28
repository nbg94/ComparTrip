package com.bedoya.compartrip.ui.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.bedoya.compartrip.ui.theme.VerdeTurquesa

data class ItemNavegacion(
    val destino: Destinos,
    val icono: ImageVector,
    val etiqueta: String
)

val itemsNavegacion = listOf(
    ItemNavegacion(Destinos.Home, Icons.Default.Search, "Buscar"),
    ItemNavegacion(Destinos.Favoritos, Icons.Default.Favorite, "Favoritos"),
    ItemNavegacion(Destinos.MisViajes, Icons.Default.DateRange, "Mis viajes"),
    ItemNavegacion(Destinos.Mensajes, Icons.Default.MailOutline, "Mensajes"),
    ItemNavegacion(Destinos.Perfil, Icons.Default.Person, "Perfil")
)

@Composable
fun BarraNavegacionInferior(
    controladorNav: NavHostController,
    viewModel: NotificacionesViewModel = hiltViewModel()
) {
    val entrada by controladorNav.currentBackStackEntryAsState()
    val rutaActual = entrada?.destination?.route
    val mensajesNoLeidos by viewModel.mensajesNoLeidos.collectAsState()
    val solicitudesPendientes by viewModel.solicitudesPendientes.collectAsState()

    val pantallasConBarra = listOf(
        Destinos.Home.ruta,
        Destinos.Favoritos.ruta,
        Destinos.MisViajes.ruta,
        Destinos.Mensajes.ruta,
        Destinos.Perfil.ruta
    )

    if (rutaActual in pantallasConBarra) {
        NavigationBar(containerColor = MaterialTheme.colorScheme.surface) {
            itemsNavegacion.forEach { item ->
                val seleccionado = rutaActual == item.destino.ruta
                NavigationBarItem(
                    selected = seleccionado,
                    onClick = {
                        if (!seleccionado) {
                            controladorNav.navigate(item.destino.ruta) {
                                popUpTo(Destinos.Home.ruta) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    icon = {
                        // Badge según el destino
                        when (item.destino) {
                            Destinos.Mensajes -> {
                                BadgedBox(
                                    badge = {
                                        if (mensajesNoLeidos > 0) {
                                            Badge(containerColor = VerdeTurquesa) {
                                                Text("$mensajesNoLeidos")
                                            }
                                        }
                                    }
                                ) {
                                    Icon(item.icono, contentDescription = item.etiqueta)
                                }
                            }
                            Destinos.MisViajes -> {
                                BadgedBox(
                                    badge = {
                                        if (solicitudesPendientes > 0) {
                                            Badge(containerColor = VerdeTurquesa) {
                                                Text("$solicitudesPendientes")
                                            }
                                        }
                                    }
                                ) {
                                    Icon(item.icono, contentDescription = item.etiqueta)
                                }
                            }
                            else -> Icon(item.icono, contentDescription = item.etiqueta)
                        }
                    },
                    label = { Text(item.etiqueta) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = VerdeTurquesa,
                        selectedTextColor = VerdeTurquesa,
                        indicatorColor = MaterialTheme.colorScheme.surface
                    )
                )
            }
        }
    }
}