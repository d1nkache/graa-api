package backend.graabackend.service

import backend.graabackend.model.response.CollectionResponse

interface CollectionService {
    suspend fun getCollection(collectionAddress: String, pageNumber: Int, pageSize: Int): CollectionResponse
    suspend fun sortCollectionByPrice(ascending: Boolean, collectionAddress: String, pageNumber: Int, pageSize: Int): CollectionResponse
    suspend fun verifiedCollection(collectionAddress: String): CollectionResponse
    fun deleteCollectionFromVerified(collectionAddress: String): CollectionResponse
}