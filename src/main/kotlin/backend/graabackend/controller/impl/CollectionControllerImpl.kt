package backend.graabackend.controller.impl

import backend.graabackend.controller.helpers.CollectionControllerHelper
import backend.graabackend.model.response.CollectionResponse
import backend.graabackend.controller.CollectionController

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/collection")
class CollectionControllerImpl(
    private val collectionControllerHelper: CollectionControllerHelper
): CollectionController {
    @GetMapping("/get/{collectionAddress}/page/{pageNumber}/page-size/{pageSize}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun getCollection(@PathVariable collectionAddress: String, @PathVariable pageNumber: Int, @PathVariable pageSize: Int): CollectionResponse {
        val hexCollectionAddress: String = collectionControllerHelper.changeCollectionAddressFormat(collectionAddress = collectionAddress)

        return collectionControllerHelper.checkControllerVariablesOnError(
            collectionAddress = hexCollectionAddress,
            pageNumber = pageNumber,
            pageSize = pageSize,
            ascending = null,
            methodName = "getCollection"
        )
    }

    @PostMapping("/add-to-verified/{collectionAddress}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun verifyCollection(@PathVariable collectionAddress: String): CollectionResponse {
        val hexCollectionAddress: String = collectionControllerHelper.changeCollectionAddressFormat(collectionAddress = collectionAddress)

        return collectionControllerHelper.checkControllerVariablesOnError(
            collectionAddress = hexCollectionAddress,
            pageNumber = null,
            pageSize = null,
            ascending = null,
            methodName = "verifyCollection"
        )
    }

    @DeleteMapping("/delete-from-verified/{collectionAddress}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun deleteCollectionFromVerified(@PathVariable collectionAddress: String): CollectionResponse {
        val hexCollectionAddress: String = collectionControllerHelper.changeCollectionAddressFormat(collectionAddress = collectionAddress)

        return collectionControllerHelper.checkControllerVariablesOnError(
            collectionAddress = hexCollectionAddress,
            pageNumber = null,
            pageSize = null,
            ascending = null,
            methodName = "deleteCollectionFromVerified"
        )
    }

    @GetMapping("/sort-collection-nfts/{ascending}/{collectionAddress}/page/{pageNumber}/pageSize/{pageSize}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun sortCollectionByPrice(@PathVariable ascending: Boolean, @PathVariable collectionAddress: String, @PathVariable pageNumber: Int, @PathVariable pageSize: Int): CollectionResponse {
       val hexCollectionAddress: String = collectionControllerHelper.changeCollectionAddressFormat(collectionAddress = collectionAddress)

        return collectionControllerHelper.checkControllerVariablesOnError(
            collectionAddress = hexCollectionAddress,
            pageNumber = pageNumber,
            pageSize = pageSize,
            ascending = ascending,
            methodName = "sortCollectionByPrice"
        )
    }
}

