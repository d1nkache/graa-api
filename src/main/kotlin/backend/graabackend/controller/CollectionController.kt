package backend.graabackend.controller

import backend.graabackend.model.response.CollectionResponse
import org.springframework.web.bind.annotation.PathVariable

interface CollectionController {
    suspend fun getCollection(collectionAddress: String): CollectionResponse
    suspend fun verifyCollection(collectionAddress: String): CollectionResponse
    fun deleteCollectionFromVerified(collectionAddress: String): CollectionResponse
//    suspend fun sortCollectionByPrice(@PathVariable ascending: Boolean): CollectionResponse
}