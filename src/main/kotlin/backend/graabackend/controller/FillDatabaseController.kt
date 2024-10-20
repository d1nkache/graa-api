package backend.graabackend.controller

import backend.graabackend.model.response.FillDatabaseResponse
import backend.graabackend.model.request.AddAddresses

interface FillDatabaseController {
    fun addCollectionsInDatabase(addresses: AddAddresses): FillDatabaseResponse
    fun addNftInDatabase(addresses: AddAddresses): FillDatabaseResponse
}