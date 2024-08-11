package backend.graabackend.controller.impl

import backend.graabackend.controller.SearchController
import backend.graabackend.controller.helpers.searchControllerHelper
import backend.graabackend.model.request.SearchRequest
import backend.graabackend.service.SearchService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/search")
class SearchControllerImpl(
    private val searchService: SearchService
) : SearchController {
    @GetMapping("/collection}")
    override fun globalSearchCollection(@RequestBody request: SearchRequest): Any = searchControllerHelper(
        firstArg = request.address,
        secondArg =  request.description,
        message = "Collection address or Description uncorrected",
        serviceMethod = searchService::globalSearchCollection
    )

    @GetMapping("/nft")
    override fun globalSearchNft(@RequestBody request: SearchRequest): Any = searchControllerHelper(
        firstArg = request.address,
        secondArg =  request.description,
        message = "Nft address is Null",
        serviceMethod = searchService::globalSearchNft
    )

    @GetMapping("/account")
    override fun globalSearchAccount(@RequestBody request: SearchRequest): Any = searchControllerHelper(
        firstArg = request.address,
        secondArg = null,
        message = "Domain name is Null",
        serviceMethod = searchService::globalSearchAccount
    )

    @GetMapping("/localSearch/nft")
    override fun localSearchNft(@RequestBody request: SearchRequest): Any = searchControllerHelper(
        firstArg = request.address,
        secondArg =  request.description,
        message = "Nft Address or Description are Null",
        serviceMethod = searchService::localSearchNft
    )
}