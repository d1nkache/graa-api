package backend.graabackend.controller.impl

import backend.graabackend.controller.SearchController
import backend.graabackend.controller.helpers.searchControllerHelper
import backend.graabackend.model.request.SearchRequest
import backend.graabackend.model.response.SearchResponse
import backend.graabackend.service.SearchService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/search")
class SearchControllerImpl(
    private val searchService: SearchService
) : SearchController {
    @GetMapping("/collection/{collectionAddress}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun globalSearchCollection(@PathVariable collectionAddress: String): SearchResponse = searchControllerHelper(
        firstArg = collectionAddress,
        secondArg = null,
        thirdArg = null,
        errorMessage = "Collection address is uncorrected",
        serviceMethod1 = searchService::globalSearchCollection
    )

    @GetMapping("/nft/{nftAddress}")
    @CrossOrigin(origins = ["*"], maxAge = 3600)
    override suspend fun globalSearchNft(@PathVariable nftAddress: String): SearchResponse = searchControllerHelper(
        firstArg = nftAddress,
        secondArg = null,
        thirdArg = null,
        errorMessage = "Nft address is uncorrected",
        serviceMethod1 = searchService::globalSearchNft
    )

    @GetMapping("/account/{domain}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun globalSearchAccount(@PathVariable domain: String ): SearchResponse = searchControllerHelper(
        firstArg = domain,
        secondArg = null,
        thirdArg = null,
        errorMessage = "Domain name is uncorrected",
        serviceMethod1 = searchService::globalSearchAccount
    )

    @GetMapping("/local-search/nft/{accountId}")
    @CrossOrigin(origins = ["http://localhost:5173"], maxAge = 3600)
    override suspend fun localSearchNft(@PathVariable accountId: String, @RequestBody searchRequest: SearchRequest): SearchResponse = searchControllerHelper(
        firstArg = accountId,
        secondArg = searchRequest.address,
        thirdArg = searchRequest.name,
        errorMessage = "Nft Address is uncorrected",
        serviceMethod2 = searchService::localSearchNft
    )
}