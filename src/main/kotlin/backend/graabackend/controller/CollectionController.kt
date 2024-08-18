package backend.graabackend.controller

import backend.graabackend.model.response.CollectionResponse
import org.springframework.web.bind.annotation.PathVariable

interface CollectionController {
    suspend fun getCollection(collectionAddress: String): CollectionResponse
//    suspend fun sortCollectionByPrice(@PathVariable ascending: Boolean): CollectionResponse
}