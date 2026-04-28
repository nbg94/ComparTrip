package com.bedoya.compartrip.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bedoya.compartrip.SesionUsuario
import com.bedoya.compartrip.data.local.entity.EntidadMensaje
import com.bedoya.compartrip.data.repository.RepositorioMensaje
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject
import javax.inject.Inject

data class MensajeUi(
    val idEmisor: String,
    val nombreEmisor: String,
    val contenido: String,
    val enviadoEn: Long,
    val esMio: Boolean
)

data class EstadoUiChat(
    val mensajes: List<MensajeUi> = emptyList(),
    val textoActual: String = "",
    val conectado: Boolean = false,
    val estaCargando: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repositorioMensaje: RepositorioMensaje
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoUiChat())
    val estado: StateFlow<EstadoUiChat> = _estado.asStateFlow()

    private var webSocket: WebSocket? = null
    private var idViajeActual: Int = 0
    private var idReceptorActual: String = ""

    private val clienteHttp = OkHttpClient()

    fun iniciarChat(idViaje: Int, idReceptor: String) {
        idViajeActual = idViaje
        idReceptorActual = idReceptor

        // ← añade esto aquí
        viewModelScope.launch {
            repositorioMensaje.marcarComoLeidos(idViaje, SesionUsuario.idActual)
        }
        // Primero cargamos mensajes locales
        viewModelScope.launch {
            repositorioMensaje.obtenerMensajesDeViaje(idViaje).collect { entidades ->
                val mensajesUi = entidades.map { entidad ->
                    MensajeUi(
                        idEmisor = entidad.idEmisor,
                        nombreEmisor = if (entidad.idEmisor == SesionUsuario.idActual)
                            SesionUsuario.nombreActual else "Usuario",
                        contenido = entidad.contenido,
                        enviadoEn = entidad.enviadoEn,
                        esMio = entidad.idEmisor == SesionUsuario.idActual
                    )
                }
                _estado.update { it.copy(mensajes = mensajesUi, estaCargando = false) }
            }
        }

        // Conectamos al WebSocket
        conectarWebSocket(idViaje)
    }

    private fun conectarWebSocket(idViaje: Int) {
        val request = Request.Builder()
            .url("wss://compartrip-server.onrender.com")
            .build()

        webSocket = clienteHttp.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                // Al conectar enviamos mensaje de unirse a la sala
                val mensaje = JSONObject().apply {
                    put("tipo", "unirse")
                    put("idViaje", idViaje)
                    put("idUsuario", SesionUsuario.idActual)
                }
                webSocket.send(mensaje.toString())
                _estado.update { it.copy(conectado = true) }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                viewModelScope.launch {
                    try {
                        val json = JSONObject(text)
                        val tipo = json.getString("tipo")

                        if (tipo == "mensaje") {
                            val nuevoMensaje = MensajeUi(
                                idEmisor = json.getString("idEmisor"),
                                nombreEmisor = json.getString("nombreEmisor"),
                                contenido = json.getString("contenido"),
                                enviadoEn = json.getLong("enviadoEn"),
                                esMio = false
                            )

                            // Guardamos en Room
                            repositorioMensaje.insertar(
                                EntidadMensaje(
                                    idViaje = idViajeActual,
                                    idEmisor = nuevoMensaje.idEmisor,
                                    idReceptor = SesionUsuario.idActual,
                                    contenido = nuevoMensaje.contenido,
                                    enviadoEn = nuevoMensaje.enviadoEn,
                                    leido = false,
                                    sincronizado = true
                                )
                            )
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("CHAT", "Error procesando mensaje: ${e.message}")
                    }
                }
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                _estado.update { it.copy(conectado = false, error = "Sin conexión") }
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                _estado.update { it.copy(conectado = false) }
            }
        })
    }

    fun alCambiarTexto(texto: String) =
        _estado.update { it.copy(textoActual = texto) }

    fun enviarMensaje() {
        val texto = _estado.value.textoActual.trim()
        if (texto.isBlank() || webSocket == null) return

        viewModelScope.launch {
            // Enviamos por WebSocket
            val jsonMensaje = JSONObject().apply {
                put("tipo", "mensaje")
                put("idEmisor", SesionUsuario.idActual)
                put("nombreEmisor", SesionUsuario.nombreActual)
                put("contenido", texto)
            }
            webSocket?.send(jsonMensaje.toString())

            // Guardamos en Room inmediatamente (sin esperar al servidor)
            repositorioMensaje.insertar(
                EntidadMensaje(
                    idViaje = idViajeActual,
                    idEmisor = SesionUsuario.idActual,
                    idReceptor = idReceptorActual,
                    contenido = texto,
                    sincronizado = true
                )
            )

            _estado.update { it.copy(textoActual = "") }
        }
    }

    override fun onCleared() {
        super.onCleared()
        webSocket?.close(1000, "Pantalla cerrada")
    }
}