package backend.graabackend.controller

import org.springframework.web.bind.annotation.PathVariable

interface CollectionController {
    fun getCollectionByAddress(@PathVariable collectionAddress: String)
    fun sortNftByPrice()
    fun getAllCollections()
}