package com.bedoya.compartrip.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bedoya.compartrip.domain.model.TipoViaje
import com.bedoya.compartrip.domain.model.Viaje
import com.bedoya.compartrip.domain.usecase.PublicarViajeUseCase
import com.bedoya.compartrip.data.repository.RepositorioCiudades
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PublicarViajeViewModel @Inject constructor(
    private val publicarViaje: PublicarViajeUseCase,
    private val repositorioCiudades: RepositorioCiudades
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoUiPublicar())
    val estado: StateFlow<EstadoUiPublicar> = _estado.asStateFlow()

    fun alCambiarOrigen(valor: String) = _estado.update { it.copy(origen = valor) }
    fun alCambiarDestino(valor: String) = _estado.update { it.copy(destino = valor) }
    fun alCambiarFechaIda(valor: Long) = _estado.update { it.copy(fechaIda = valor) }
    fun alCambiarFechaVuelta(valor: Long) = _estado.update { it.copy(fechaVuelta = valor) }
    fun alCambiarTipo(valor: TipoViaje) = _estado.update { it.copy(tipo = valor) }
    fun alCambiarPlazas(valor: String) = _estado.update { it.copy(plazas = valor) }
    fun alCambiarDescripcion(valor: String) = _estado.update { it.copy(descripcion = valor) }
    fun alCambiarPrecio(valor: String) = _estado.update { it.copy(precio = valor) }
    fun alCambiarFumadores(valor: Boolean) = _estado.update { it.copy(admiteFumadores = valor) }
    fun alCambiarMascotas(valor: Boolean) = _estado.update { it.copy(admiteMascotas = valor) }

    fun alCambiarSoloHombres(valor: Boolean) = _estado.update {
        it.copy(soloHombres = valor, soloMujeres = if (valor) false else it.soloMujeres)
    }

    // si activas solo hombres, desactiva solo mujeres automáticamente y viceversa
    fun alCambiarSoloMujeres(valor: Boolean) = _estado.update {
        it.copy(soloMujeres = valor, soloHombres = if (valor) false else it.soloHombres)
    }

    fun alCambiarEdadMinima(valor: String) = _estado.update { it.copy(edadMinima = valor) }

    fun alCambiarEdadMaxima(valor: String) = _estado.update { it.copy(edadMaxima = valor) }

    fun alAgregarFoto(uri: String) {
        if (_estado.value.fotosUri.size >= 5) return  // máximo 5 fotos
        _estado.update { it.copy(fotosUri = it.fotosUri + uri) }
    }

    fun alEliminarFoto(uri: String) {
        val nuevaLista = _estado.value.fotosUri - uri
        val nuevoIndice = _estado.value.indiceFotoPortada.coerceAtMost(
            (nuevaLista.size - 1).coerceAtLeast(0)
        )
        _estado.update { it.copy(fotosUri = nuevaLista, indiceFotoPortada = nuevoIndice) }
    }

    fun alElegirPortada(indice: Int) =
        _estado.update { it.copy(indiceFotoPortada = indice) }

    fun alPublicar(idUsuarioActual: String) {
        viewModelScope.launch {
            _estado.update { it.copy(estaCargando = true, error = null) }
            try {
                val viaje = Viaje(
                    idPublicador = idUsuarioActual,
                    origen = _estado.value.origen,
                    destino = _estado.value.destino,
                    fecha = _estado.value.fechaIda ?: System.currentTimeMillis(),
                    tipo = _estado.value.tipo,
                    // plazas solo obligatorio si es TRANSPORTE
                    plazasDisponibles = _estado.value.plazas.toIntOrNull() ?: 1,
                    descripcion = _estado.value.descripcion,
                    precio = _estado.value.precio.toDoubleOrNull(),
                    admiteFumadores = _estado.value.admiteFumadores,
                    admiteMascotas = _estado.value.admiteMascotas,
                    soloMujeres = _estado.value.soloMujeres,
                    soloHombres = _estado.value.soloHombres,
                    edadMinima = _estado.value.edadMinima.toIntOrNull(),
                    edadMaxima = _estado.value.edadMaxima.toIntOrNull(),
                    urlsFotos = _estado.value.fotosUri
                )
                publicarViaje.ejecutar(viaje)
                _estado.update { it.copy(estaCargando = false, publicadoConExito = true) }
            } catch (e: Exception) {
                _estado.update { it.copy(estaCargando = false, error = e.message) }
            }
        }
    }

    suspend fun buscarCiudades(texto: String) =
        repositorioCiudades.buscarCiudades(texto).getOrDefault(emptyList())


}