package com.bedoya.compartrip.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color

private val EsquemaClaro = lightColorScheme(
    primary = VerdeTurquesa,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = VerdeTurquesaContainer,
    onPrimaryContainer = Color(0xFF00201C),

    secondary = VerdeTurquesaOscuro,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = VerdeTurquesaClaro,
    onSecondaryContainer = Color(0xFF00201C),

    background = CremeFondo,
    onBackground = Color(0xFF111111),

    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF111111),
    onSurfaceVariant = GrisTexto,

    error = RojoError,
    outline = GrisBorde
)

private val EsquemaOscuro = darkColorScheme(
    primary = VerdeTurquesa,
    onPrimary = Color(0xFF00201C),
    primaryContainer = VerdeTurquesaOscuro,
    onPrimaryContainer = VerdeTurquesaClaro,

    secondary = VerdeTurquesaClaro,
    onSecondary = Color(0xFF00201C),
    secondaryContainer = VerdeTurquesaOscuro,
    onSecondaryContainer = VerdeTurquesaClaro,

    background = FondoOscuro,
    onBackground = Color(0xFFE0F2EF),

    surface = SuperficieOscura,
    onSurface = Color(0xFFE0F2EF),
    onSurfaceVariant = Color(0xFFAAC5C1),

    error = Color(0xFFCF6679),
    outline = Color(0xFF4A6663)
)

@Composable
fun CompartripTheme(
    temaOscuro: Boolean = isSystemInDarkTheme(),
    // Dynamic color desactivado — usamos nuestra paleta siempre
    contenido: @Composable () -> Unit
) {
    val esquemaColores = if (temaOscuro) EsquemaOscuro else EsquemaClaro

    MaterialTheme(
        colorScheme = esquemaColores,
        typography = Tipografia,
        content = contenido
    )
}