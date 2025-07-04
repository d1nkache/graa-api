package backend.graabackend.retrofit.endpoints

import backend.graabackend.model.response.CollectionResponse

import retrofit2.http.Path
import retrofit2.http.GET

interface CollectionControllerTonApiEndpoints {
    @GET("/v2/nfts/collections/{accountId}/items")
    suspend fun getAllNftFromCollection(@Path("accountId") accountId: String): CollectionResponse.NftItemsHelperResponse

    @GET("/v2/nfts/collections/{accountId}")
    suspend fun getCollectionMetadata(@Path("accountId") accountId: String): CollectionResponse.CollectionMetadataHelperResponse
}