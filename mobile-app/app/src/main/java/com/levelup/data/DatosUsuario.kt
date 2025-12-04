package com.levelup.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("levelup_Datos")

object Keys {
    val NOMBRE_USUARIO = stringPreferencesKey("nombre_usuario")
    val EDAD_USUARIO = stringPreferencesKey("edad_usuario")

    val EMAIL_USUARIO = stringPreferencesKey("email_usuario")
    val DIRECCION_USUARIO = stringPreferencesKey("direccion_usuario")
    val PREFERENCIAS_USUARIO = stringPreferencesKey("preferencias_usuario")
    val CARRITO_IDS = stringPreferencesKey("carrito_ids")
    val ORDENES = stringPreferencesKey("ordenes_csv")

}

class DatosUsuario(private val context: Context) {

    val nombreUsuario: Flow<String> =
        context.dataStore.data.map { it[Keys.NOMBRE_USUARIO] ?: "" }
    val edadUsuario: Flow<String> =
        context.dataStore.data.map { it[Keys.EDAD_USUARIO] ?: "" }

    val emailUsuario: Flow<String> =
        context.dataStore.data.map { it[Keys.EMAIL_USUARIO] ?: "" }

    val direccionUsuario: Flow<String> =
        context.dataStore.data.map { it[Keys.DIRECCION_USUARIO] ?: "" }

    val preferenciasUsuario: Flow<String> =
        context.dataStore.data.map { it[Keys.PREFERENCIAS_USUARIO] ?: "" }

    val carritoIds: Flow<String> =
        context.dataStore.data.map { it[Keys.CARRITO_IDS] ?: "" }

    val ordenesCsv: Flow<String> =
        context.dataStore.data.map { it[Keys.ORDENES] ?: "" }

    suspend fun guardarUsuario(
        nombreUsuario: String,
        edadUsuario: String,
        emailUsuario: String,
        direccionUsuario: String = "",
        preferenciasUsuario: String = ""
    ) {
        context.dataStore.edit {
            it[Keys.NOMBRE_USUARIO] = nombreUsuario
            it[Keys.EDAD_USUARIO] = edadUsuario
            it[Keys.EMAIL_USUARIO] = emailUsuario
            it[Keys.DIRECCION_USUARIO] = direccionUsuario
            it[Keys.PREFERENCIAS_USUARIO] = preferenciasUsuario
        }
    }

    suspend fun guardarCarrito(idsCsv: String) {
        context.dataStore.edit { it[Keys.CARRITO_IDS] = idsCsv }
    }

    suspend fun agregarOrden(linea: String) {
        context.dataStore.edit { prefs ->
            val actual = prefs[Keys.ORDENES] ?: ""
            val nuevo = if (actual.isBlank()) linea else "$actual||$linea"
            prefs[Keys.ORDENES] = nuevo
        }
    }
}
