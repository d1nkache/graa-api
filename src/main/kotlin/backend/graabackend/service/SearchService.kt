package backend.graabackend.service

import backend.graabackend.model.response.SearchResponse

interface SearchService {
    suspend fun globalSearchCollection(searchString: String): SearchResponse
    suspend fun globalSearchNft(collectionAddress: String?, searchString: String): SearchResponse
    suspend fun globalSearchAccount(accountId: String): SearchResponse
    suspend fun localSearchNft(accountId: String, searchString: String): SearchResponse
}
