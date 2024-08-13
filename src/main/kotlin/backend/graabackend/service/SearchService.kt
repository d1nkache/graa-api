package backend.graabackend.service

import backend.graabackend.model.response.SearchResponse

interface SearchService {
    suspend fun globalSearchCollection(collectionAddress: String = "NULL", description: String = "NULL"): SearchResponse
    suspend fun globalSearchNft(nftAddress: String = "NULL", description: String = "NULL"): SearchResponse
    suspend fun globalSearchAccount(domain: String): SearchResponse
//    fun localSearchNft(nftAddress: String = "NULL", description: String = "NULL"): Any
}
