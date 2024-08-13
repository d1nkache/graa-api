package backend.graabackend.controller

import org.springframework.web.bind.annotation.PathVariable

interface CollectionController {
    suspend fun getCollectionByAddress(@PathVariable collectionAddress: String): Any
    fun sortNftByPrice()
    fun getAllCollections()
}