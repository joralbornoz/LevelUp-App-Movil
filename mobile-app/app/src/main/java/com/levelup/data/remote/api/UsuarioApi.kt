package com.levelup.data.remote.api



import com.levelup.data.remote.dto.UsuarioDto
import retrofit2.http.*

interface UsuarioApi {

    // 👑 ADMIN: listar usuarios
    @GET("api/usuarios")
    suspend fun obtenerUsuarios(): List<UsuarioDto>

    @GET("api/usuarios/{id}")
    suspend fun getUsuarioPorId(@Path("id") id: Long): UsuarioDto

    @GET("api/usuarios/email/{email}")
    suspend fun getUsuarioPorEmail(@Path("email") email: String): UsuarioDto

    @POST("api/usuarios")
    suspend fun crearUsuario(@Body usuario: UsuarioDto): UsuarioDto

    @PUT("api/usuarios/{id}")
    suspend fun actualizarUsuario(
        @Path("id") id: Long,
        @Body usuario: UsuarioDto
    ): UsuarioDto

    // 👑 ADMIN: eliminar usuario
    @DELETE("api/usuarios/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Long)
}
