package com.bedoya.compartrip.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bedoya.compartrip.R
import com.bedoya.compartrip.ui.theme.CremeFondo
import com.bedoya.compartrip.ui.theme.VerdeTurquesa
import com.bedoya.compartrip.ui.theme.VerdeTurquesaClaro

@Composable
fun PantallaLogin(
    alIniciarSesion: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CremeFondo)   // fondo crema en toda la pantalla
    ) {
        // Franja turquesa arriba
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
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
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_compartrip_preview),
                contentDescription = "Logo Compartrip",
                modifier = Modifier.size(160.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Comparte tu viaje,\ncomparte la experiencia",
                style = MaterialTheme.typography.titleMedium,
                color = VerdeTurquesa,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(80.dp))

            // Tarjeta de login
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
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = alIniciarSesion,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = VerdeTurquesa
                        )
                    ) {
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