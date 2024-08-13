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
@Api(value = "Search Controller", description = "REST APIs for search operations")
class SearchControllerImpl(
    private val searchService: SearchService
) : SearchController {
    @GetMapping("/collection/{collectionAddress}")
    @ApiOperation(value = "Получить пример", notes = "Возвращает пример объекта", response = SearchResponse.MetadataResponse::class)
    override suspend fun globalSearchCollection(@PathVariable collectionAddress: String): SearchResponse = searchControllerHelper(
        firstArg = collectionAddress,
        secondArg = null,
        errorMessage = "Collection address or Description uncorrected",
        serviceMethod = searchService::globalSearchCollection
    )

    @GetMapping("/nft/{nftAddress}")
    @ApiOperation(value = "Получить пример", notes = "Возвращает пример объекта", response = SearchResponse.MetadataResponse::class)
    override suspend fun globalSearchNft(@PathVariable nftAddress: String): SearchResponse = searchControllerHelper(
        firstArg = nftAddress,
        secondArg = null,
        errorMessage = "Nft address is Null",
        serviceMethod = searchService::globalSearchNft
    )

    @GetMapping("/account/{domain}")
    @ApiOperation(value = "Получить пример", notes = "Возвращает пример объекта", response = SearchResponse.SearchAccountResponse::class)
    override suspend fun globalSearchAccount(@PathVariable domain: String ): SearchResponse = searchControllerHelper(
        firstArg = domain,
        secondArg = null,
        errorMessage = "Domain name is Null",
        serviceMethod = searchService::globalSearchAccount
    )

//    @GetMapping("/localSearch/nft")
//    override fun localSearchNft(@RequestBody request: SearchRequest): Any = searchControllerHelper(
//        firstArg = request.address,
//        secondArg =  request.description,
//        message = "Nft Address or Description are Null",
//        serviceMethod = searchService::localSearchNft
//    )
}