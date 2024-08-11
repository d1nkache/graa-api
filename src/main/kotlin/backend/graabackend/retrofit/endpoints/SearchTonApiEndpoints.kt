package backend.graabackend.retrofit.endpoints

import backend.graabackend.model.response.SearchCollectionResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SearchTonApiEndpoints {
    @GET("/v2/nfts/collections/{account_id}")
    fun getNftCollection(@Path("accountId") accountId: String): SearchCollectionResponse?
}