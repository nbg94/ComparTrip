package com.bedoya.compartrip.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaLogin(
    alIniciarSesion: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Compartrip",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Comparte tu viaje",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = alIniciarSesion,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar con Auth0")
        }
    }
}