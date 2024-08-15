package backend.graabackend.retrofit.endpoints

import backend.graabackend.model.response.CollectionResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface CollectionControllerTonApiEndpoints {
    @GET("/v2/nfts/collections/{accountId}/items")
    suspend fun getAllNftFromCollection(@Path("accountId") accountId: String): CollectionResponse.AllCollectionNftResponse?
}