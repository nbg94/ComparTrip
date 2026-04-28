package com.bedoya.compartrip.ui.screens.perfil

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.bedoya.compartrip.domain.model.Usuario
import com.bedoya.compartrip.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfil(
    alCerrarSesion: () -> Unit,
    viewModel: PerfilViewModel = hiltViewModel()
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val contexto = LocalContext.current

    LaunchedEffect(estado.sesionCerrada) {
        if (estado.sesionCerrada) alCerrarSesion()
    }

    // ---- Diálogo verificación DNI ----
    if (estado.mostrarDialogoVerificacion) {
        AlertDialog(
            onDismissRequest = viewModel::alCerrarDialogoVerificacion,
            icon = { Text("🪪", fontSize = 32.sp) },
            title = { Text("Verificar identidad") },
            text = {
                Text(
                    "La verificación de identidad hace tu perfil más confiable para otros viajeros.\n\n" +
                            "En una versión real, aquí se solicitaría una foto de tu DNI o pasaporte " +
                            "a través de un servicio seguro.\n\n¿Simular la verificación?",
                    textAlign = TextAlign.Center
                )
            },
            confirmButton = {
                Button(
                    onClick = viewModel::alConfirmarVerificacion,
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeTurquesa)
                ) { Text("Verificar") }
            },
            dismissButton = {
                TextButton(onClick = viewModel::alCerrarDialogoVerificacion) {
                    Text("Cancelar")
                }
            }
        )
    }

    // ---- Diálogo editar biografía ----
    if (estado.mostrarDialogoBiografia) {
        var textoBio by remember { mutableStateOf(estado.usuario?.biografia ?: "") }
        AlertDialog(
            onDismissRequest = viewModel::alCerrarDialogoBiografia,
            title = { Text("Editar sobre mí") },
            text = {
                OutlinedTextField(
                    value = textoBio,
                    onValueChange = { textoBio = it },
                    label = { Text("Sobre mí") },
                    placeholder = { Text("Cuéntanos algo sobre ti...") },
                    modifier = Modifier.fillMaxWidth().height(150.dp),
                    minLines = 4,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.alGuardarBiografia(textoBio)
                        viewModel.alCerrarDialogoBiografia()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = VerdeTurquesa)
                ) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = viewModel::alCerrarDialogoBiografia) {
                    Text("Cancelar")
                }
            }
        )
    }

    Scaffold(
        containerColor = CremeFondo,
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeTurquesa,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingInterno ->
        when {
            estado.estaCargando -> {
                Box(
                    Modifier.fillMaxSize().padding(paddingInterno),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator(color = VerdeTurquesa) }
            }
            estado.usuario != null -> {
                ContenidoPerfil(
                    usuario = estado.usuario!!,
                    totalViajes = estado.totalViajesPublicados,
                    paddingInterno = paddingInterno,
                    alVerificarDni = viewModel::alPulsarVerificarDni,
                    alCerrarSesion = { viewModel.cerrarSesion(contexto, alCerrarSesion) },
                    alCambiarFoto = viewModel::alCambiarFoto,
                    alEditarBiografia = viewModel::alPulsarEditarBiografia
                )
            }
        }
    }
}

@Composable
private fun ContenidoPerfil(
    usuario: Usuario,
    totalViajes: Int,
    paddingInterno: PaddingValues,
    alVerificarDni: () -> Unit,
    alCerrarSesion: () -> Unit,
    alCambiarFoto: (String) -> Unit,
    alEditarBiografia: () -> Unit
) {
    val lanzadorFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? -> uri?.let { alCambiarFoto(it.toString()) } }

    val partesNombre = usuario.nombre.split(" ")
    val nombre = partesNombre.firstOrNull() ?: usuario.nombre
    val apellidos = partesNombre.drop(1).joinToString(" ")

    val nivelUsuario = when {
        totalViajes == 0 -> "🌱 Usuario nuevo"
        totalViajes < 5 -> "⭐ Viajero principiante"
        totalViajes < 15 -> "🌟 Viajero experimentado"
        else -> "🏆 Viajero experto"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingInterno)
            .verticalScroll(rememberScrollState())
    ) {
        // ---- Cabecera ----
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(VerdeTurquesa)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Foto de perfil con botón cámara
                Box(
                    modifier = Modifier.clickable {
                        lanzadorFoto.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    }
                ) {
                    if (usuario.urlFoto != null) {
                        AsyncImage(
                            model = usuario.urlFoto,
                            contentDescription = "Foto de perfil",
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape)
                                .background(VerdeTurquesaOscuro),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = nombre.firstOrNull()?.toString() ?: "?",
                                fontSize = 36.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    // Icono cámara
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(VerdeTurquesa),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("📷", fontSize = 14.sp)
                    }

                    if (usuario.dniVerificado) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF4CAF50)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Check,
                                contentDescription = "Verificado",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Text(nombre, style = MaterialTheme.typography.headlineSmall,
                    color = Color.White, fontWeight = FontWeight.Bold)
                if (apellidos.isNotBlank()) {
                    Text(apellidos, style = MaterialTheme.typography.titleMedium,
                        color = VerdeTurquesaClaro)
                }
                Surface(shape = RoundedCornerShape(20.dp), color = VerdeTurquesaOscuro) {
                    Text(
                        text = nivelUsuario,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ---- Stats ----
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatPerfil("✈️", "$totalViajes", "Viajes")
                    VerticalDivider(modifier = Modifier.height(48.dp))
                    StatPerfil(
                        "⭐",
                        if (usuario.valoracionMedia > 0f)
                            String.format("%.1f", usuario.valoracionMedia) else "—",
                        "Valoración"
                    )
                    VerticalDivider(modifier = Modifier.height(48.dp))
                    StatPerfil(
                        if (usuario.dniVerificado) "✅" else "❌",
                        if (usuario.dniVerificado) "Sí" else "No",
                        "Verificado"
                    )
                }
            }

            // ---- Info ----
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text("📋 Información", style = MaterialTheme.typography.titleMedium,
                        color = VerdeTurquesa, fontWeight = FontWeight.SemiBold)
                    FilaInfo("📧", "Correo", usuario.correo)
                    FilaInfo(
                        "📅", "Miembro desde",
                        java.text.SimpleDateFormat("MMMM yyyy",
                            java.util.Locale.forLanguageTag("es-ES"))
                            .format(java.util.Date(usuario.creadoEn))
                    )
                    // ---- Biografía con botón editar ----
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            if (!usuario.biografia.isNullOrBlank()) {
                                FilaInfo("💬", "Sobre mí", usuario.biografia)
                            } else {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("💬", fontSize = 20.sp)
                                    Text("Añade algo sobre ti",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = GrisTexto)
                                }
                            }
                        }
                        IconButton(
                            onClick = alEditarBiografia,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar",
                                tint = VerdeTurquesa, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            // ---- Verificación DNI ----
            if (!usuario.dniVerificado) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = VerdeTurquesaClaro),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🪪", fontSize = 32.sp)
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Verifica tu identidad",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold, color = VerdeTurquesaOscuro)
                            Text("Genera más confianza en otros viajeros",
                                style = MaterialTheme.typography.bodySmall, color = GrisTexto)
                        }
                        Button(onClick = alVerificarDni,
                            colors = ButtonDefaults.buttonColors(containerColor = VerdeTurquesa),
                            shape = RoundedCornerShape(8.dp)) { Text("Verificar") }
                    }
                }
            } else {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("✅", fontSize = 32.sp)
                        Column {
                            Text("Identidad verificada",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold, color = Color(0xFF2E7D32))
                            Text("Tu perfil es de confianza para otros viajeros",
                                style = MaterialTheme.typography.bodySmall, color = GrisTexto)
                        }
                    }
                }
            }

            // ---- Reseñas ----
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("💬 Opiniones de otros viajeros",
                        style = MaterialTheme.typography.titleMedium,
                        color = VerdeTurquesa, fontWeight = FontWeight.SemiBold)
                    Text(
                        "Aún no tienes valoraciones. ¡Comparte tu primer viaje!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = GrisTexto, textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp)
                    )
                }
            }

            // ---- Cerrar sesión ----
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = alCerrarSesion,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Cerrar sesión")
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun StatPerfil(icono: String, valor: String, etiqueta: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(icono, fontSize = 24.sp)
        Text(valor, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(etiqueta, style = MaterialTheme.typography.bodySmall, color = GrisTexto)
    }
}

@Composable
private fun FilaInfo(icono: String, etiqueta: String, valor: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(icono, fontSize = 20.sp)
        Column {
            Text(etiqueta, style = MaterialTheme.typography.bodySmall, color = GrisTexto)
            Text(valor, style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium)
        }
    }
}