package backend.graabackend.service

import backend.graabackend.model.response.FillDatabaseResponse

interface FillDatabaseService {
    fun addCollectionsInDatabase(addresses: List<String>): FillDatabaseResponse
    fun addNftInDatabase(addresses: List<String>): FillDatabaseResponse
}