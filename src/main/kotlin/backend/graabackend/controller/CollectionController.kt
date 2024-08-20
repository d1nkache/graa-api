package backend.graabackend.controller

import backend.graabackend.model.response.CollectionResponse

interface CollectionController {
    suspend fun getCollection(collectionAddress: String, pageNumber: Int, pageSize: Int): CollectionResponse
    suspend fun verifyCollection(collectionAddress: String): CollectionResponse
    fun deleteCollectionFromVerified(collectionAddress: String): CollectionResponse
//    suspend fun sortCollectionByPrice(@PathVariable ascending: Boolean): CollectionResponse
}