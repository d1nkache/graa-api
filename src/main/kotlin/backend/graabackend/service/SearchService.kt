package backend.graabackend.service

import backend.graabackend.model.response.SearchResponse

interface SearchService {
    suspend fun globalSearchCollection(collectionAddress: String): SearchResponse
    suspend fun globalSearchNft(nftAddress: String): SearchResponse
    suspend fun globalSearchAccount(domain: String): SearchResponse
    suspend fun localSearchNft(accountId: String, nftAddress: String): SearchResponse
}
