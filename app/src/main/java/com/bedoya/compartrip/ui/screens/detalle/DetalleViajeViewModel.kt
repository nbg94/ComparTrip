package com.bedoya.compartrip.ui.screens.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bedoya.compartrip.ConfiguracionApi
import com.bedoya.compartrip.data.repository.RepositorioRutas
import com.bedoya.compartrip.data.repository.RepositorioUsuario
import com.bedoya.compartrip.data.repository.RepositorioViaje
import com.bedoya.compartrip.domain.model.aDominio
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
    private val repositorioRutas: RepositorioRutas
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

                // Calculamos la ruta entre origen y destino
                calcularRuta(viaje.origen, viaje.destino)

                repositorioUsuario.obtenerUsuario(viaje.idPublicador).collect { usuarioEntidad ->
                    _estado.update { it.copy(publicador = usuarioEntidad?.aDominio()) }
                }
            }
        }
    }

    private fun calcularRuta(origen: String, destino: String) {
        viewModelScope.launch {
            android.util.Log.d("RUTA", "Calculando ruta: $origen → $destino")
            _estado.update { it.copy(cargandoRuta = true) }
            try {
                // Primero geocodificamos origen y destino con Nominatim
                // para obtener las coordenadas
                val coordOrigen = geocodificar(origen) ?: run {
                    android.util.Log.e("RUTA", "No se pudo geocodificar: $origen")
                    _estado.update { it.copy(cargandoRuta = false) }
                    return@launch
                }
                val coordDestino = geocodificar(destino) ?: run {
                    android.util.Log.e("RUTA", "No se pudo geocodificar: $destino")
                    _estado.update { it.copy(cargandoRuta = false) }
                    return@launch
                }

                android.util.Log.d("RUTA", "Llamando a ORS con coords: $coordOrigen → $coordDestino")

                val resultado = repositorioRutas.calcularRuta(
                    origenLat = coordOrigen.first,
                    origenLon = coordOrigen.second,
                    destinoLat = coordDestino.first,
                    destinoLon = coordDestino.second,
                    apiKey = ConfiguracionApi.ORS_KEY
                )

                resultado.fold(
                    onSuccess = { ruta ->
                        android.util.Log.d("RUTA", "Ruta obtenida: ${ruta.rutas.firstOrNull()?.resumen?.distanciaMetros} m")
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
                    onFailure = {
                        android.util.Log.e("RUTA", "Error ORS: ${it.message}")
                        _estado.update { it.copy(cargandoRuta = false) }
                    }
                )
            } catch (e: Exception) {
                android.util.Log.e("RUTA", "Excepción: ${e.message}")
                _estado.update { it.copy(cargandoRuta = false) }
            }
        }
    }

    private suspend fun geocodificar(ciudad: String): Pair<Double, Double>? {
        android.util.Log.d("RUTA", "Geocodificando: $ciudad")
        return kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            try {
                val ciudadEncoded = java.net.URLEncoder.encode(ciudad.trim(), "UTF-8")
                val url = "https://nominatim.openstreetmap.org/search?q=$ciudadEncoded&format=json&limit=1"
                android.util.Log.d("RUTA", "URL: $url")

                val client = okhttp3.OkHttpClient()
                val request = okhttp3.Request.Builder()
                    .url(url)
                    .header("User-Agent", "Compartrip/1.0 (Android)")
                    .build()
                val response = client.newCall(request).execute()
                val body = response.body?.string() ?: run {
                    android.util.Log.d("RUTA", "Body vacío para $ciudad")
                    return@withContext null
                }
                android.util.Log.d("RUTA", "Respuesta: $body")

                val json = org.json.JSONArray(body)
                if (json.length() == 0) {
                    android.util.Log.d("RUTA", "Sin resultados para $ciudad")
                    return@withContext null
                }
                val obj = json.getJSONObject(0)
                val lat = obj.getString("lat").toDouble()
                val lon = obj.getString("lon").toDouble()
                android.util.Log.d("RUTA", "Coordenadas $ciudad: $lat, $lon")
                Pair(lat, lon)
            } catch (e: Exception) {
                android.util.Log.e("RUTA", "Error geocodificando $ciudad: ${e.message}")
                null
            }
        }
    }
}