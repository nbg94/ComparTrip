package com.bedoya.compartrip.ui.screens.favoritos

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bedoya.compartrip.ui.theme.CremeFondo
import com.bedoya.compartrip.ui.theme.GrisTexto
import com.bedoya.compartrip.ui.theme.VerdeTurquesa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaFavoritos() {
    Scaffold(
        containerColor = CremeFondo,
        topBar = {
            TopAppBar(
                title = { Text("Favoritos") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = VerdeTurquesa,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { paddingInterno ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("❤️", fontSize = 48.sp)
                Text(
                    "Aún no tienes favoritos",
                    style = MaterialTheme.typography.titleMedium,
                    color = GrisTexto
                )
                Text(
                    "Guarda los viajes que te interesen\npara encontrarlos fácilmente",
                    style = MaterialTheme.typography.bodyMedium,
                    color = GrisTexto,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}