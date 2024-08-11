package backend.graabackend.controller.impl

import backend.graabackend.controller.CollectionController
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController("/collection")
class CollectionControllerImpl : CollectionController {
    @GetMapping("/{collectionAddress}")
    override fun getCollectionByAddress(@PathVariable collectionAddress: String) {
        TODO("Not yet implemented")
    }

    override fun sortNftByPrice() {
        TODO("Not yet implemented")
    }

    override fun getAllCollections() {
        TODO("Not yet implemented")
    }

}