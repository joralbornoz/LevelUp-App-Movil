package com.levelup.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.levelup.data.DatosUsuario
import com.levelup.data.DataProductos
import com.levelup.data.Productos
import com.levelup.data.Sucursal
import com.levelup.data.model.Usuario
import com.levelup.data.Compra
import com.levelup.data.repository.CompraRepository
import com.levelup.data.repository.ProductoRepository
import com.levelup.data.repository.SucursalRepository
import com.levelup.data.repository.UsuarioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import com.levelup.data.repository.DealsRepository
import com.levelup.data.external.Deal


class AppViewModel(app: Application) : AndroidViewModel(app) {

    private val datos = DatosUsuario(app)

    private val productoRepository = ProductoRepository()
    private val sucursalRepository = SucursalRepository()
    private val usuarioRepository = UsuarioRepository()

    private val compraRepository = CompraRepository()

    private val dealsRepository = DealsRepository()

    private val _deals = MutableStateFlow<List<Deal>>(emptyList())
    val deals: StateFlow<List<Deal>> = _deals.asStateFlow()

    // ----------------- COMPRA -----------------
    private val _comprasUsuario = MutableStateFlow<List<Compra>>(emptyList())
    val comprasUsuario: StateFlow<List<Compra>> = _comprasUsuario.asStateFlow()

    // ----------------- CARRITO -----------------
    private val _carrito = MutableStateFlow<List<String>>(emptyList())
    val carrito: StateFlow<List<String>> = _carrito


    // ----------------- USUARIO (LOCAL - DATASTORE) -----------------
    val nombreUsuario: StateFlow<String> = datos.nombreUsuario.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), ""
    )
    val edadUsuario: StateFlow<String> = datos.edadUsuario.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), ""
    )
    val emailUsuario: StateFlow<String> = datos.emailUsuario.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), ""
    )
    val direccionUsuario: StateFlow<String> = datos.direccionUsuario.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), ""
    )

    val preferenciasUsuario: StateFlow<String> = datos.preferenciasUsuario.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5_000), ""
    )

    val estaLogueado: StateFlow<Boolean> = nombreUsuario
        .map { it.isNotBlank() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    val ordenesCsv = datos.ordenesCsv.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5_000),
        ""
    )

    // ----------------- USUARIO (BACKEND - MICROSERVICIO) -----------------
    private val _usuarioBackend = MutableStateFlow<Usuario?>(null)
    val usuarioBackend: StateFlow<Usuario?> = _usuarioBackend.asStateFlow()

    private val _cargandoUsuario = MutableStateFlow(false)
    val cargandoUsuario: StateFlow<Boolean> = _cargandoUsuario.asStateFlow()

    private val _errorUsuario = MutableStateFlow<String?>(null)
    val errorUsuario: StateFlow<String?> = _errorUsuario.asStateFlow()

    // ----------------- PRODUCTOS (desde API) -----------------
    private val _productos = MutableStateFlow<List<Productos>>(emptyList())
    val productos: StateFlow<List<Productos>> = _productos.asStateFlow()

    private val _cargandoProductos = MutableStateFlow(false)
    val cargandoProductos: StateFlow<Boolean> = _cargandoProductos.asStateFlow()

    private val _errorProductos = MutableStateFlow<String?>(null)
    val errorProductos: StateFlow<String?> = _errorProductos.asStateFlow()

    // ----------------- SUCURSALES (desde API) -----------------
    private val _sucursales = MutableStateFlow<List<Sucursal>>(emptyList())
    val sucursales: StateFlow<List<Sucursal>> = _sucursales.asStateFlow()

    private val _cargandoSuc = MutableStateFlow(false)
    val cargandoSuc: StateFlow<Boolean> = _cargandoSuc.asStateFlow()

    private val _errorSuc = MutableStateFlow<String?>(null)
    val errorSuc: StateFlow<String?> = _errorSuc.asStateFlow()

    init {

        viewModelScope.launch {
            datos.carritoIds.collect { csv ->
                _carrito.value = if (csv.isBlank()) emptyList()
                else csv.split(",").filter { it.isNotBlank() }
            }
        }


        viewModelScope.launch {
            emailUsuario.collect { email ->
                if (email.isNotBlank()) {
                    try {
                        _cargandoUsuario.value = true
                        _errorUsuario.value = null
                        val user = usuarioRepository.obtenerUsuarioPorEmail(email)
                        _usuarioBackend.value = user
                    } catch (e: Exception) {
                        _errorUsuario.value = "No se pudo sincronizar usuario: ${e.message}"
                    } finally {
                        _cargandoUsuario.value = false
                    }
                }
            }
        }
    }

    // --------- PRODUCTOS: llamada al microservicio ---------
    fun cargarProductosDesdeApi() {
        viewModelScope.launch {
            try {
                _cargandoProductos.value = true
                _errorProductos.value = null

                val lista = productoRepository.obtenerProductos()
                _productos.value = lista

            } catch (e: Exception) {
                _errorProductos.value = e.message ?: "Error cargando productos"
            } finally {
                _cargandoProductos.value = false
            }
        }
    }

    // --------- SUCURSALES: llamada al microservicio ---------
    fun cargarSucursalesDesdeApi() {
        viewModelScope.launch {
            try {
                _cargandoSuc.value = true
                _errorSuc.value = null

                val resultado = sucursalRepository.obtenerSucursales()
                _sucursales.value = resultado

            } catch (e: Exception) {
                _errorSuc.value = e.message ?: "Error desconocido"
            } finally {
                _cargandoSuc.value = false
            }
        }
    }

    // --------- LOGIN contra microservicio ---------
    fun loginUsuario(email: String, password: String) {
        viewModelScope.launch {
            try {
                _cargandoUsuario.value = true
                _errorUsuario.value = null


                val user: Usuario? = usuarioRepository.obtenerUsuarioPorEmail(email)


                if (user == null) {
                    _usuarioBackend.value = null
                    _errorUsuario.value = "Usuario no encontrado"
                    return@launch
                }

                if (user.password == password) {
                    // Login OK -> guardamos en DataStore y en estado
                    datos.guardarUsuario(
                        nombreUsuario = user.nombre,
                        edadUsuario = user.edad?.toString() ?: "",
                        emailUsuario = user.email,
                        direccionUsuario = user.direccion ?: "",
                        preferenciasUsuario = user.preferencias ?: ""
                    )
                    _usuarioBackend.value = user
                } else {
                    _usuarioBackend.value = null
                    _errorUsuario.value = "Contraseña incorrecta"
                }

            } catch (e: Exception) {
                _usuarioBackend.value = null
                _errorUsuario.value = "Usuario no encontrado o error: ${e.message}"
            } finally {
                _cargandoUsuario.value = false
            }
        }
    }


    // --------- REGISTRO contra microservicio ---------
    fun registrarUsuario(nombre: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _cargandoUsuario.value = true
                _errorUsuario.value = null

                val nuevoUsuario = Usuario(
                    id = 0L,
                    nombre = nombre,
                    email = email,
                    edad = null,
                    preferencias = null,
                    direccion = null,
                    rol = "USUARIO",
                    password = password
                )

                val creado = usuarioRepository.crearUsuario(nuevoUsuario)


                datos.guardarUsuario(
                    nombreUsuario = creado.nombre,
                    edadUsuario = creado.edad?.toString() ?: "",
                    emailUsuario = creado.email,
                    direccionUsuario = creado.direccion ?: "",
                    preferenciasUsuario = creado.preferencias ?: ""
                )

                _usuarioBackend.value = creado
            } catch (e: Exception) {
                _usuarioBackend.value = null

                _errorUsuario.value = when (e) {
                    is HttpException -> {
                        when (e.code()) {
                            409 -> "Error: el correo ya existe en el sistema"
                            else -> "Error HTTP ${e.code()} al registrar usuario"
                        }
                    }

                    else -> "Error registrando usuario: ${e.message}"
                }
            }
        }
    }

    // --------- PERFIL / LOCAL + BACKEND ---------

    fun guardarPerfil(
        nombre: String,
        email: String,
        edad: String,
        direccion: String = "",
        preferencias: String = ""
    ) = viewModelScope.launch {
        // Convertimos a Int solo si se puede
        val edadInt = edad.toIntOrNull()
        // Tomamos las preferencias que llegaron, o si están vacías,
        // usamos lo que ya está guardado en DataStore
        val prefsFinal = if (preferencias.isNotBlank()) {
            preferencias
        } else {
            preferenciasUsuario.value
        }

        // Guardar local en DataStore
        datos.guardarUsuario(
            nombreUsuario = nombre,
            edadUsuario = edad,
            emailUsuario = email,
            direccionUsuario = direccion,
            preferenciasUsuario = prefsFinal
        )

        // Intentar actualizar también en el backend
        try {
            _cargandoUsuario.value = true
            _errorUsuario.value = null


            val backendUser: Usuario? =
                usuarioBackend.value ?: usuarioRepository.obtenerUsuarioPorEmail(email)


            val actual: Usuario = backendUser ?: run {
                _errorUsuario.value = "Usuario no existe en el backend para actualizar."
                _cargandoUsuario.value = false
                return@launch
            }


            val actualizado = actual.copy(
                nombre = nombre,
                edad = edadInt ?: actual.edad,
                email = email,
                direccion = direccion,
                preferencias = prefsFinal
            )

            val resultado = usuarioRepository.actualizarUsuario(actual.id, actualizado)
            _usuarioBackend.value = resultado

        } catch (e: Exception) {
            _errorUsuario.value = "Error actualizando usuario: ${e.message}"
        } finally {
            _cargandoUsuario.value = false
        }
    }

    // --------- LÓGICA DEL CARRITO ---------
    private val _mensajeCarrito = MutableStateFlow<String?>(null)
    val mensajeCarrito: StateFlow<String?> = _mensajeCarrito.asStateFlow()

    fun agregarEnCarrito(codigo: String) {
        val producto = productos.value.firstOrNull { it.codigo == codigo }
            ?: DataProductos.firstOrNull { it.codigo == codigo }

        if (producto == null) {
            _mensajeCarrito.value = "Producto no encontrado."
            return
        }

        val stockDisponible = producto.stock
        val cantidadActualEnCarrito = _carrito.value.count { it == codigo }

        if (cantidadActualEnCarrito >= stockDisponible) {
            _mensajeCarrito.value = "No puedes agregar más. Stock disponible: $stockDisponible"
            return
        }

        _carrito.value = _carrito.value + codigo
        persistCarrito()
        _mensajeCarrito.value = null
    }
    // --------- GUARDAR CARRITO EN DATASTORE ---------
    private fun persistCarrito() = viewModelScope.launch {
        datos.guardarCarrito(_carrito.value.joinToString(","))
    }

    fun limpiarCarrito() {
        _carrito.value = emptyList()
        persistCarrito()
    }
    // --------- TOTAL COMPRA ---------
    fun total(): Int {
        val listaProductos = productos.value   // productos cargados desde API

        return carrito.value.sumOf { codigo ->
            listaProductos.firstOrNull { it.codigo == codigo }?.precio ?: 0
        }
    }


    fun pagarYGuardarOrden(onFinish: (() -> Unit)? = null) = viewModelScope.launch {
        val codigos = _carrito.value
        if (codigos.isEmpty()) {
            onFinish?.invoke()
            return@launch
        }

        val subtotal = total()
        val totalIva = (subtotal * 1.19).toInt()
        val cantidad = codigos.size
        val fechaMs = System.currentTimeMillis()


        val venta: Map<String, Int> = codigos.groupingBy { it }.eachCount()

        val detalle: String = venta.entries.joinToString("; ") { (codigo, cant) ->
            val producto = productos.value.firstOrNull { it.codigo == codigo }
                ?: DataProductos.firstOrNull { it.codigo == codigo }

            val nombre = producto?.nombre ?: "Producto $codigo"
            "$nombre x$cant"
        }
        val linea = "$fechaMs;$cantidad;$subtotal;$totalIva"
        datos.agregarOrden(linea)

        // 2) Crear compra en backend
        val email = emailUsuario.value
        if (email.isNotBlank()) {
            try {
                compraRepository.crearCompra(
                    emailUsuario = email,
                    total = totalIva,
                    detalle = detalle
                )

                cargarHistorialCompras()
            } catch (e: Exception) {

            }
        }


        val grupos: Map<String, Int> = codigos.groupingBy { it }.eachCount()
        for ((codigo, cantidadComprada) in grupos) {
            val productoActual = productos.value.firstOrNull { it.codigo == codigo }
                ?: DataProductos.firstOrNull { it.codigo == codigo }

            if (productoActual != null) {
                val nuevoStock = (productoActual.stock - cantidadComprada).coerceAtLeast(0)
                try {
                    val productoActualizado = productoActual.copy(stock = nuevoStock)
                    productoRepository.actualizarProducto(productoActualizado)
                } catch (_: Exception) {}
            }
        }


        limpiarCarrito()


        try {
            val lista = productoRepository.obtenerProductos()
            _productos.value = lista
        } catch (_: Exception) {}

        onFinish?.invoke()
    }

    fun cargarHistorialCompras() {
        val email = emailUsuario.value
        if (email.isBlank()) return

        viewModelScope.launch {
            try {
                val lista = compraRepository.obtenerComprasPorUsuario(email)
                _comprasUsuario.value = lista
            } catch (e: Exception) {

            }
        }
    }



    // --------- LOGOUT ---------
    fun logout() = viewModelScope.launch {
        datos.guardarUsuario("", "", "", "")
        datos.guardarCarrito("")
        _carrito.value = emptyList()
        _usuarioBackend.value = null
    }
    // ----- API EXTERNA: OFERTAS GAMER -----
    private val _ofertasExternas = MutableStateFlow<List<Deal>>(emptyList())
    val ofertasExternas: StateFlow<List<Deal>> = _ofertasExternas.asStateFlow()

    private val _cargandoOfertasExternas = MutableStateFlow(false)
    val cargandoOfertasExternas: StateFlow<Boolean> = _cargandoOfertasExternas.asStateFlow()

    private val _errorOfertasExternas = MutableStateFlow<String?>(null)
    val errorOfertasExternas: StateFlow<String?> = _errorOfertasExternas.asStateFlow()

    fun cargarOfertasExternas() {
        viewModelScope.launch {
            try {
                _cargandoOfertasExternas.value = true
                _errorOfertasExternas.value = null
                _ofertasExternas.value = dealsRepository.obtenerDeals()
            } catch (e: Exception) {
                _errorOfertasExternas.value = "No se pudieron cargar las ofertas: ${e.message}"
            } finally {
                _cargandoOfertasExternas.value = false
            }
        }
    }






}
