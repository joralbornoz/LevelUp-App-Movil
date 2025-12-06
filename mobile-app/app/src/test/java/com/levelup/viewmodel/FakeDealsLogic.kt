package com.levelup.viewmodel

import com.levelup.data.external.Deal
import com.levelup.data.repository.interfaces.IDealsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class FakeDealsLogic(
    private val dealsRepository: IDealsRepository
) {

    private val _ofertas = MutableStateFlow<List<Deal>>(emptyList())
    val ofertas: StateFlow<List<Deal>> = _ofertas.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    suspend fun cargarOfertas() {
        try {
            val lista = dealsRepository.obtenerDeals()
            _ofertas.value = lista
            _error.value = null
        } catch (e: Exception) {
            _ofertas.value = emptyList()
            _error.value = "No se pudieron cargar las ofertas"
        }
    }
}
