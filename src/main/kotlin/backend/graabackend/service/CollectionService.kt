package backend.graabackend.service

import backend.graabackend.model.response.CollectionResponse
import org.springframework.web.bind.annotation.PathVariable

interface CollectionService {
    suspend fun getCollection(collectionAddress: String): CollectionResponse
    suspend fun sortCollectionByPrice(ascending: Boolean, collectionAddress: String): CollectionResponse
}