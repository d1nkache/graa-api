package backend.graabackend.controller.impl

import backend.graabackend.controller.SearchController
import backend.graabackend.controller.helpers.searchControllerHelper
import backend.graabackend.model.request.SearchRequest
import backend.graabackend.model.response.SearchResponse
import backend.graabackend.service.SearchService
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/search")
class SearchControllerImpl(
    private val searchService: SearchService
) : SearchController {
    @GetMapping("/collection/{collectionAddress}")
    override suspend fun globalSearchCollection(@PathVariable collectionAddress: String): SearchResponse = searchControllerHelper(
        firstArg = collectionAddress,
        secondArg = null,
        errorMessage = "Collection address is uncorrected",
        serviceMethod1 = searchService::globalSearchCollection
    )

    @GetMapping("/nft/{nftAddress}")
    override suspend fun globalSearchNft(@PathVariable nftAddress: String): SearchResponse = searchControllerHelper(
        firstArg = nftAddress,
        secondArg = null,
        errorMessage = "Nft address is Null",
        serviceMethod1 = searchService::globalSearchNft
    )

    @GetMapping("/account/{domain}")
    override suspend fun globalSearchAccount(@PathVariable domain: String ): SearchResponse = searchControllerHelper(
        firstArg = domain,
        secondArg = null,
        errorMessage = "Domain name is Null",
        serviceMethod1 = searchService::globalSearchAccount
    )

    @GetMapping("/local-search/nft/{accountId}/{nftAddress}")
    override suspend fun localSearchNft(@PathVariable accountId: String, @PathVariable nftAddress: String): SearchResponse = searchControllerHelper(
        firstArg = accountId,
        secondArg = nftAddress,
        errorMessage = "Nft Address is uncorrected",
        serviceMethod2 = searchService::localSearchNft
    )
}