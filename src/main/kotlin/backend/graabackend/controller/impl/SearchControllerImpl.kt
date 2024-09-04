package backend.graabackend.controller.impl

import backend.graabackend.controller.helpers.SearchControllerHelper
import backend.graabackend.model.response.SearchResponse
import backend.graabackend.controller.SearchController
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/search")
class SearchControllerImpl(
    private val searchControllerHelper: SearchControllerHelper
) : SearchController {
    @GetMapping("/collection/{collectionAddress}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun globalSearchCollection(@PathVariable collectionAddress: String): SearchResponse {
        val hexCollectionAddress = searchControllerHelper.changeAddressFormat(collectionAddress)

        return searchControllerHelper.checkControllerVariablesOnError(
            itemAddress = hexCollectionAddress,
            accountId = null,
            searchString = null,
            methodName = "globalSearchCollection"
        )
    }

    @GetMapping("/nft/{nftAddress}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun globalSearchNft(@PathVariable nftAddress: String): SearchResponse {
        val hexNftAddress = searchControllerHelper.changeAddressFormat(nftAddress)

        return searchControllerHelper.checkControllerVariablesOnError(
            itemAddress = hexNftAddress,
            accountId = null,
            searchString = null,
            methodName = "globalSearchNft"
        )
    }

    @GetMapping("/account/{accountId}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun globalSearchAccount(@PathVariable accountId: String): SearchResponse {
        val hexAccountId = searchControllerHelper.changeAddressFormat(accountId)

        return searchControllerHelper.checkControllerVariablesOnError(
            itemAddress = null,
            accountId = hexAccountId,
            searchString = null,
            methodName = "globalSearchAccount"
        )
    }

    @GetMapping("/local-search/nft/{accountId}/{searchString}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun localSearchNft(@PathVariable accountId: String, @PathVariable searchString: String): SearchResponse {
        val hexAccountId = searchControllerHelper.changeAddressFormat(accountId)

        return searchControllerHelper.checkControllerVariablesOnError(
            itemAddress = null,
            accountId = hexAccountId,
            searchString = searchString,
            methodName = "localSearchNft"
        )
    }
}