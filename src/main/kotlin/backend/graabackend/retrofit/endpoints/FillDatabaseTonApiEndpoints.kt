package backend.graabackend.retrofit.endpoints

import backend.graabackend.model.response.CollectionResponse
import backend.graabackend.model.response.NftResponse
import retrofit2.http.Path
import retrofit2.http.GET
import retrofit2.Call

interface FillDatabaseTonApiEndpoints {
    @GET("/v2/nfts/collections/{accountId}/items")
    fun getAllNftFromCollection(@Path("accountId") accountId: String): Call<CollectionResponse.NftItemsHelperResponse>

    @GET("/v2/nfts/collections/{accountId}")
    fun getCollectionMetadata(@Path("accountId") accountId: String): Call<CollectionResponse.CollectionMetadataHelperResponse>

    @GET("/v2/nfts/{accountId}")
    fun getNft(@Path("accountId") accountId: String): Call<NftResponse.NftMetadataHelperResponse>
}