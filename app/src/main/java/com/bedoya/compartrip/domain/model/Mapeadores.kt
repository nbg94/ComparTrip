package com.bedoya.compartrip.domain.model

import com.bedoya.compartrip.data.local.entity.*

// ---- EntidadViaje ↔ Viaje ----

fun EntidadViaje.aDominio(): Viaje {
    return Viaje(
        idViaje = idViaje,
        idPublicador = idPublicador,
        origen = origen,
        destino = destino,
        fecha = fecha,
        tipo = TipoViaje.valueOf(tipo),
        plazasDisponibles = plazasDisponibles,
        descripcion = descripcion,
        precio = precio,
        admiteFumadores = admiteFumadores,
        admiteMascotas = admiteMascotas,
        soloMujeres = soloMujeres,
        soloHombres = soloHombres,
        edadMinima = edadMinima,
        edadMaxima = edadMaxima,
        generoPreferido = generoPreferido,
        edadMinimaPreferida = edadMinimaPreferida,
        edadMaximaPreferida = edadMaximaPreferida,
        estado = EstadoViaje.valueOf(estado),
        urlsFotos = if (urlsFotos.isBlank()) emptyList()
        else urlsFotos.split(",")
        // guardamos las fotos como "url1,url2,url3" y las separamos al leer
    )
}

fun Viaje.aEntidad(): EntidadViaje {
    return EntidadViaje(
        idViaje = idViaje,
        idPublicador = idPublicador,
        origen = origen,
        destino = destino,
        fecha = fecha,
        tipo = tipo.name,
        plazasDisponibles = plazasDisponibles,
        descripcion = descripcion,
        precio = precio,
        admiteFumadores = admiteFumadores,
        admiteMascotas = admiteMascotas,
        soloMujeres = soloMujeres,
        soloHombres = soloHombres,
        edadMinima = edadMinima,
        edadMaxima = edadMaxima,
        generoPreferido = generoPreferido,
        edadMinimaPreferida = edadMinimaPreferida,
        edadMaximaPreferida = edadMaximaPreferida,
        estado = estado.name,
        urlsFotos = urlsFotos.joinToString(",")
    )
}

// ---- EntidadUsuario ↔ Usuario ----

fun EntidadUsuario.aDominio(): Usuario {
    return Usuario(
        idUsuario = idUsuario,
        nombre = nombre,
        correo = correo,
        urlFoto = urlFoto,
        biografia = biografia,
        valoracionMedia = valoracionMedia,
        totalViajes = totalViajes,
        dniVerificado = dniVerificado,
        creadoEn = creadoEn
    )
}

fun Usuario.aEntidad(): EntidadUsuario {
    return EntidadUsuario(
        idUsuario = idUsuario,
        nombre = nombre,
        correo = correo,
        urlFoto = urlFoto,
        biografia = biografia,
        valoracionMedia = valoracionMedia,
        totalViajes = totalViajes,
        dniVerificado = dniVerificado,
        creadoEn = creadoEn
    )
}

// ---- EntidadMensaje ↔ Mensaje ----

fun EntidadMensaje.aDominio(): Mensaje {
    return Mensaje(
        idMensaje = idMensaje,
        idViaje = idViaje,
        idEmisor = idEmisor,
        idReceptor = idReceptor,
        contenido = contenido,
        enviadoEn = enviadoEn,
        leido = leido
    )
}

fun Mensaje.aEntidad(): EntidadMensaje {
    return EntidadMensaje(
        idMensaje = idMensaje,
        idViaje = idViaje,
        idEmisor = idEmisor,
        idReceptor = idReceptor,
        contenido = contenido,
        enviadoEn = enviadoEn,
        leido = leido
    )
}