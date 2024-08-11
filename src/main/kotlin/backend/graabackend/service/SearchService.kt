package backend.graabackend.service

import backend.graabackend.model.response.SearchCollectionResponse

interface SearchService {
    fun globalSearchCollection(collectionAddress: String = "NULL", description: String = "NULL"): SearchCollectionResponse
    fun globalSearchNft(nftAddress: String = "NULL", description: String = "NULL"): Any
    fun globalSearchAccount(domain: String): Any
    fun localSearchNft(nftAddress: String = "NULL", description: String = "NULL"): Any
}
