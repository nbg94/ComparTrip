package com.bedoya.compartrip.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bedoya.compartrip.R
import com.bedoya.compartrip.ui.theme.CremeFondo
import com.bedoya.compartrip.ui.theme.VerdeTurquesa
import com.bedoya.compartrip.ui.theme.VerdeTurquesaClaro

@Composable
fun PantallaLogin(
    alIniciarSesion: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()
    val contexto = LocalContext.current
    // LocalContext → forma de obtener el Context dentro de un Composable

    // Cuando el login es exitoso navegamos al Home
    LaunchedEffect(estado.loginExitoso) {
        if (estado.loginExitoso) alIniciarSesion()
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
                .height(300.dp)
                .background(
                    VerdeTurquesa,
                    shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp)
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_compartrip),
                contentDescription = "Logo Compartrip",
                modifier = Modifier.size(180.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Comparte tu viaje,\ncomparte la experiencia",
                style = MaterialTheme.typography.titleMedium,
                color = VerdeTurquesaClaro,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(80.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = CremeFondo),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Bienvenida",
                        style = MaterialTheme.typography.headlineMedium,
                        color = VerdeTurquesa
                    )
                    Text(
                        text = "Inicia sesión para publicar o unirte a viajes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    // Mensaje de error si algo falla
                    if (estado.error != null) {
                        Text(
                            text = estado.error!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { viewModel.iniciarSesion(contexto) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VerdeTurquesa
                        ),
                        enabled = !estado.estaCargando
                    ) {
                        if (estado.estaCargando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(
                                text = "Entrar con Auth0",
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    }
                }
            }
        }
    }
}