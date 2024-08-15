package backend.graabackend.controller.impl

import backend.graabackend.controller.CollectionController
import backend.graabackend.controller.helpers.collectionControllerHelper
import backend.graabackend.model.response.CollectionResponse
import backend.graabackend.service.CollectionService
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController("/collection")
class CollectionControllerImpl(
    private val collectionService: CollectionService
) : CollectionController {
    @GetMapping("/get/{collectionAddress}")
    @CrossOrigin(origins = ["*"], maxAge = 3600)
    override suspend fun getCollection(@PathVariable collectionAddress: String): CollectionResponse = collectionControllerHelper(
        firstArg = collectionAddress,
        secondArg = null,
        errorMessage = "Collection address is uncorrected",
        serviceMethod1 = collectionService::getCollection,
        serviceMethod2 = null
    )

//    @GetMapping("/sort/{ascending}")
//    @CrossOrigin(origins = ["*"], maxAge = 3600)
//    override suspend fun sortCollectionByPrice(@PathVariable ascending: Boolean): CollectionResponse = collectionControllerHelper(
//        firstArg = null,
//        secondArg = ascending,
//        errorMessage = "Ascending is uncorrected",
//        serviceMethod1 = null,
//        serviceMethod2 = collectionService::sortCollectionByPrice
//    )
}

