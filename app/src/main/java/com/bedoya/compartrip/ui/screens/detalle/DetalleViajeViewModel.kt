package com.bedoya.compartrip.ui.screens.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bedoya.compartrip.ConfiguracionApi
import com.bedoya.compartrip.SesionUsuario
import com.bedoya.compartrip.data.repository.RepositorioRutas
import com.bedoya.compartrip.data.repository.RepositorioUsuario
import com.bedoya.compartrip.data.repository.RepositorioViaje
import com.bedoya.compartrip.domain.model.aDominio
import com.bedoya.compartrip.domain.usecase.SolicitarViajeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetalleViajeViewModel @Inject constructor(
    private val repositorioViaje: RepositorioViaje,
    private val repositorioUsuario: RepositorioUsuario,
    private val repositorioRutas: RepositorioRutas,
    private val solicitarViaje: SolicitarViajeUseCase
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoUiDetalle())
    val estado: StateFlow<EstadoUiDetalle> = _estado.asStateFlow()

    fun cargarViaje(idViaje: Int) {
        viewModelScope.launch {
            repositorioViaje.obtenerViajePorId(idViaje).collect { entidad ->
                if (entidad == null) {
                    _estado.update { it.copy(estaCargando = false, error = "Viaje no encontrado") }
                    return@collect
                }
                val viaje = entidad.aDominio()
                _estado.update { it.copy(viaje = viaje, estaCargando = false) }
                calcularRuta(viaje.origen, viaje.destino)
                repositorioUsuario.obtenerUsuario(viaje.idPublicador).collect { usuarioEntidad ->
                    _estado.update { it.copy(publicador = usuarioEntidad?.aDominio()) }
                }
            }
        }
    }

    fun alPulsarSolicitarUnirse() {
        // Si ya solicitó, no hace nada
        if (_estado.value.estadoSolicitud != EstadoSolicitud.SIN_SOLICITAR) return
        _estado.update { it.copy(mostrarDialogoSolicitud = true) }
    }

    fun alCerrarDialogoSolicitud() {
        _estado.update { it.copy(mostrarDialogoSolicitud = false) }
    }

    fun alConfirmarSolicitud() {
        val idViaje = _estado.value.viaje?.idViaje ?: return
        viewModelScope.launch {
            _estado.update { it.copy(mostrarDialogoSolicitud = false) }
            val resultado = solicitarViaje.ejecutar(idViaje, SesionUsuario.idActual)
            resultado.fold(
                onSuccess = {
                    _estado.update { it.copy(estadoSolicitud = EstadoSolicitud.PENDIENTE) }
                },
                onFailure = { error ->
                    _estado.update { it.copy(error = error.message) }
                }
            )
        }
    }

    private fun calcularRuta(origen: String, destino: String) {
        viewModelScope.launch {
            android.util.Log.d("RUTA", "Calculando ruta: $origen → $destino")
            _estado.update { it.copy(cargandoRuta = true) }
            try {
                val coordOrigen = geocodificar(origen) ?: run {
                    _estado.update { it.copy(cargandoRuta = false) }
                    return@launch
                }
                val coordDestino = geocodificar(destino) ?: run {
                    _estado.update { it.copy(cargandoRuta = false) }
                    return@launch
                }
                val resultado = repositorioRutas.calcularRuta(
                    origenLat = coordOrigen.first,
                    origenLon = coordOrigen.second,
                    destinoLat = coordDestino.first,
                    destinoLon = coordDestino.second,
                    apiKey = ConfiguracionApi.ORS_KEY
                )
                resultado.fold(
                    onSuccess = { ruta ->
                        val resumen = ruta.rutas.firstOrNull()?.resumen
                        if (resumen != null) {
                            _estado.update {
                                it.copy(
                                    distanciaKm = resumen.distanciaMetros / 1000,
                                    duracionMinutos = resumen.duracionSegundos / 60,
                                    cargandoRuta = false
                                )
                            }
                        }
                    },
                    onFailure = { throwable ->
                        android.util.Log.e("RUTA", "Error ORS: ${throwable.message}")
                        _estado.update { estado -> estado.copy(cargandoRuta = false) }
                    }
                )
            } catch (e: Exception) {
                android.util.Log.e("RUTA", "Excepción: ${e.message}")
                _estado.update { it.copy(cargandoRuta = false) }
            }
        }
    }

    private suspend fun geocodificar(ciudad: String): Pair<Double, Double>? {
        return kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val ciudadEncoded = java.net.URLEncoder.encode(ciudad.trim(), "UTF-8")
                val url = "https://nominatim.openstreetmap.org/search?q=$ciudadEncoded&format=json&limit=1"
                val client = okhttp3.OkHttpClient()
                val request = okhttp3.Request.Builder()
                    .url(url)
                    .header("User-Agent", "Compartrip/1.0 (Android)")
                    .build()
                val response = client.newCall(request).execute()
                val body = response.body?.string() ?: return@withContext null
                val json = org.json.JSONArray(body)
                if (json.length() == 0) return@withContext null
                val obj = json.getJSONObject(0)
                Pair(obj.getString("lat").toDouble(), obj.getString("lon").toDouble())
            } catch (e: Exception) {
                null
            }
        }
    }
}