package backend.graabackend.retrofit.endpoints

import backend.graabackend.model.response.SearchResponse

import retrofit2.http.Path
import retrofit2.http.GET

interface SearchControllerTonApiEndpoints {
    @GET("/v2/nfts/collections/{accountId}")
    suspend fun getNftCollection(@Path("accountId") accountId: String): SearchResponse.SearchItemResponse

    @GET("/v2/nfts/{accountId}")
    suspend fun getNft(@Path("accountId") accountId: String): SearchResponse.SearchItemResponse

    @GET("/v2/accounts/{accountId}")
    suspend fun getAccount(@Path("accountId") domain: String): SearchResponse.SearchAccountResponse

    @GET("/v2/accounts/{accountId}/nfts")
    suspend fun getUserNft(@Path("accountId") accountId: String): SearchResponse.LocalSearchItemResponse
}