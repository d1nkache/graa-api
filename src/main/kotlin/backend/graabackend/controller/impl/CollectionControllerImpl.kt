package backend.graabackend.controller.impl

import backend.graabackend.controller.CollectionController
import backend.graabackend.controller.helpers.collectionControllerHelper
import backend.graabackend.model.response.CollectionResponse
import backend.graabackend.service.CollectionService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/collection")
class CollectionControllerImpl(
    private val collectionService: CollectionService
) : CollectionController {
    @GetMapping("/get/{collectionAddress}/page/{pageNumber}/page-size/{pageSize}")
    @CrossOrigin(origins = ["*"], maxAge = 3600)
    override suspend fun getCollection(@PathVariable collectionAddress: String, @PathVariable pageNumber: Int, @PathVariable pageSize: Int): CollectionResponse = collectionControllerHelper(
        firstArg = collectionAddress,
        secondArg = null,
        pageNumber = pageNumber,
        pageSize = pageSize,
        errorMessage = "Collection address is uncorrected",
        serviceMethod1 = collectionService::getCollection,
        serviceMethod2 = null
    )

    @PostMapping("/add-to-verified/{collectionAddress}")
    @CrossOrigin(origins = ["*"], maxAge = 3600)
    override suspend fun verifyCollection(@PathVariable collectionAddress: String): CollectionResponse = collectionService.verifiedCollection(
        collectionAddress = collectionAddress
    )

    @DeleteMapping("/delete-from-verified/{collectionAddress}")
    @CrossOrigin(origins = ["*"], maxAge = 3600)
    override fun deleteCollectionFromVerified(@PathVariable collectionAddress: String): CollectionResponse = collectionService.deleteCollectionFromVerified(
        collectionAddress = collectionAddress
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

