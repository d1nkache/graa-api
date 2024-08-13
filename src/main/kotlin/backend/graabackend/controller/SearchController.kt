package backend.graabackend.controller

import backend.graabackend.model.request.SearchRequest
import backend.graabackend.model.response.SearchResponse

interface SearchController {
    suspend fun globalSearchCollection(collectionAddress: String): SearchResponse
    suspend fun globalSearchNft(nftAddress: String): SearchResponse
    suspend fun globalSearchAccount(domain: String): SearchResponse
//    fun localSearchNft(request: SearchRequest): Any
}

