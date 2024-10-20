package backend.graabackend.controller.impl

import backend.graabackend.controller.helpers.SearchControllerHelper
import backend.graabackend.model.response.SearchResponse
import backend.graabackend.controller.SearchController
import org.springframework.data.jpa.repository.Query
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/search")
class SearchControllerImpl(
    private val searchControllerHelper: SearchControllerHelper
) : SearchController {
    @GetMapping("/collection/{searchString}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun globalSearchCollection(@PathVariable searchString: String): SearchResponse {

        return searchControllerHelper.checkControllerVariablesOnError(
            itemAddress = null,
            accountId = null,
            searchString = searchString,
            methodName = "globalSearchCollection"
        )
    }

    @GetMapping("/nft/{searchString}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun globalSearchNft(@RequestParam collectionAddress: String?, @PathVariable searchString: String): SearchResponse {
        val hexCollectionAddress = collectionAddress?.let { searchControllerHelper.changeAddressFormat(it) }

        return searchControllerHelper.checkControllerVariablesOnError(
            itemAddress = collectionAddress,
            accountId = null,
            searchString = searchString,
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