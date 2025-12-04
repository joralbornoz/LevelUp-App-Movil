package com.levelup.data.remote

import com.levelup.data.remote.api.CheapSharkApi
import com.levelup.data.remote.api.UsuarioApi
import com.levelup.data.remote.api.ProductoApi
import com.levelup.data.remote.api.SucursalApi
import com.levelup.data.remote.api.CompraApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory





object RetrofitClient {

    private const val BASE_URL_CHEAPSHARK = "https://www.cheapshark.com/api/1.0/"
    private const val BASE_URL_PRODUCTOS = "http://10.0.2.2:8081/"
    private const val BASE_URL_USUARIOS = "http://10.0.2.2:8082/"
    private const val BASE_URL_SUCURSALES = "http://10.0.2.2:8083/api/"



    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    // ----- Productos -----
    private val retrofitProductos: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_PRODUCTOS)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val productoApi: ProductoApi = retrofitProductos.create(ProductoApi::class.java)

    // ----- Sucursales -----
    private val retrofitSucursales: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_SUCURSALES)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val sucursalApi: SucursalApi = retrofitSucursales.create(SucursalApi::class.java)

    // ----- Usuarios -----
    private val retrofitUsuarios: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_USUARIOS)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val usuarioApi: UsuarioApi = retrofitUsuarios.create(UsuarioApi::class.java)

    // ----- Compras -----
    private val retrofitCompras: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_USUARIOS)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val compraApi: CompraApi = retrofitCompras.create(CompraApi::class.java)

    // ------------ RETROFIT CHEAPSHARK --------------
    private val retrofitCheapShark: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL_CHEAPSHARK)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val cheapSharkApi: CheapSharkApi =
        retrofitCheapShark.create(CheapSharkApi::class.java)

}


