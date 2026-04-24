package com.bedoya.compartrip.ui.screens.completarperfil

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bedoya.compartrip.ui.theme.CremeFondo
import com.bedoya.compartrip.ui.theme.VerdeTurquesa
import com.bedoya.compartrip.ui.theme.VerdeTurquesaClaro
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.bedoya.compartrip.ui.theme.VerdeTurquesaOscuro

@Composable
fun PantallaCompletarPerfil(
    alTerminar: () -> Unit,
    viewModel: CompletarPerfilViewModel = hiltViewModel()
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    LaunchedEffect(estado.guardadoConExito) {
        if (estado.guardadoConExito) alTerminar()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CremeFondo)
    ) {
        // Franja turquesa arriba
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(VerdeTurquesa)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "👋 ¡Bienvenida!",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Cuéntanos cómo te llamas\npara que otros viajeros te conozcan",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(80.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CremeFondo),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Completa tu perfil",
                        style = MaterialTheme.typography.titleLarge,
                        color = VerdeTurquesa
                    )

                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Completa tu perfil",
                            style = MaterialTheme.typography.titleLarge,
                            color = VerdeTurquesa
                        )

                        // ---- FOTO DE PERFIL (OPCIONAL) ---- añade desde aquí
                        val lanzadorFoto = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.PickVisualMedia()
                        ) { uri: Uri? ->
                            uri?.let { viewModel.alCambiarFoto(it.toString()) }
                        }

                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .align(Alignment.CenterHorizontally)
                                .clip(CircleShape)
                                .border(3.dp, VerdeTurquesa, CircleShape)
                                .clickable {
                                    lanzadorFoto.launch(
                                        PickVisualMediaRequest(
                                            ActivityResultContracts.PickVisualMedia.ImageOnly
                                        )
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            if (estado.urlFotoLocal != null) {
                                // Si hay foto la mostramos
                                AsyncImage(
                                    model = estado.urlFotoLocal,
                                    contentDescription = "Foto de perfil",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                // Si no hay foto mostramos el icono de cámara
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text("📷", fontSize = 28.sp)
                                    Text(
                                        text = "Foto\n(opcional)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = VerdeTurquesa,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    OutlinedTextField(
                        value = estado.nombre,
                        onValueChange = viewModel::alCambiarNombre,
                        label = { Text("Nombre *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = estado.errorNombre != null,
                        supportingText = {
                            estado.errorNombre?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words
                        )
                    )

                    OutlinedTextField(
                        value = estado.apellidos,
                        onValueChange = viewModel::alCambiarApellidos,
                        label = { Text("Apellidos *") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        isError = estado.errorApellidos != null,
                        supportingText = {
                            estado.errorApellidos?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words
                        )
                    )

                    OutlinedTextField(
                        value = estado.biografia,
                        onValueChange = viewModel::alCambiarBiografia,
                        label = { Text("Sobre mí (opcional)") },
                        placeholder = { Text("Cuéntanos algo sobre ti...") },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        minLines = 3
                    )

                    if (estado.error != null) {
                        Text(
                            text = estado.error!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Button(
                        onClick = viewModel::alGuardar,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = VerdeTurquesa),
                        enabled = !estado.estaCargando
                    ) {
                        if (estado.estaCargando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text("Empezar a viajar ✈️")
                        }
                    }
                }
            }
        }
    }
}