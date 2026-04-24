package com.bedoya.compartrip.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bedoya.compartrip.domain.usecase.BuscarViajesUseCase
import com.bedoya.compartrip.domain.usecase.ObtenerViajesUseCase
import com.bedoya.compartrip.data.repository.RepositorioCiudades
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel  // le dice a Hilt que este ViewModel recibe inyecciones
class HomeViewModel @Inject constructor(
    // Hilt inyecta estos casos de uso automáticamente
    private val obtenerViajes: ObtenerViajesUseCase,
    private val buscarViajes: BuscarViajesUseCase,
    private val repositorioCiudades: RepositorioCiudades
) : ViewModel() {

    // MutableStateFlow → estado interno que solo el ViewModel puede modificar
    private val _estado = MutableStateFlow(EstadoUiHome())

    // StateFlow → versión de solo lectura que expone la pantalla
    // La pantalla observa este Flow y se redibuja cuando cambia
    val estado: StateFlow<EstadoUiHome> = _estado.asStateFlow()

    init {
        // init se ejecuta cuando se crea el ViewModel → cargamos los viajes al arrancar
        cargarViajes()
    }

    fun cargarViajes() {
        viewModelScope.launch {
            // viewModelScope → coroutine ligada al ciclo de vida del ViewModel
            // cuando el ViewModel muere, esta coroutine se cancela automáticamente
            _estado.update { it.copy(estaCargando = true, error = null) }
            // copy() → crea una copia del estado cambiando solo los campos indicados

            obtenerViajes.ejecutar()
                .catch { excepcion ->
                    // catch → captura cualquier error del Flow
                    _estado.update {
                        it.copy(estaCargando = false, error = excepcion.message)
                    }
                }
                .collect { listaViajes ->
                    // collect → recibe cada nueva lista cuando cambia la BD
                    _estado.update {
                        it.copy(estaCargando = false, viajes = listaViajes)
                    }
                }
        }
    }

    fun alCambiarBusqueda(texto: String) {
        _estado.update { it.copy(textoBusqueda = texto) }
        buscar(normalizar(texto), _estado.value.filtroTipo, _estado.value.modoBusqueda)
    }

    fun alCambiarFiltro(tipo: String) {
        _estado.update { it.copy(filtroTipo = tipo) }
        buscar(_estado.value.textoBusqueda, tipo, _estado.value.modoBusqueda)
    }

    fun alCambiarModoBusqueda(modo: String) {
        _estado.update { it.copy(modoBusqueda = modo) }
        buscar(_estado.value.textoBusqueda, _estado.value.filtroTipo, modo)
    }

    private fun buscar(texto: String, tipo: String, modo: String) {
        viewModelScope.launch {
            buscarViajes.ejecutar(texto, tipo, modo)
                .catch { excepcion ->
                    _estado.update { it.copy(error = excepcion.message) }
                }
                .collect { listaViajes ->
                    _estado.update { it.copy(viajes = listaViajes) }
                }
        }
    }

    private fun normalizar(texto: String): String {
        return texto.trim()
            .lowercase()
            .replace("á", "a")
            .replace("é", "e")
            .replace("í", "i")
            .replace("ó", "o")
            .replace("ú", "u")
            .replace("ü", "u")
            .replace("ñ", "n")
    }

    suspend fun buscarCiudades(texto: String) =
        repositorioCiudades.buscarCiudades(texto).getOrDefault(emptyList())

}