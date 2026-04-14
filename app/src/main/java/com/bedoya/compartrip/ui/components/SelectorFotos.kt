package com.bedoya.compartrip.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.bedoya.compartrip.ui.theme.NaranjaAccento
import com.bedoya.compartrip.ui.theme.VerdeTurquesa
import com.bedoya.compartrip.ui.theme.VerdeTurquesaClaro

@Composable
fun SelectorFotos(
    fotos: List<String>,
    indiceFotoPortada: Int,
    alAgregarFoto: (String) -> Unit,
    alEliminarFoto: (String) -> Unit,
    alElegirPortada: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Lanzador del selector de imágenes del sistema
    // PickVisualMedia → abre la galería del móvil de forma segura
    val lanzador = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { alAgregarFoto(it.toString()) }
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "📸 Fotos del viaje (opcional, máx. 5)",
            style = MaterialTheme.typography.titleMedium,
            color = VerdeTurquesa
        )
        Text(
            text = "La foto con ⭐ será la portada que verán otros viajeros",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Fotos seleccionadas
            itemsIndexed(fotos) { indice, uri ->
                val esPortada = indice == indiceFotoPortada
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = if (esPortada) 3.dp else 1.dp,
                            color = if (esPortada) NaranjaAccento else Color.LightGray,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { alElegirPortada(indice) }
                    // pulsar la foto la convierte en portada
                ) {
                    AsyncImage(
                        model = uri,
                        contentDescription = "Foto del viaje",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Icono de portada
                    if (esPortada) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .padding(4.dp)
                                .background(NaranjaAccento, CircleShape)
                                .padding(4.dp)
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "Foto portada",
                                tint = Color.White,
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }

                    // Botón eliminar
                    IconButton(
                        onClick = { alEliminarFoto(uri) },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .size(28.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Eliminar foto",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }

            // Botón añadir foto (solo si hay menos de 5)
            if (fotos.size < 5) {
                item {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(VerdeTurquesaClaro)
                            .border(
                                width = 2.dp,
                                color = VerdeTurquesa,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable {
                                lanzador.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                        // ImageOnly → solo muestra imágenes, no vídeos
                                    )
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Añadir foto",
                                tint = VerdeTurquesa,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "Añadir",
                                style = MaterialTheme.typography.bodySmall,
                                color = VerdeTurquesa
                            )
                        }
                    }
                }
            }
        }
    }
}