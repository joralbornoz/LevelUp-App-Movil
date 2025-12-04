package com.levelup.data.repository.interfaces

import com.levelup.data.external.Deal

interface IDealsRepository {
    suspend fun obtenerDeals(): List<Deal>
}